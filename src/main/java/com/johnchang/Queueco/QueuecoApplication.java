package com.johnchang.Queueco;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class QueuecoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(QueuecoApplication.class, args);
	}

	@Override
	public void run(String... args) {
		WebSocketConnectionManager connectionManager = new WebSocketConnectionManager(
				new StandardWebSocketClient(), new GDAXWebSocketHandler(), "wss://api.gemini.com/v1/marketdata/btcusd");
		connectionManager.start();
	}

	private class GDAXWebSocketHandler extends TextWebSocketHandler {

		@Override
		public void handleTextMessage(WebSocketSession session, TextMessage message) {
//			System.out.println("Message Received [" + message.getPayload() + "]");
			Map value = new Gson().fromJson(message.getPayload(), Map.class);
			List<LinkedTreeMap> events = (List<LinkedTreeMap>) value.get("events");
			Double bestAskPrice = 0.00;
			for (LinkedTreeMap treeMap : events) {
				String price = (String) treeMap.get("price");
				Double priceInt = Double.valueOf(price);
				if (priceInt > bestAskPrice) {
					bestAskPrice = priceInt;
				}
			}
			System.out.println(bestAskPrice);
			//				session.sendMessage(new TextMessage("Hello " + value.get("events") + " !"));
//				System.out.println(events.stream().map(x -> x.get("price")).collect(Collectors.toList()));
//				value.get("events").stream().forEach(x -> System.out.println(x))
			System.out.println(message.getPayload());
		}

		public void getHighest(String a, String b) {

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
		public void afterConnectionClosed(WebSocketSession session, CloseStatus status){
			System.out.println("Connection Closed [" + status.getReason() + "]");
		}
	}


}
