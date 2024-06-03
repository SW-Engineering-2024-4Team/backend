package com.example.agricola.cards.factory.imp.minor;


import com.example.agricola.cards.minorimprovement.MinorImprovementCard;
import com.example.agricola.enums.ExchangeTiming;
import com.example.agricola.models.Player;

import java.util.HashMap;
import java.util.Map;

public class HardenedClay extends MinorImprovementCard {
    public HardenedClay(int id) {
        super(id, "경질 자기", "아무 때나 흙 2/3/4개를 돌 1/2/3개로 바꿀 수 있습니다.", createExchangeRates(), null, createPurchaseCost(), null, ExchangeTiming.ANYTIME, 1);
    }

    private static Map<String, Integer> createPurchaseCost() {
        Map<String, Integer> cost = new HashMap<>();
        cost.put("clay", 1);
        return cost;
    }

    private static Map<String, Integer> createExchangeRates() {
        Map<String, Integer> exchangeRates = new HashMap<>();
        exchangeRates.put("clay2", 1); // 흙 2개 -> 돌 1개
        exchangeRates.put("clay3", 2); // 흙 3개 -> 돌 2개
        exchangeRates.put("clay4", 3); // 흙 4개 -> 돌 3개
        return exchangeRates;
    }

    @Override
    public void applyEffect(Player player) {
        // 추가 효과가 필요 없으므로 비워둠
    }
}
