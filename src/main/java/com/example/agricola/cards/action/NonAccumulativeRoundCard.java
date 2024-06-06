package com.example.agricola.cards.action;

import com.example.agricola.cards.common.ActionRoundCard;
import com.example.agricola.models.Player;


import java.util.Map;

public class NonAccumulativeRoundCard implements ActionRoundCard {
    private int id;
    public String name;
    private String description;
    private boolean revealed;
    private boolean occupied;
    private Map<String, Integer> resourcesToGain;
    private boolean hasResources = false;
    private String occupiedPlayerId = "null";

    public NonAccumulativeRoundCard(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.revealed = false;
        this.occupied = false;
    }

    public void setHasResources() {
        hasResources = true;
    }

    public boolean getHasResources() {
        return hasResources;
    }

    public Map<String, Integer> getResourcesToGain() {
        return resourcesToGain;
    }

    public Map<String, Integer> createResourcesToGain() {
        setHasResources();
        return resourcesToGain;
    }

    @Override
    public void execute(Player player) {
//        // 액션 카드 실행 로직
//        gainResources(player, resourcesToGain);
//        setOccupied(true);
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
    public String getOccupiedPlayerId() {
        return occupiedPlayerId;
    }

    @Override
    public void resetOccupiedPlayer() {
        occupiedPlayerId = "null";
    }

}
