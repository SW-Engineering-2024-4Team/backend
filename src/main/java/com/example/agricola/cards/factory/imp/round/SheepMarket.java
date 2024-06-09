package com.example.agricola.cards.factory.imp.round;


import com.example.agricola.cards.round.AccumulativeRoundCard;
import com.example.agricola.models.Player;

import java.util.HashMap;
import java.util.Map;

public class SheepMarket extends AccumulativeRoundCard {

    public SheepMarket(int id, int cycle) {
        super(id, "양 시장", "양 자원 1마리를 누적합니다..", cycle, createAccumulatedAmounts());
    }

    private static Map<String, Integer> createAccumulatedAmounts() {
        Map<String, Integer> accumulatedAmounts = new HashMap<>();
        accumulatedAmounts.put("sheep", 1); // 양 1개를 누적
        return accumulatedAmounts;
    }

    @Override
    public void execute(Player player) {
        System.out.println("Executing SheepMarket: revealed=" + isRevealed() + ", occupied=" + isOccupied());
        if (isRevealed()) {
            // 누적된 자원을 플레이어에게 부여
            gainResources(player, getAccumulatedResources());
            // 카드를 점유 상태로 설정
//            setOccupied(true);
            // 누적된 자원 초기화
            clearAccumulatedResources();
        } else {
            System.out.println("양 시장 실행 불가: 카드가 공개되지 않았거나 이미 점유됨");
        }
    }
}
