package com.example.agricola.cards.factory.imp.action;


import com.example.agricola.cards.action.NonAccumulativeRoundCard;
import com.example.agricola.models.Player;

import java.util.HashMap;
import java.util.Map;

public class ResourceMarket extends NonAccumulativeRoundCard {
    Map<String, Integer> resourcesToGain = new HashMap<String, Integer>();

    public ResourceMarket(int id) {
        super(id, "자원시장", "돌, 음식 자원을 1개씩 획득합니다.");
        resourcesToGain.put("stone", 1); // 돌 1개 획득
        resourcesToGain.put("food", 1);// 음식 1개 획득
    }
    @Override
    public Map<String, Integer> createResourcesToGain() {
        super.setHasResources();
        return resourcesToGain;
    }

    @Override
    public void execute(Player player) {
            gainResources(player, createResourcesToGain());
    }
}
