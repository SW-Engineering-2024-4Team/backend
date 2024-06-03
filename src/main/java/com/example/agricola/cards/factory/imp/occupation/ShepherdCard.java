package com.example.agricola.cards.factory.imp.occupation;


import com.example.agricola.cards.occupation.OccupationCard;
import com.example.agricola.enums.ExchangeTiming;

import java.util.HashMap;
import java.util.Map;

public class ShepherdCard extends OccupationCard {

    public ShepherdCard(int id) {
        super(id, "양 보행자", "아무때나 양 1마리를 돌 1개로 바꿉니다.", createExchangeRate(), null, 1, 4, ExchangeTiming.ANYTIME);
    }

    private static Map<String, Integer> createExchangeRate() {
        Map<String, Integer> exchangeRate = new HashMap<>();
        exchangeRate.put("sheep", 1);
        exchangeRate.put("stone", 1);
        return exchangeRate;
    }
}
