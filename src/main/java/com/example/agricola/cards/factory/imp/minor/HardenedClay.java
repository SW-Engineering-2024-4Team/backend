package com.example.agricola.cards.factory.imp.minor;

import com.example.agricola.cards.minorimprovement.MinorImprovementCard;
import com.example.agricola.enums.ExchangeTiming;
import com.example.agricola.models.Player;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class HardenedClay extends MinorImprovementCard {
    public HardenedClay(int id) {
        super(id, "경질 자기", "아무 때나 흙 2/3/4개를 돌 1/2/3개로 바꿀 수 있습니다.",
                createExchangeRate(), null,
                createPurchaseCost(), null,
                ExchangeTiming.ANYTIME, 1);
    }

    private static Map<String, Integer> createPurchaseCost() {
        Map<String, Integer> cost = new HashMap<>();
        cost.put("clay", 1);
        return cost;
    }

    private static LinkedHashMap<String, Integer> createExchangeRate() {
        LinkedHashMap<String, Integer> exchangeRate = new LinkedHashMap<>();
        exchangeRate.put("clay", 2); // fromResource
        exchangeRate.put("stone", 1); // toResource
        return exchangeRate;
    }

    @Override
    public void applyEffect(Player player) {
        // 추가 효과가 필요 없으므로 비워둠
    }
}
