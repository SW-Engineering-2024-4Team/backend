package cards.majorimprovement;

import cards.common.BakingCard;
import cards.common.CommonCard;
import cards.common.ExchangeableCard;
import enums.ExchangeTiming;
import models.Player;

import java.util.Map;
import java.util.Objects;

public class MajorImprovementCard implements CommonCard, ExchangeableCard, BakingCard {
    private int id;
    private String name;
    private String description;
    private Map<String, Integer> exchangeRate;
    private Map<String, Integer> breadBakingExchangeRate;
    private Map<String, Integer> purchaseCost;
    private int additionalPoints;
    private boolean immediateBakingAction;
    private boolean purchased;
    private ExchangeTiming exchangeTiming; // 추가된 필드

    public MajorImprovementCard(int id, String name, String description,
                                Map<String, Integer> purchaseCost,
                                Map<String, Integer> exchangeRate,
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

    public boolean purchase(Player player) {
        Map<String, Integer> cost = player.getDiscountedCost(this.purchaseCost);
        if (player.checkResources(cost)) {
            player.payResources(cost);
            player.addMajorImprovementCard(this);
            this.purchased = true;
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
    public void executeExchange(Player player, String fromResource, String toResource, int amount) {
        if (exchangeRate == null) {
            System.out.println("일반적인 교환 기능을 제공하지 않음. 빵굽기만 가능");
            return;
        }
        int exchangeAmount = exchangeRate.get(toResource) * amount / exchangeRate.get(fromResource);
        player.addResource(fromResource, -amount);
        player.addResource(toResource, exchangeAmount);
    }

    @Override
    public Map<String, Integer> getExchangeRate() {
        return exchangeRate;
    }

    @Override
    public void triggerBreadBaking(Player player) {
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
}
