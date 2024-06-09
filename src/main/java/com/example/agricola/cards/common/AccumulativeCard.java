package com.example.agricola.cards.common;

import java.util.Map;

public interface AccumulativeCard extends com.example.agricola.cards.common.ActionRoundCard {
    Map<String, Integer> getAccumulatedResources(); // 누적된 자원을 반환하는 메서드
    Map<String, Integer> getAccumulatedAmounts();
    void clearAccumulatedResources(); // 누적된 자원을 초기화하는 메서드
    void accumulateResources();

}
