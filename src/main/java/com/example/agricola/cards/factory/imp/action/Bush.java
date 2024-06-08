package com.example.agricola.cards.factory.imp.action;


import com.example.agricola.cards.action.AccumulativeActionCard;
import com.example.agricola.models.Player;

import java.util.HashMap;
import java.util.Map;

public class Bush extends AccumulativeActionCard {

    public Bush(int id) {
        super(id, "덤불", "나무 자원 1개를 누적합니다..", createAccumulatedAmounts());
    }

    private static Map<String, Integer> createAccumulatedAmounts() {
        Map<String, Integer> accumulatedAmounts = new HashMap<>();
        accumulatedAmounts.put("wood", 1); // 나무 1개
        return accumulatedAmounts;
    }

    @Override
    public void execute(Player player) {
        // 누적된 자원을 플레이어에게 부여
        gainResources(player, getAccumulatedResources());
        // 카드를 점유 상태로 설정
        clearAccumulatedResources();
    }
}
