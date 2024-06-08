package com.example.agricola.cards.factory.imp.action;


import com.example.agricola.cards.action.NonAccumulativeActionCard;
import com.example.agricola.models.Player;

import java.util.HashMap;
import java.util.Map;

public class Seed extends NonAccumulativeActionCard {
    Map<String, Integer> resourcesToGain = new HashMap<String, Integer>();
    public Seed(int id) {
        super(id, "곡식 종자", "곡식 자원 1개를 획득합니다.");
        resourcesToGain.put("grain", 1);
        super.setHasResources();
    }
    @Override
    public Map<String, Integer> createResourcesToGain() {
        return resourcesToGain;
    }

    @Override
    public void execute(Player player) {
        gainResources(player, createResourcesToGain());
    }
}
