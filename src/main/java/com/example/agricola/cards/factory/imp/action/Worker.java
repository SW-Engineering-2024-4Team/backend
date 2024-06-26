package com.example.agricola.cards.factory.imp.action;


import com.example.agricola.cards.action.NonAccumulativeActionCard;
import com.example.agricola.models.Player;

import java.util.HashMap;
import java.util.Map;

public class Worker extends NonAccumulativeActionCard {

    public Worker(int id) {
        super(id, "날품팔이", "음식 자원을 2개씩 획득합니다.");
    }

    public Map<String, Integer> createResourcesToGain() {
        Map<String, Integer> resourcesToGain = new HashMap<>();
        resourcesToGain.put("food", 2);
        return resourcesToGain;
    }

    @Override
    public void execute(Player player) {
        gainResources(player, createResourcesToGain());
    }
}
