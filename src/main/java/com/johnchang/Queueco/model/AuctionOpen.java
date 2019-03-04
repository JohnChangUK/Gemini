package com.johnchang.Queueco.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class AuctionOpen {

    @JsonProperty("auction_open_ms")
    private Double auctionOpenMs;
    @JsonProperty("auction_time_ms")
    private Double auctionTimeMs;
    @JsonProperty("first_indicative_ms")
    private Double firstIndicativeMs;
    @JsonProperty("last_cancel_time_ms")
    private Double lastCancelTimeMs;
    @JsonProperty("type")
    private String type;

    public AuctionOpen() {
    }

    public AuctionOpen(Double auctionOpenMs, Double auctionTimeMs, Double firstIndicativeMs, Double lastCancelTimeMs, String type) {
        this.auctionOpenMs = auctionOpenMs;
        this.auctionTimeMs = auctionTimeMs;
        this.firstIndicativeMs = firstIndicativeMs;
        this.lastCancelTimeMs = lastCancelTimeMs;
        this.type = type;
    }

    public Double getAuctionOpenMs() {
        return auctionOpenMs;
    }

    public void setAuctionOpenMs(Double auctionOpenMs) {
        this.auctionOpenMs = auctionOpenMs;
    }

    public Double getAuctionTimeMs() {
        return auctionTimeMs;
    }

    public void setAuctionTimeMs(Double auctionTimeMs) {
        this.auctionTimeMs = auctionTimeMs;
    }

    public Double getFirstIndicativeMs() {
        return firstIndicativeMs;
    }

    public void setFirstIndicativeMs(Double firstIndicativeMs) {
        this.firstIndicativeMs = firstIndicativeMs;
    }

    public Double getLastCancelTimeMs() {
        return lastCancelTimeMs;
    }

    public void setLastCancelTimeMs(Double lastCancelTimeMs) {
        this.lastCancelTimeMs = lastCancelTimeMs;
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
        AuctionOpen that = (AuctionOpen) o;
        return Objects.equals(auctionOpenMs, that.auctionOpenMs) &&
                Objects.equals(auctionTimeMs, that.auctionTimeMs) &&
                Objects.equals(firstIndicativeMs, that.firstIndicativeMs) &&
                Objects.equals(lastCancelTimeMs, that.lastCancelTimeMs) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(auctionOpenMs, auctionTimeMs, firstIndicativeMs, lastCancelTimeMs, type);
    }

    @Override
    public String toString() {
        return "AuctionOpen{" +
                "auctionOpenMs=" + auctionOpenMs +
                ", auctionTimeMs=" + auctionTimeMs +
                ", firstIndicativeMs=" + firstIndicativeMs +
                ", lastCancelTimeMs=" + lastCancelTimeMs +
                ", type='" + type + '\'' +
                '}';
    }
}
