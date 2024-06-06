package com.example.agricola.message;

import java.util.List;

public class GameActionBoardMessage {
    private String message;
    private int[] clickedActionCards;
    private int[] resourceActionCards;

    public GameActionBoardMessage(String message, int[] clickedActionCards, int[] resourceActionCards) {
        this.message = message;
        this.clickedActionCards = clickedActionCards;
        this.resourceActionCards = resourceActionCards;
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int[] getClickedActionCards() {
        return clickedActionCards;
    }

    public void setClickedActionCards(int[] clickedActionCards) {
        this.clickedActionCards = clickedActionCards;
    }

    public int[] getResourceActionCards() {
        return resourceActionCards;
    }

    public void setResourceActionCards(int[] resourceActionCards) {
        this.resourceActionCards = resourceActionCards;
    }
}

