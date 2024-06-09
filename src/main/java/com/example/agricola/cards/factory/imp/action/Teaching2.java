package com.example.agricola.cards.factory.imp.action;


import com.example.agricola.cards.action.NonAccumulativeActionCard;
import com.example.agricola.models.Player;

import java.util.HashMap;
import java.util.Map;

public class Teaching2 extends NonAccumulativeActionCard {

    public Teaching2(int id) {
        super(id, "교습2", "음식 1개를 지불하고 직업카드를 사용합니다.");
    }

    private static Map<String, Integer> neededResources() {
        Map<String, Integer> neededResources = new HashMap<>();
        neededResources.put("food", 1);

        return neededResources;
    }

    @Override
    public void execute(Player player) {
        if(!checkResources(player, neededResources())) {
            return;
        }
        payResources(player, neededResources());
        useOccupationCard(player);
    }
}
