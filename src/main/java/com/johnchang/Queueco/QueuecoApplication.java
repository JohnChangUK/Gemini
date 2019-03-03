package com.johnchang.Queueco;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.lang.Double.valueOf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.johnchang.Queueco.model.OrderBook;
import com.johnchang.Queueco.model.Price;
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
    List<OrderBook> orderBookList;
    List<LinkedTreeMap> bestBidList;
    List<LinkedTreeMap> askPriceList;
    Map<Double, Double> bestBidMap;
    Map<Double, Double> askPriceMap;
    final ObjectMapper mapper;
    int count = 0;

    private static String GEMINI_WSS = "wss://api.gemini.com/v1/marketdata/btcusd";

    @Autowired
    public QueuecoApplication() {
        orderBook = new OrderBook();
        orderBookList = new ArrayList<>();
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
            Map value2 = new Gson().fromJson(message.getPayload(), Map.class);
            List<LinkedTreeMap> orderbookEvents = (List<LinkedTreeMap>) value2.get("events");
            for (LinkedTreeMap treeMap : orderbookEvents) {
                if (treeMap.get("reason").equals("initial")) {
                    if (treeMap.get("side") != null && treeMap.get("side").equals("bid")) {
                        bestBidList.add(treeMap);
                    } else if (treeMap.get("side") != null && treeMap.get("side").equals("ask") && treeMap.get("type").equals("change")) {
                        askPriceList.add(treeMap);
                    }
                }
            }
            ++count;
            if (count == 1) {
                Collections.reverse(bestBidList);
            }
            System.out.println(bestBidList.get(0).get("price") + " " + bestBidList.get(0).get("remaining") + " - " +
                    askPriceList.get(0).get("price") + " " + askPriceList.get(0).get("remaining"));

            for (LinkedTreeMap treeMap : orderbookEvents) {
                if (treeMap.get("reason").equals("cancel")) {
                    if (treeMap.get("side") != null && treeMap.get("side").equals("bid")) {
                        bestBidList.remove(treeMap);
                    } else if (treeMap.get("side") != null && treeMap.get("side").equals("ask")) {
                        askPriceList.remove(treeMap);
                    }
                }
                if (treeMap.get("reason").equals("place")) {
                    if (treeMap.get("side") != null && treeMap.get("side").equals("bid")) {
                        bestBidList.add(treeMap);
                    } else if (treeMap.get("side") != null && treeMap.get("side").equals("ask")) {
                        askPriceList.add(treeMap);
                    }
                }
                if (treeMap.get("reason").equals("trade")) {
                    if (treeMap.get("side") != null && treeMap.get("side").equals("bid")) {
                        bestBidList.add(treeMap);
                    } else if (treeMap.get("side") != null && treeMap.get("side").equals("ask")) {
                        askPriceList.add(treeMap);
                    }
                }
            }
        }

        private void setPriceAndQuantity(Double bestPrice, LinkedTreeMap treeMap, Price priceClass) {
            Double priceInt = valueOf(treeMap.get("price").toString());

            if (priceInt > bestPrice) {
                bestPrice = priceInt;
                priceClass.setPrice(bestPrice);
                priceClass.setQuantity(valueOf((String) treeMap.get("remaining")));
            }
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
