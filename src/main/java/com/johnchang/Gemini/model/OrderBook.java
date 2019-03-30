package com.johnchang.Gemini.model;

import java.util.Objects;

public class OrderBook {

    private String type;
    private String reason;
    private Double price;
    private Double delta;
    private Double remaining;
    private String side;

    public OrderBook() {}

    public OrderBook(String type, String reason, Double price, Double delta, Double remaining, String side) {
        this.type = type;
        this.reason = reason;
        this.price = price;
        this.delta = delta;
        this.remaining = remaining;
        this.side = side;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDelta() {
        return delta;
    }

    public void setDelta(Double delta) {
        this.delta = delta;
    }

    public Double getRemaining() {
        return remaining;
    }

    public void setRemaining(Double remaining) {
        this.remaining = remaining;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderBook orderBook = (OrderBook) o;
        return Objects.equals(type, orderBook.type) &&
                Objects.equals(reason, orderBook.reason) &&
                Objects.equals(price, orderBook.price) &&
                Objects.equals(delta, orderBook.delta) &&
                Objects.equals(remaining, orderBook.remaining) &&
                Objects.equals(side, orderBook.side);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, reason, price, delta, remaining, side);
    }

    @Override
    public String toString() {
        return "OrderBook{" +
                "type='" + type + '\'' +
                ", reason='" + reason + '\'' +
                ", price=" + price +
                ", delta=" + delta +
                ", remaining=" + remaining +
                ", side='" + side + '\'' +
                '}';
    }
}
