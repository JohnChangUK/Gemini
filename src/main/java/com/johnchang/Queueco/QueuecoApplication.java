package com.johnchang.Queueco;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.johnchang.Queueco.model.AskPrice;
import com.johnchang.Queueco.model.BestBid;
import com.johnchang.Queueco.model.Price;
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

    private static String GEMINI_WSS = "wss://api.gemini.com/v1/marketdata/btcusd";
    Price bestBid;
    private Price askPrice;

    public QueuecoApplication() {
        bestBid = new BestBid();
        askPrice = new AskPrice();
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
            Double bestBidPrice = 0.0;
            Double bestAskPrice = 0.0;

            Map value = new Gson().fromJson(message.getPayload(), Map.class);
            List<LinkedTreeMap> events = (List<LinkedTreeMap>) value.get("events");

            for (LinkedTreeMap treeMap : events) {
                if (treeMap.get("side") != null && treeMap.get("side").equals("bid")) {
                    bestBidPrice = setPriceAndQuantity(bestBidPrice, treeMap, bestBid);
                } else if (treeMap.get("side") != null && treeMap.get("side").equals("ask")) {
                    bestAskPrice = setPriceAndQuantity(bestAskPrice, treeMap, askPrice);
                }
            }

            System.out.println(bestBid + " - " + askPrice);
        }

        private Double setPriceAndQuantity(Double bestPrice, LinkedTreeMap treeMap, Price priceClass) {
            String price = (String) treeMap.get("price");
            Double priceInt = Double.valueOf(price);

            if (priceInt > bestPrice) {
                bestPrice = priceInt;
                priceClass.setPrice(bestPrice);
                priceClass.setQuantity(Double.valueOf((String) treeMap.get("remaining")));
            }

            return bestPrice;
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
