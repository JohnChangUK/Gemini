package com.johnchang.Gemini.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Trade {

    @JsonProperty("type")
    private String type;
    @JsonProperty("tid")
    private Double tid;
    @JsonProperty("price")
    private Double price;
    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("makerSide")
    private String makerSide;

    public Trade() {
    }

    public Trade(String type, Double tid, Double price, Double amount, String makerSide) {
        this.type = type;
        this.tid = tid;
        this.price = price;
        this.amount = amount;
        this.makerSide = makerSide;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getTid() {
        return tid;
    }

    public void setTid(Double tid) {
        this.tid = tid;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getMakerSide() {
        return makerSide;
    }

    public void setMakerSide(String makerSide) {
        this.makerSide = makerSide;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trade trade = (Trade) o;
        return Objects.equals(type, trade.type) &&
                Objects.equals(tid, trade.tid) &&
                Objects.equals(price, trade.price) &&
                Objects.equals(amount, trade.amount) &&
                Objects.equals(makerSide, trade.makerSide);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, tid, price, amount, makerSide);
    }

    @Override
    public String toString() {
        return "Trade{" +
                "type='" + type + '\'' +
                ", tid=" + tid +
                ", price=" + price +
                ", amount=" + amount +
                ", makerSide='" + makerSide + '\'' +
                '}';
    }
}
