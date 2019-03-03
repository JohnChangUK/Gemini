package com.johnchang.Queueco.model;

import java.math.BigInteger;
import java.util.Objects;

public class Trade {

    private String trade;
    private BigInteger tid;
    private Double price;
    private Double amount;
    private String makerSide;

    public Trade(String trade, BigInteger tid, Double price, Double amount, String makerSide) {
        this.trade = trade;
        this.tid = tid;
        this.price = price;
        this.amount = amount;
        this.makerSide = makerSide;
    }

    public String getTrade() {
        return trade;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }

    public BigInteger getTid() {
        return tid;
    }

    public void setTid(BigInteger tid) {
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
        Trade trade1 = (Trade) o;
        return Objects.equals(trade, trade1.trade) &&
                Objects.equals(tid, trade1.tid) &&
                Objects.equals(price, trade1.price) &&
                Objects.equals(amount, trade1.amount) &&
                Objects.equals(makerSide, trade1.makerSide);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trade, tid, price, amount, makerSide);
    }

    @Override
    public String toString() {
        return "Trade{" +
                "trade='" + trade + '\'' +
                ", tid=" + tid +
                ", price=" + price +
                ", amount=" + amount +
                ", makerSide='" + makerSide + '\'' +
                '}';
    }
}
