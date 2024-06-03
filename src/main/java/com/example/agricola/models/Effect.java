package com.example.agricola.models;

public class Effect {
    private String description;
    private int value;

    public Effect(String description, int value) {
        this.description = description;
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public int getValue() {
        return value;
    }

    public int getAmount() {
        return value;
    }
}

