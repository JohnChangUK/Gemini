package com.johnchang.Queueco;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static java.lang.Double.valueOf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.johnchang.Queueco.model.OrderBook;
import com.johnchang.Queueco.model.Price;
import com.johnchang.Queueco.model.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@SpringBootApplication
public class QueuecoApplication implements CommandLineRunner {

    OrderBook orderBook;
    List<OrderBook> bidList;
    List<OrderBook> askList;
    List<LinkedTreeMap> bestBidList;
    List<LinkedTreeMap> askPriceList;
    Map<Double, Double> bestBidMap;
    Map<Double, Double> askPriceMap;
    final ObjectMapper mapper;
    int count = 0;

    private static String GEMINI_WSS = "wss://api.gemini.com/v1/marketdata/btcusd";

    @Autowired
    public QueuecoApplication() {
        bidList = new ArrayList<>();
        askList = new ArrayList<>();
        bestBidList = new ArrayList<>();
        askPriceList = new ArrayList<>();
        mapper = new ObjectMapper();
        bestBidMap = new TreeMap<>(Collections.reverseOrder());
        askPriceMap = new TreeMap<>();
    }

    public static void main(String[] args) {
        SpringApplication.run(QueuecoApplication.class, args);
    }

    @Override
    public void run(String... args) {
        WebSocketConnectionManager connectionManager = new WebSocketConnectionManager(
                new StandardWebSocketClient(), new GeminiWebSocketHandler(), GEMINI_WSS);
        connectionManager.start();
    }

    private class GeminiWebSocketHandler extends TextWebSocketHandler {

        @Override
        public void handleTextMessage(WebSocketSession session, TextMessage message) {
            Map value = new Gson().fromJson(message.getPayload(), Map.class);
            List<LinkedTreeMap> events = (List<LinkedTreeMap>) value.get("events");

            LinkedTreeMap result = new LinkedTreeMap();
            for (LinkedTreeMap<Object, Object> map : events) {
                for (Map.Entry<Object, Object> entry : map.entrySet()) {
                    result.put(entry.getKey(), entry.getValue());
                }

                if (events.get(0).size() == 6) {
                    OrderBook orderBook = mapper.convertValue(result, OrderBook.class);
                    if (orderBook.getReason().equals("initial")) {
                        if (orderBook.getSide() != null && orderBook.getSide().equals("bid")) {
                            bidList.add(orderBook);
                        } else if (orderBook.getSide() != null && orderBook.getSide().equals("ask")) {
                            askList.add(orderBook);
                        }
                    }
                    if (orderBook.getReason().equals("cancel")) {
                        OrderBook book = bidList.stream().filter(x -> x.getPrice().equals(orderBook.getPrice())).findFirst().orElse(orderBook);
                        OrderBook askbook = askList.stream().filter(x -> x.getPrice().equals(orderBook.getPrice())).findFirst().orElse(orderBook);
                        if (orderBook.getSide() != null && orderBook.getSide().equals("bid")) {
                            int index = bidList.indexOf(book);
                            if (orderBook.getRemaining() == 0) {
                                bidList.remove(book);
                            } else {
                                bidList.get(index).setRemaining(orderBook.getRemaining());
                            }
                        } else if (orderBook.getSide() != null && orderBook.getSide().equals("ask")) {
                            int askIndex = askList.indexOf(askbook);
                            if (orderBook.getRemaining() == 0) {
                                askList.remove(askbook);
                            } else {
                                askList.get(askIndex).setRemaining(orderBook.getRemaining());
                            }
                        }
                    }
                    if (orderBook.getReason().equals("place")) {
                        Optional<OrderBook> book = bidList.stream().filter(x -> x.getPrice().equals(orderBook.getPrice())).findFirst();
                        Optional<OrderBook> askbook = askList.stream().filter(x -> x.getPrice().equals(orderBook.getPrice())).findFirst();
                        if (orderBook.getSide() != null && orderBook.getSide().equals("bid")) {
                            if (book.isPresent()) {
                                orderBook.setRemaining(orderBook.getRemaining() + book.get().getRemaining());
                            } else {
                                bidList.add(orderBook);
                            }
                        } else if (orderBook.getSide() != null && orderBook.getSide().equals("ask")) {
                            if (askbook.isPresent()) {
                                orderBook.setRemaining(orderBook.getRemaining() + askbook.get().getRemaining());
                            } else {
                                askList.add(orderBook);
                            }
                        }
                    }
                    if (orderBook.getReason().equals("trade")) {
                        if (orderBook.getSide() != null && orderBook.getSide().equals("bid")) {
                            OrderBook book = bidList.stream().filter(x -> x.getPrice().equals(orderBook.getPrice())).findFirst().get();
                            if (orderBook.getRemaining() == 0) {
                                bidList.remove(book);
                            } else {
                                int index = bidList.indexOf(book);
                                bidList.get(index).setRemaining(orderBook.getRemaining());
                            }
                            bidList.remove(orderBook);
                        } else if (orderBook.getSide() != null && orderBook.getSide().equals("ask")) {
                            OrderBook askBook = askList.stream().filter(x -> x.getPrice().equals(orderBook.getPrice())).findFirst().get();
                            if (orderBook.getRemaining() == 0) {
                                askList.remove(askBook);
                            } else {
                                int askIndex = askList.indexOf(askBook);
                                askList.get(askIndex).setRemaining(orderBook.getRemaining());
                            }
                        }
                    }
                } else {
                    Trade trade = mapper.convertValue(result, Trade.class);
                    System.out.println("TRADE Object: " + trade);
                }

                ++count;

                Collections.sort(bidList, bidPriceCompare);
                Collections.sort(askList, askPriceCompare);
//            Collections.sort(bidList, bidremainingCompare);
            }

            System.out.println(bidList.get(0).getPrice() + " " + bidList.get(0).getRemaining() + " - " +
                    askList.get(0).getPrice() + " " + askList.get(0).getRemaining());
        }

