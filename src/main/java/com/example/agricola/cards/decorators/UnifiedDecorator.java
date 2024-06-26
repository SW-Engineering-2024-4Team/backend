package com.example.agricola.cards.decorators;


import com.example.agricola.cards.common.AccumulativeCard;
import com.example.agricola.models.Player;

import java.util.Map;

public abstract class UnifiedDecorator implements AccumulativeCard {
    protected AccumulativeCard decoratedCard;
    protected boolean occupied;
    protected Player appliedPlayer;


    public UnifiedDecorator(AccumulativeCard decoratedCard, Player appliedPlayer) {
        if (decoratedCard == null) {
            throw new IllegalArgumentException("Decorated card cannot be null");
        }
        this.decoratedCard = decoratedCard;
        this.occupied = decoratedCard.isOccupied();
        this.appliedPlayer = appliedPlayer;
    }

    @Override
    public Map<String , Integer> getAccumulatedAmounts() {
        return decoratedCard.getAccumulatedAmounts();
    }

    @Override
    public void execute(Player player) {
    }

    @Override
    public boolean isOccupied() {
        return this.occupied;
    }

    @Override
    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
        decoratedCard.setOccupied(occupied);
    }

    @Override
    public int getId() {
        return decoratedCard.getId();
    }

    @Override
    public String getName() {
        return decoratedCard.getName();
    }

    @Override
    public String getDescription() {
        return decoratedCard.getDescription();
    }

    @Override
    public boolean isRevealed() {
        return decoratedCard.isRevealed();
    }

    @Override
    public void reveal() {
        decoratedCard.reveal();
    }

    @Override
    public boolean isAccumulative() {
        return decoratedCard.isAccumulative();
    }

    @Override
    public Map<String, Integer> getAccumulatedResources() {
        return decoratedCard.getAccumulatedResources();
    }

    @Override
    public void clearAccumulatedResources() {
        decoratedCard.clearAccumulatedResources();
    }

    @Override
    public void accumulateResources() {
        decoratedCard.accumulateResources();
    }

    @Override
    public void gainResources(Player player, Map<String, Integer> resources) {
//        if (player.equals(appliedPlayer))
//            decoratedCard.gainResources(player, resources);
        decoratedCard.gainResources(player, resources);
    }

    @Override
    public void useOccupationCard(Player player) {
        decoratedCard.useOccupationCard(player);
    }

    @Override
    public void useMinorImprovementCard(Player player) {
        decoratedCard.useMinorImprovementCard(player);
    }

    @Override
    public void purchaseMajorImprovementCard(Player player) {
        decoratedCard.purchaseMajorImprovementCard(player);
    }

    @Override
    public void triggerBreadBaking(Player player) {
        decoratedCard.triggerBreadBaking(player);
    }

    @Override
    public void buildHouse(Player player) {
        decoratedCard.buildHouse(player);
    }

    @Override
    public void plowField(Player player) {
        decoratedCard.plowField(player);
    }
    @Override
    public void buildBarn(Player player) {
        decoratedCard.buildBarn(player);
    }

    @Override
    public void buildFence(Player player) {
        decoratedCard.buildFence(player);
    }

    @Override
    public void plantField(Player player) {
        decoratedCard.plantField(player);
    }


    @Override
    public void becomeFirstPlayer(Player player) {
        decoratedCard.becomeFirstPlayer(player);
    }

    @Override
    public boolean addNewborn(Player player) {
        return decoratedCard.addNewborn(player);
    }

    @Override
    public void renovateRooms(Player player) {
        decoratedCard.renovateRooms(player);
    }

    @Override
    public boolean checkResources(Player player, Map<String, Integer> resources) {
        return decoratedCard.checkResources(player, resources);
    }

    @Override
    public void payResources(Player player, Map<String, Integer> resources) {
        decoratedCard.payResources(player, resources);
    }

    @Override
    public void executeThen(Player player, Runnable action1, Runnable action2) {
        decoratedCard.executeThen(player, action1, action2);
    }

    @Override
    public void applyAdditionalEffects(Player player) { }

//    public Map<String, Integer> getResourcesToGain() {
//        if (decoratedCard instanceof Seed) {
//            return ((Seed) decoratedCard).createResourcesToGain();
//        }
//        return null;
//    }
}
