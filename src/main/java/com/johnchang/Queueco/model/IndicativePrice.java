package com.johnchang.Queueco.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class IndicativePrice {

    @JsonProperty("type")
    private String type;
    @JsonProperty("eid")
    private Double eid;
    @JsonProperty("result")
    private String result;
    @JsonProperty("time_ms")
    private Double timeMs;
    @JsonProperty("highest_bid_price")
    private Double highestBidPrice;
    @JsonProperty("lowest_ask_price")
    private Double lowestAskPrice;
    @JsonProperty("collar_price")
    private Double collarPrice;
    @JsonProperty("indicative_price")
    private Double indicativePrice;
    @JsonProperty("indicative_quantity")
    private Double indicativeQuantity;

    public IndicativePrice() {
    }

    public IndicativePrice(String type, Double eid, String result, Double timeMs, Double highestBidPrice,
                           Double lowestAskPrice, Double collarPrice, Double indicativePrice, Double indicativeQuantity) {
        this.type = type;
        this.eid = eid;
        this.result = result;
        this.timeMs = timeMs;
        this.highestBidPrice = highestBidPrice;
        this.lowestAskPrice = lowestAskPrice;
        this.collarPrice = collarPrice;
        this.indicativePrice = indicativePrice;
        this.indicativeQuantity = indicativeQuantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getEid() {
        return eid;
    }

    public void setEid(Double eid) {
        this.eid = eid;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Double getTimeMs() {
        return timeMs;
    }

    public void setTimeMs(Double timeMs) {
        this.timeMs = timeMs;
    }

    public Double getHighestBidPrice() {
        return highestBidPrice;
    }

    public void setHighestBidPrice(Double highestBidPrice) {
        this.highestBidPrice = highestBidPrice;
    }

    public Double getLowestAskPrice() {
        return lowestAskPrice;
    }

    public void setLowestAskPrice(Double lowestAskPrice) {
        this.lowestAskPrice = lowestAskPrice;
    }

    public Double getCollarPrice() {
        return collarPrice;
    }

    public void setCollarPrice(Double collarPrice) {
        this.collarPrice = collarPrice;
    }

    public Double getIndicativePrice() {
        return indicativePrice;
    }

    public void setIndicativePrice(Double indicativePrice) {
        this.indicativePrice = indicativePrice;
    }

    public Double getIndicativeQuantity() {
        return indicativeQuantity;
    }

    public void setIndicativeQuantity(Double indicativeQuantity) {
        this.indicativeQuantity = indicativeQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndicativePrice that = (IndicativePrice) o;
        return Objects.equals(type, that.type) &&
                Objects.equals(eid, that.eid) &&
                Objects.equals(result, that.result) &&
                Objects.equals(timeMs, that.timeMs) &&
                Objects.equals(highestBidPrice, that.highestBidPrice) &&
                Objects.equals(lowestAskPrice, that.lowestAskPrice) &&
                Objects.equals(collarPrice, that.collarPrice) &&
                Objects.equals(indicativePrice, that.indicativePrice) &&
                Objects.equals(indicativeQuantity, that.indicativeQuantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, eid, result, timeMs, highestBidPrice, lowestAskPrice, collarPrice, indicativePrice, indicativeQuantity);
    }

    @Override
    public String toString() {
        return "IndicativePrice{" +
                "type='" + type + '\'' +
                ", eid=" + eid +
                ", result='" + result + '\'' +
                ", timeMs=" + timeMs +
                ", highestBidPrice=" + highestBidPrice +
                ", lowestAskPrice=" + lowestAskPrice +
                ", collarPrice=" + collarPrice +
                ", indicativePrice=" + indicativePrice +
                ", indicativeQuantity=" + indicativeQuantity +
                '}';
    }
}
