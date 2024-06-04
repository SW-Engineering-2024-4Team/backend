package com.example.agricola.cards.factory.imp.major;


import com.example.agricola.cards.common.BakingCard;
import com.example.agricola.cards.majorimprovement.MajorImprovementCard;
import com.example.agricola.enums.ExchangeTiming;
import com.example.agricola.models.Player;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Hearth2 extends MajorImprovementCard implements BakingCard {
    public Hearth2(int id) {
        super(id,
                "화로2",
                "아무때나 양 한 마리를 음식 2개로 교환 가능. 빵굽기: 곡식 1개를 음식 2개로 교환 가능. 추가점수 1점.",
                createPurchaseCost(),
                createAExchangeRate(),
                createBreadBakingExchangeRate(),
                1,
                false,
                ExchangeTiming.ANYTIME
        );
    }

    private static Map<String, Integer> createPurchaseCost() {
        Map<String, Integer> cost = new HashMap<>();
        cost.put("clay", 3);
        return cost;
    }

    private static LinkedHashMap<String, Integer> createAExchangeRate() {
        LinkedHashMap<String, Integer> rate = new LinkedHashMap<>();
        rate.put("sheep", 1);
        rate.put("food", 2);
        return rate;
    }

    private static Map<String, Integer> createBreadBakingExchangeRate() {
        Map<String, Integer> rate = new HashMap<>();
        rate.put("grain", 1);
        rate.put("food", 2);
        return rate;
    }

//    @Override
//    public void triggerBreadBaking(Player player) {
//        int grain = player.getResource("grain");
//        int food = grain * 2;
//        player.addResource("grain", -grain);
//        player.addResource("food", food);
//    }

    @Override
    public boolean hasBreadBakingExchangeRate() {
        return true; // 이 클래스는 항상 빵굽기 교환 비율을 가지고 있음
    }
}