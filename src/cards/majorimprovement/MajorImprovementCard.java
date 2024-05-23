package cards.majorimprovement;

import cards.common.BakingCard;
import cards.common.CommonCard;
import cards.common.ExchangeableCard;
import enums.ExchangeTiming;
import models.Player;

import java.util.Map;

public class MajorImprovementCard implements CommonCard, ExchangeableCard, BakingCard {
    private int id;
    private String name;
    private String description;
    private Map<String, Integer> anytimeExchangeRate;
    private Map<String, Integer> harvestExchangeRate;
    private Map<String, Integer> breadBakingExchangeRate;
    private Map<String, Integer> purchaseCost;
    private int additionalPoints;
    private boolean immediateBakingAction;

    public MajorImprovementCard(int id, String name, String description,
                                Map<String, Integer> purchaseCost,
                                Map<String, Integer> anytimeExchangeRate,
                                Map<String, Integer> breadBakingExchangeRate,
                                int additionalPoints,
                                boolean immediateBakingAction) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.purchaseCost = purchaseCost;
        this.anytimeExchangeRate = anytimeExchangeRate;
        this.breadBakingExchangeRate = breadBakingExchangeRate;
        this.additionalPoints = additionalPoints;
        this.immediateBakingAction = immediateBakingAction;
    }

    @Override
    public void execute(Player player) {
        // 기본 실행 로직
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

    @Override
    public boolean canExchange(ExchangeTiming timing) {
        return anytimeExchangeRate != null && (timing == ExchangeTiming.ANYTIME || timing == ExchangeTiming.HARVEST);
    }

    @Override
    public void executeExchange(Player player, String fromResource, String toResource, int amount) {
        if (anytimeExchangeRate == null) return;
        int exchangeAmount = anytimeExchangeRate.get(toResource) * amount / anytimeExchangeRate.get(fromResource);
        player.addResource(fromResource, -amount);
        player.addResource(toResource, exchangeAmount);
    }

    @Override
    public Map<String, Integer> getExchangeRate() {
        return anytimeExchangeRate;
    }

    @Override
    public void triggerBreadBaking(Player player) {
        if (breadBakingExchangeRate == null) return;

        int availableGrain = player.getResource("grain");
        if (availableGrain > 0) {
            // 플레이어가 교환할 곡식의 양을 선택
            int amount = player.selectGrainForBaking(availableGrain);

            if (amount > 0) {
                int exchangeAmount = breadBakingExchangeRate.get("food") * amount / breadBakingExchangeRate.get("grain");
                player.addResource("grain", -amount);
                player.addResource("food", exchangeAmount);
            }
        }
    }
}

