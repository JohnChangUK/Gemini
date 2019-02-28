package com.johnchang.Queueco.model;

public class GeminiPayload {

    private String type;
    private Integer socketSequence;

    public GeminiPayload(String type, Integer socketSequence) {
        this.type = type;
        this.socketSequence = socketSequence;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSocketSequence() {
        return socketSequence;
    }

    public void setSocketSequence(Integer socketSequence) {
        this.socketSequence = socketSequence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeminiPayload that = (GeminiPayload) o;

        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        return socketSequence != null ? socketSequence.equals(that.socketSequence) : that.socketSequence == null;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (socketSequence != null ? socketSequence.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GeminiPayload{" +
                "type='" + type + '\'' +
                ", socketSequence=" + socketSequence +
                '}';
    }
}
