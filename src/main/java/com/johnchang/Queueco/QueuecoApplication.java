package com.johnchang.Queueco;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.lang.Double.valueOf;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
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

    Map<Double, Double> bestBidMap;
    Map<Double, Double> askPriceMap;
    int count = 0;

    private static String GEMINI_WSS = "wss://api.gemini.com/v1/marketdata/btcusd";

    @Autowired
    public QueuecoApplication() {
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

            if (count >= 1) {
                Map<Double, Double> bestBidMapCopy = bestBidMap;
                Map<Double, Double> askPriceMapCopy = askPriceMap;
                Map<Double, Double> bestBidMapCopy2 = new TreeMap<>(Collections.reverseOrder());
                Map<Double, Double> askPriceMapCopy2 = new TreeMap<>();
                Map.Entry<Double, Double> bestBidCopy;
                Map.Entry<Double, Double> askPriceCopy;

                Map value = new Gson().fromJson(message.getPayload(), Map.class);
                List<LinkedTreeMap> events = (List<LinkedTreeMap>) value.get("events");

                for (LinkedTreeMap treeMap : events) {
                    if (treeMap.get("side") != null && treeMap.get("side").equals("bid") && treeMap.get("type").equals("change")) {
                        bestBidMapCopy2.put(valueOf(treeMap.get("price").toString()), valueOf(treeMap.get("remaining").toString()));
                    } else if (treeMap.get("side") != null && treeMap.get("side").equals("ask") && treeMap.get("type").equals("change")) {
                        askPriceMapCopy2.put(valueOf(treeMap.get("price").toString()), valueOf(treeMap.get("remaining").toString()));
                    }
                }
                if (bestBidMapCopy2.size() != 0) {
                    if (!bestBidMapCopy.equals(bestBidMapCopy2)) {
                        bestBidCopy = bestBidMapCopy2.entrySet().iterator().next();
                        System.out.println(bestBidCopy.getKey() + " " + bestBidCopy.getValue() + " - ");
                    }
                }
                if (askPriceMapCopy2.size() != 0) {
                    if (!askPriceMapCopy.equals(askPriceMapCopy2)) {
                        askPriceCopy = askPriceMapCopy.entrySet().iterator().next();
                        System.out.println(" - " + askPriceCopy.getKey() + " " + askPriceCopy.getValue());
                    }
                }
            }

            Map value = new Gson().fromJson(message.getPayload(), Map.class);
            List<LinkedTreeMap> events = (List<LinkedTreeMap>) value.get("events");

            for (LinkedTreeMap treeMap : events) {
                if (treeMap.get("side") != null && treeMap.get("side").equals("bid")) {
                    bestBidMap.put(valueOf(treeMap.get("price").toString()), valueOf(treeMap.get("remaining").toString()));
                } else if (treeMap.get("side") != null && treeMap.get("side").equals("ask")) {
                    askPriceMap.put(valueOf(treeMap.get("price").toString()), valueOf(treeMap.get("remaining").toString()));
                }
            }
//            TreeMap<Double, Double> bestBid = (TreeMap) bestBidMap.getFirstEntry();
            Map.Entry<Double, Double> bestBid = bestBidMap.entrySet().iterator().next();
            Map.Entry<Double, Double> askPrice = askPriceMap.entrySet().iterator().next();
            if (count >= 1) {
                Map<Double, Double> bestBidMapCopy = bestBidMap;
                Map<Double, Double> askPriceMapCopy = askPriceMap;
                Map<Double, Double> bestBidMapCopy2 = new TreeMap<>(Collections.reverseOrder());
                Map<Double, Double> askPriceMapCopy2 = new TreeMap<>();
            }
            if (count == 0) {
                count++;
                System.out.println(bestBid.getKey() + " " + bestBid.getValue() + " - " + askPrice.getKey() + " " + askPrice.getValue());
            }
//            System.out.println(bestBid.getKey() + " " + bestBid.getValue() + " - " + askPrice.getKey() + " " + askPrice.getValue());
//            DecimalFormat df = new DecimalFormat("#");
////            df.setMaximumFractionDigits(2);
////            System.out.print(df.format(bestList.get(0)));
////            System.out.println(df.format(askList.get(0)));

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
