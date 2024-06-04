package com.example.agricola.cards.common;

public class ExchangeRate {
    private String fromResource;
    private String toResource;
    private int rate;

    public ExchangeRate(String fromResource, String toResource, int rate) {
        this.fromResource = fromResource;
        this.toResource = toResource;
        this.rate = rate;
    }

    public String getFromResource() {
        return fromResource;
    }

    public String getToResource() {
        return toResource;
    }

    public int getRate() {
        return rate;
    }
}
