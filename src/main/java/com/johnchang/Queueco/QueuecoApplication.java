package com.johnchang.Queueco;

import java.util.List;
import java.util.Map;

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

    private final Price bestBid;
    private final Price askPrice;

    Double bestBidPrice = 0.00;
    Double bestAskPrice = 0.00;
    Double oldBidPrice = 0.00;
    Double oldAskPrice = 0.00;

    private static String GEMINI_WSS = "wss://api.gemini.com/v1/marketdata/btcusd";

    @Autowired
    public QueuecoApplication(Price bestBid, Price askPrice) {
        this.bestBid = bestBid;
        this.askPrice = askPrice;
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

            for (LinkedTreeMap treeMap : events) {
                if (treeMap.get("side") != null && treeMap.get("side").equals("bid")) {
                    Double priceInt = Double.valueOf(treeMap.get("price").toString());

                    if (priceInt > bestBidPrice && bestBidPrice < 1000000.0) {
                        oldBidPrice = priceInt;
                        bestBidPrice = priceInt;
                        bestBid.setPrice(bestBidPrice);
                        bestBid.setQuantity(Double.valueOf((String) treeMap.get("remaining")));
                    }
                    if (!bestBidPrice.equals(oldBidPrice) || !bestAskPrice.equals(oldAskPrice)) {
                        System.out.println(bestBid + " - " + askPrice);
                    }
                } else if (treeMap.get("side") != null && treeMap.get("side").equals("ask")) {
                    Double priceInt = Double.valueOf(treeMap.get("price").toString());
                    if (priceInt > bestAskPrice && bestAskPrice < 100000.0) {
                        oldAskPrice = priceInt;
                        bestAskPrice = priceInt;
                        askPrice.setPrice(bestAskPrice);
                        askPrice.setQuantity(Double.valueOf((String) treeMap.get("remaining")));
                    }
                    if (!bestBidPrice.equals(oldBidPrice) || !bestAskPrice.equals(oldAskPrice)) {
                        System.out.println(bestBid + " - " + askPrice);
                    }
                }
            }


        }

        private void setPriceAndQuantity(Double bestPrice, LinkedTreeMap treeMap, Price priceClass) {
            Double priceInt = Double.valueOf(treeMap.get("price").toString());

            if (priceInt > bestPrice) {
                bestPrice = priceInt;
                priceClass.setPrice(bestPrice);
                priceClass.setQuantity(Double.valueOf((String) treeMap.get("remaining")));
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
