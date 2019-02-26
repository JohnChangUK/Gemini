package com.johnchang.Queueco.model;

import java.math.BigInteger;
import java.util.Objects;

public class AskPrice {

    private double price;
    private BigInteger quantity;

    public AskPrice(double price, BigInteger quantity) {
        this.price = price;
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public BigInteger getQuantity() {
        return quantity;
    }

    public void setQuantity(BigInteger quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AskPrice)) return false;
        AskPrice askPrice = (AskPrice) o;
        return Double.compare(askPrice.price, price) == 0 &&
                Objects.equals(quantity, askPrice.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, quantity);
    }

    @Override
    public String toString() {
        return "Ask Price{" +
                "price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
