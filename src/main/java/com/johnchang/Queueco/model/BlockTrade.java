package com.johnchang.Queueco.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class BlockTrade {

    @JsonProperty("type")
    private String type;
    @JsonProperty("tid")
    private Double tid;
    @JsonProperty("price")
    private Double price;
    @JsonProperty("amount")
    private Double amount;

    public BlockTrade() {
    }

    public BlockTrade(String type, Double tid, Double price, Double amount) {
        this.type = type;
        this.tid = tid;
        this.price = price;
        this.amount = amount;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockTrade that = (BlockTrade) o;
        return Objects.equals(type, that.type) &&
                Objects.equals(tid, that.tid) &&
                Objects.equals(price, that.price) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, tid, price, amount);
    }

    @Override
    public String toString() {
        return "BlockTrade{" +
                "type='" + type + '\'' +
                ", tid=" + tid +
                ", price=" + price +
                ", amount=" + amount +
                '}';
    }
}

