package com.example.agricola.cards.factory.imp.major;


import com.example.agricola.cards.common.BakingCard;
import com.example.agricola.cards.majorimprovement.MajorImprovementCard;
import com.example.agricola.enums.ExchangeTiming;
import com.example.agricola.models.Player;

import java.util.HashMap;
import java.util.Map;

public class ClayOven extends MajorImprovementCard implements BakingCard {
    public ClayOven(int id) {
        super(
                id,
                "흙 가마",
                "빵굽기: 곡식 1개당 음식 5개로 교환 가능. 이 설비를 지을 때 즉시 빵굽기 행동을 할 수 있습니다. 추가점수 2점.",
                createPurchaseCost(),
                null,
                createBreadBakingExchangeRate(),
                2,
                true,
                ExchangeTiming.NONE
        );
    }

    private static Map<String, Integer> createPurchaseCost() {
        Map<String, Integer> cost = new HashMap<>();
        cost.put("clay", 3);
        cost.put("stone", 1);
        return cost;
    }

    private static Map<String, Integer> createBreadBakingExchangeRate() {
        Map<String, Integer> rate = new HashMap<>();
        rate.put("grain", 1);
        rate.put("food", 5);
        return rate;
    }

    @Override
    public void triggerBreadBaking(Player player) {
        int grain = player.getResource("grain");
        int food = grain * 5;
        player.addResource("grain", -grain);
        player.addResource("food", food);
    }
}
