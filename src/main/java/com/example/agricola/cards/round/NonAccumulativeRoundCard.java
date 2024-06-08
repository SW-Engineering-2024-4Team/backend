package com.example.agricola.cards.round;

import com.example.agricola.cards.common.ActionRoundCard;
import com.example.agricola.models.Player;

import java.util.Map;

public class NonAccumulativeRoundCard implements ActionRoundCard {
    private int id;
    private String name;
    private String description;
    private int cycle;
    private boolean revealed;
    private boolean occupied;
    private Map<String, Integer> resourcesToGain;
    private String occupiedPlayerId = "null";
    private boolean hasResources = false;

    public NonAccumulativeRoundCard(int id, String name, String description, int cycle) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cycle = cycle;
        this.revealed = false;
        this.occupied = false;
    }
    public void setHasResources() {
        hasResources = true;
    }

    public boolean getHasResources() {
        return hasResources;
    }

    public Map<String, Integer> createResourcesToGain() {
        setHasResources();
        return resourcesToGain;
    }


    @Override
    public void execute(Player player) {
        // 라운드 카드 실행 로직
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public int getCycle() {
        return cycle;
    }

    @Override
    public boolean isRevealed() {
        return revealed;
    }

    @Override
    public void reveal() {
        revealed = true;
    }

    @Override
    public boolean isAccumulative() {
        return false; // 자원 누적 불가능
    }

    @Override
    public boolean isOccupied() {
        return occupied;
    }

    @Override
    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    @Override
    public void setOccupiedPlayerId(String id) {
        occupiedPlayerId = id;
    }

    @Override
    public String getOccupiedPlayerId() {
        return this.occupiedPlayerId;
    }
    //각카드에 구현필요
    @Override
    public void resetOccupiedPlayer() {
        this.occupiedPlayerId = "null";
    }
}
