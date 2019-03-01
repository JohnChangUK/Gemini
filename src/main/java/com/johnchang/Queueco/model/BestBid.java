package com.johnchang.Queueco.model;

import java.util.Objects;

public class BestBid implements Price {

    private double price;
    private Double quantity;

    public BestBid() {
    }

    public BestBid(double price, Double quantity) {
        this.price = price;
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BestBid bestBid = (BestBid) o;
        return Double.compare(bestBid.price, price) == 0 &&
                Objects.equals(quantity, bestBid.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, quantity);
    }

    @Override
    public String toString() {
        return price + " " + quantity;
    }
}
