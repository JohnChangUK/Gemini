package com.johnchang.Gemini.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class TradeAuction {

    @JsonProperty("auction_price")
    private Double auctionPrice;
    @JsonProperty("auction_quantity")
    private Double auctionQuantity;
    @JsonProperty("eid")
    private Double eid;
    @JsonProperty("highest_bid_price")
    private Double highestBidPrice;
    @JsonProperty("lowest_ask_price")
    private Double lowestAskPrice;
    @JsonProperty("result")
    private String result;
    @JsonProperty("time_ms")
    private Double timeMs;
    @JsonProperty("type")
    private String type;

    public TradeAuction() {
    }

    public TradeAuction(Double auctionPrice, Double auctionQuantity, Double eid, Double highestBidPrice,
                        Double lowestAskPrice, String result, Double timeMs, String type) {
        this.auctionPrice = auctionPrice;
        this.auctionQuantity = auctionQuantity;
        this.eid = eid;
        this.highestBidPrice = highestBidPrice;
        this.lowestAskPrice = lowestAskPrice;
        this.result = result;
        this.timeMs = timeMs;
        this.type = type;
    }

    public Double getAuctionPrice() {
        return auctionPrice;
    }

    public void setAuctionPrice(Double auctionPrice) {
        this.auctionPrice = auctionPrice;
    }

    public Double getAuctionQuantity() {
        return auctionQuantity;
    }

    public void setAuctionQuantity(Double auctionQuantity) {
        this.auctionQuantity = auctionQuantity;
    }

    public Double getEid() {
        return eid;
    }

    public void setEid(Double eid) {
        this.eid = eid;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TradeAuction that = (TradeAuction) o;
        return Objects.equals(auctionPrice, that.auctionPrice) &&
                Objects.equals(auctionQuantity, that.auctionQuantity) &&
                Objects.equals(eid, that.eid) &&
                Objects.equals(highestBidPrice, that.highestBidPrice) &&
                Objects.equals(lowestAskPrice, that.lowestAskPrice) &&
                Objects.equals(result, that.result) &&
                Objects.equals(timeMs, that.timeMs) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(auctionPrice, auctionQuantity, eid, highestBidPrice, lowestAskPrice, result, timeMs, type);
    }

    @Override
    public String toString() {
        return "TradeAuction{" +
                "auctionPrice=" + auctionPrice +
                ", auctionQuantity=" + auctionQuantity +
                ", eid=" + eid +
                ", highestBidPrice=" + highestBidPrice +
                ", lowestAskPrice=" + lowestAskPrice +
                ", result='" + result + '\'' +
                ", timeMs=" + timeMs +
                ", type='" + type + '\'' +
                '}';
    }
}
