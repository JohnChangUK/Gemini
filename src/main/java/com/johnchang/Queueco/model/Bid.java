package com.johnchang.Queueco.model;

import java.math.BigInteger;
import java.util.Objects;

public class Bid {

    private double price;
    private BigInteger quantity;

    public Bid(double price, BigInteger quantity) {
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
        if (!(o instanceof Bid)) return false;
        Bid bid = (Bid) o;
        return Double.compare(bid.price, price) == 0 &&
                Objects.equals(quantity, bid.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, quantity);
    }

    @Override
    public String toString() {
        return "Bid{" +
                "price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
