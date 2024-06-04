package com.example.agricola.cards.majorimprovement;


import com.example.agricola.cards.common.BakingCard;
import com.example.agricola.cards.common.CommonCard;
import com.example.agricola.cards.common.ExchangeableCard;
import com.example.agricola.enums.ExchangeTiming;
import com.example.agricola.models.Player;

import java.util.LinkedHashMap;
import java.util.Map;

public class MajorImprovementCard implements CommonCard, ExchangeableCard, BakingCard {
    private int id;
    private String name;
    private String description;
    private LinkedHashMap<String, Integer> exchangeRate;
    private Map<String, Integer> breadBakingExchangeRate;
    private Map<String, Integer> purchaseCost;
    private int additionalPoints;
    private boolean immediateBakingAction;
    private boolean purchased;
    private ExchangeTiming exchangeTiming; // 추가된 필드

    public MajorImprovementCard(int id, String name, String description,
                                Map<String, Integer> purchaseCost,
                                LinkedHashMap<String, Integer> exchangeRate,
                                Map<String, Integer> breadBakingExchangeRate,
                                int additionalPoints,
                                boolean immediateBakingAction,
                                ExchangeTiming exchangeTiming) { // 생성자에 추가된 파라미터
        this.id = id;
        this.name = name;
        this.description = description;
        this.purchaseCost = purchaseCost;
        this.exchangeRate = exchangeRate;
        this.breadBakingExchangeRate = breadBakingExchangeRate;
        this.additionalPoints = additionalPoints;
        this.immediateBakingAction = immediateBakingAction;
        this.purchased = false;
        this.exchangeTiming = exchangeTiming; // 초기화
    }

    @Override
    public void execute(Player player) {
        purchase(player);
    }

//    public boolean purchase(Player player) {
//        Map<String, Integer> cost = player.getDiscountedCost(this.purchaseCost);
//        if (player.checkResources(cost)) {
//            player.payResources(cost);
//            player.addMajorImprovementCard(this);
//            this.purchased = true;
//            if (this.purchased) {
//                triggerBreadBaking(player);
//            }
//            return true;
//        } else {
//            System.out.println("Not enough resources to purchase " + name);
//            return false;
//        }
//    }

    public boolean purchase(Player player) {
        Map<String, Integer> cost = player.getDiscountedCost(this.purchaseCost);
        if (player.checkResources(cost)) {
            player.payResources(cost);
            player.addMajorImprovementCard(this);
            this.purchased = true;
            if (this.immediateBakingAction) {
                triggerBreadBaking(player);
            }
            return true;
        } else {
            System.out.println("Not enough resources to purchase " + name);
            return false;
        }
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
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

    public Map<String, Integer> getPurchaseCost() {
        return purchaseCost;
    }

    public int getAdditionalPoints() {
        return additionalPoints;
    }

    public boolean hasImmediateBakingAction() {
        return immediateBakingAction;
    }

    public ExchangeTiming getExchangeTiming() { // getter 추가
        return exchangeTiming;
    }

    @Override
    public boolean canExchange(ExchangeTiming timing) {
        return(timing == ExchangeTiming.ANYTIME || timing == this.exchangeTiming);
    }

    @Override
    public LinkedHashMap<String, Integer> getExchangeRate() {
        return exchangeRate;
    }

    @Override
    public void triggerBreadBaking(Player player) {
        System.out.println("majorimprovement클래스 triggerbreadbaking");
        if (breadBakingExchangeRate == null) return;

        int availableGrain = player.getResource("grain");
        if (availableGrain > 0) {
            int amount = player.selectGrainForBaking(availableGrain);

            if (amount > 0) {
                int exchangeAmount = breadBakingExchangeRate.get("food") * amount / breadBakingExchangeRate.get("grain");
                player.addResource("grain", -amount);
                player.addResource("food", exchangeAmount);
            }
        }
    }

    public void setPurchaseCost(Map<String, Integer> purchaseCost) {
        this.purchaseCost = purchaseCost;
    }

    @Override
    public boolean hasBreadBakingExchangeRate() {
        return breadBakingExchangeRate != null;
    }
}
