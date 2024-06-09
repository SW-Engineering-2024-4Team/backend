package com.example.agricola.cards.minorimprovement;


import com.example.agricola.cards.common.ExchangeableCard;
import com.example.agricola.cards.common.UnifiedCard;
import com.example.agricola.enums.ExchangeTiming;
import com.example.agricola.models.Player;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

public class MinorImprovementCard implements UnifiedCard, ExchangeableCard {
    private int id;
    private String name;
    private String description;
    private LinkedHashMap<String, Integer> exchangeRate;
    private Map<String, Integer> gainResources;
    protected Map<String, Integer> cost;
    private Predicate<Player> condition;
    private ExchangeTiming exchangeTiming;
    private int bonusPoints;

    public MinorImprovementCard(int id, String name, String description, LinkedHashMap<String, Integer> exchangeRate, Map<String, Integer> gainResources, Map<String, Integer> cost, Predicate<Player> condition, ExchangeTiming exchangeTiming, int bonusPoints) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.exchangeRate = exchangeRate;
        this.gainResources = gainResources;
        this.cost = cost;
        this.condition = condition;
        this.exchangeTiming = exchangeTiming;
        this.bonusPoints = bonusPoints;
    }

    @Override
    public void execute(Player player) {
        if (testCondition(player)) {
            if (checkResources(player, cost)) {
                payResources(player, cost);
                applyEffect(player);
                gainResource(player);
            } else {
                // 자원이 부족하다는 메시지 표시
            }
        } else {
            // 조건을 만족하지 않는다는 메시지 표시
        }
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
    public void gainResource(Player player) {
        if (gainResources != null) {
            for (Map.Entry<String, Integer> entry : gainResources.entrySet()) {
                player.addResource(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public void applyEffect(Player player) {
        // 구현 필요: 카드의 효과를 적용하는 로직
    }

    @Override
    public boolean canExchange(ExchangeTiming timing) {
        return exchangeRate != null && this.exchangeTiming == timing;
    }

//    @Override
//    // TODO 양 자원 교환시 양을 플레이어 보드에서 제거해줘야 함
//    public void executeExchange(Player player, String fromResource, String toResource, int amount) {
//        if (canExchange(exchangeTiming)) {
//            int exchangeAmount = exchangeRate.get(toResource) * amount / exchangeRate.get(fromResource);
//            player.addResource(fromResource, -amount);
//            player.addResource(toResource, exchangeAmount);
//        }
//    }

    @Override
    public LinkedHashMap<String, Integer> getExchangeRate() {
        return exchangeRate;
    }

    public boolean testCondition(Player player) {
        return condition == null || condition.test(player);
    }

    public int getBonusPoints() {
        return bonusPoints;
    }

    public Map<String, Integer> getPurchaseCost() {
        return cost;
    }

    public void setPurchaseCost(Map<String, Integer> cost) {
        this.cost = cost;
    }
}
