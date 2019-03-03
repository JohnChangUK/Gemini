package com.johnchang.Queueco;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.johnchang.Queueco.model.OrderBook;
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

    private List<OrderBook> bidList;
    private List<OrderBook> askList;
    private Map<Double, Double> cache;
    private final ObjectMapper mapper;
    private DecimalFormat df;
    private DecimalFormat dfSatoshi;

    private static String GEMINI_WSS = "wss://api.gemini.com/v1/marketdata/btcusd";

    @Autowired
    public QueuecoApplication() {
        bidList = new ArrayList<>();
        askList = new ArrayList<>();
        mapper = new ObjectMapper();
        cache = new HashMap<>();
        df = new DecimalFormat("#0.00");
        dfSatoshi = new DecimalFormat("#0.000000000");
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
            LinkedTreeMap tradeResult = new LinkedTreeMap();

            for (LinkedTreeMap<Object, Object> map : events) {
                for (Map.Entry<Object, Object> entry : map.entrySet()) {
                    if (map.size() == 6) {
                        result.put(entry.getKey(), entry.getValue());
                    } else if (map.size() == 5) {
                        tradeResult.put(entry.getKey(), entry.getValue());
                    }
                }

                // Trade event occurs
                if (events.get(0).size() == 5 && events.get(0).get("makerSide").equals("bid")) {
                    Trade bidTrade = mapper.convertValue(tradeResult, Trade.class);
                    OrderBook bidBookTrade = bidList.stream().filter(x -> x.getPrice().equals(bidTrade.getPrice())).findFirst().orElse(null);

                    if (bidList.indexOf(bidBookTrade) != -1) {
                        int index = bidList.indexOf(bidBookTrade);
                        bidList.get(index).setRemaining(bidList.get(index).getRemaining() - bidTrade.getAmount());

                        if (bidList.get(index).getRemaining() <= 0) {
                            bidList.remove(bidBookTrade);
                        }
                    } else {
                        System.out.println("Orderbook doesn't exist for BidBookTrade: " + bidBookTrade);
                    }
                }

                if (events.get(0).size() == 5 && events.get(0).get("makerSide").equals("ask")) {
                    Trade askTrade = mapper.convertValue(tradeResult, Trade.class);
                    OrderBook askBookTrade = askList.stream().filter(x -> x.getPrice().equals(askTrade.getPrice())).findFirst().orElse(null);

                    if (askList.indexOf(askBookTrade) != -1) {
                        int index = askList.indexOf(askBookTrade);
                        askList.get(index).setRemaining(askList.get(index).getRemaining() - askTrade.getAmount());

                        if (askList.get(index).getRemaining() <= 0) {
                            askList.remove(askBookTrade);
                        }
                    } else {
                        System.out.println("Orderbook doesnt exist for AskBookTrade: " + askBookTrade);
                    }
                }

                if (events.get(0).size() != 5) {
                    OrderBook orderBook = mapper.convertValue(result, OrderBook.class);

                    if (orderBook != null) {
                        if (orderBook.getReason().equals("initial")) {
                            if (orderBook.getSide() != null && orderBook.getSide().equals("bid")) {
                                bidList.add(orderBook);
                            } else if (orderBook.getSide() != null && orderBook.getSide().equals("ask")) {
                                askList.add(orderBook);
                            }
                        }

                        if (orderBook.getReason().equals("cancel")) {
                            OrderBook bidBookCancel = bidList.stream().filter(x -> x.getPrice().equals(orderBook.getPrice())).findFirst().orElse(orderBook);

                            if (orderBook.getSide() != null && orderBook.getSide().equals("bid")) {
                                if (bidList.indexOf(bidBookCancel) != -1) {
                                    int index = bidList.indexOf(bidBookCancel);

                                    if (orderBook.getRemaining() <= 0) {
                                        bidList.remove(bidBookCancel);
                                    } else {
                                        bidList.get(index).setRemaining(orderBook.getRemaining());
                                    }
                                } else {
                                    System.out.println("Orderbook doesnt exist for BidBookCancel: " + bidBookCancel);
                                }
                            }

                            if (orderBook.getSide() != null && orderBook.getSide().equals("ask")) {
                                OrderBook askBookCancel = askList.stream().filter(x -> x.getPrice().equals(orderBook.getPrice())).findFirst().orElse(orderBook);
                                if (askList.indexOf(askBookCancel) != -1) {
                                    int askIndex = askList.indexOf(askBookCancel);

                                    if (orderBook.getRemaining() <= 0) {
                                        askList.remove(askBookCancel);
                                    } else {
                                        askList.get(askIndex).setRemaining(orderBook.getRemaining());
                                    }
                                } else {
                                    System.out.println("Orderbook doesnt exist for AskBookCancel: " + askBookCancel);
                                }
                            }
                        }

                        if (orderBook.getReason().equals("place")) {
                            Optional<OrderBook> bookPlace = bidList.stream().filter(x -> x.getPrice().equals(orderBook.getPrice())).findFirst();
                            Optional<OrderBook> askBookPlace = askList.stream().filter(x -> x.getPrice().equals(orderBook.getPrice())).findFirst();

                            if (orderBook.getSide() != null && orderBook.getSide().equals("bid")) {
                                if (bookPlace.isPresent()) {
                                    orderBook.setRemaining(orderBook.getRemaining() + bookPlace.get().getRemaining());
                                } else {
                                    bidList.add(orderBook);
                                }
                            } else if (orderBook.getSide() != null && orderBook.getSide().equals("ask")) {
                                if (askBookPlace.isPresent()) {
                                    orderBook.setRemaining(orderBook.getRemaining() + askBookPlace.get().getRemaining());
                                } else {
                                    askList.add(orderBook);
                                }
                            }
                        }
                    } else {
                        System.out.println("order book is null: " + orderBook);
                    }
                }

                bidList.sort(bidPriceCompare);
                askList.sort(askPriceCompare);
            }

            populateCache();

            if (!cache.containsKey(bidList.get(0).getPrice()) || !cache.containsValue(bidList.get(0).getRemaining()) ||
                    !cache.containsKey(askList.get(0).getPrice()) || !cache.containsValue(askList.get(0).getRemaining())) {

                cache.put(bidList.get(0).getPrice(), bidList.get(0).getRemaining());
                cache.put(askList.get(0).getPrice(), askList.get(0).getRemaining());

                printBestBidAskPrice();
            }
        }

        private void populateCache() {
            if (cache.isEmpty()) {
                printBestBidAskPrice();

                cache.put(bidList.get(0).getPrice(), bidList.get(0).getRemaining());
                cache.put(askList.get(0).getPrice(), askList.get(0).getRemaining());
            }
        }

        Comparator<OrderBook> bidPriceCompare = (o1, o2) -> o2.getPrice().compareTo(o1.getPrice());

        Comparator<OrderBook> askPriceCompare = Comparator.comparing(OrderBook::getPrice);

        private void printBestBidAskPrice() {
            System.out.println(df.format(bidList.get(0).getPrice()) + " " + dfSatoshi.format(bidList.get(0).getRemaining()) + " - " +
                    df.format(askList.get(0).getPrice()) + " " + dfSatoshi.format(askList.get(0).getRemaining()));
        }

        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            System.out.println("Connected");
            session.setTextMessageSizeLimit(30000000);
        }

        @Override
        public void handleTransportError(WebSocketSession session, Throwable exception) {
            System.out.println("Transport Error");
        }

        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
            System.out.println("Connection Closed [" + status.getReason() + "]");
        }
    }
}