            public Comparator<LinkedTreeMap<Double, Double>> mapCompare = (o1, o2) -> o2.get("price").compareTo(o1.get("price"));

            public Comparator<OrderBook> bidPriceCompare = (o1, o2) -> o2.getPrice().compareTo(o1.getPrice());
            public Comparator<OrderBook> bidDeltaCompare = (o1, o2) -> o2.getDelta().compareTo(o1.getDelta());
            public Comparator<OrderBook> bidremainingCompare = (o1, o2) -> o2.getDelta().compareTo(o1.getDelta());

            public Comparator<OrderBook> askPriceCompare = Comparator.comparing(OrderBook::getPrice);
            public Comparator<OrderBook> askDeltaCompare = Comparator.comparing(OrderBook::getDelta);
            public Comparator<OrderBook> askRemainingCompare = Comparator.comparing(OrderBook::getRemaining);


//        private void setPriceAndQuantity(Double bestPrice, LinkedTreeMap treeMap, Price priceClass) {
//            Double priceInt = valueOf(treeMap.get("price").toString());
//
//            if (priceInt > bestPrice) {
//                bestPrice = priceInt;
//                priceClass.setPrice(bestPrice);
//                priceClass.setQuantity(valueOf((String) treeMap.get("remaining")));
//            }
//        }

            @Override
            public void afterConnectionEstablished (WebSocketSession session) throws Exception {
                System.out.println("Connected");
                session.setTextMessageSizeLimit(30000000);
            }

            @Override
            public void handleTransportError (WebSocketSession session, Throwable exception){
                System.out.println("Transport Error");
            }

            @Override
            public void afterConnectionClosed (WebSocketSession session, CloseStatus status){
                System.out.println("Connection Closed [" + status.getReason() + "]");
            }
        }

    }
