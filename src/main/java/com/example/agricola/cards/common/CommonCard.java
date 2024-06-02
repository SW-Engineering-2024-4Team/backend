package com.example.agricola.cards.common;

import com.example.agricola.models.Player;

import java.util.Map;

public interface CommonCard {

    int getId();
    String getName();
    String getDescription();


    // TODO 기능이 조합된 경우 플레이어의 선택을 어떻게 받아낼지
    void execute(Player player);

    // 기능 조합 유틸리티 메서드
    default void executeAndOr(Player player, Runnable... actions) {
        for (Runnable action : actions) {
            action.run();
        }
    }

    default void executeOr(Player player, Runnable action1, Runnable action2) {
        boolean choice = player.chooseOption(); // 플레이어가 선택한 옵션
        if (choice) {
            action1.run();
        } else {
            action2.run();
        }
    }

    default void executeThen(Player player, Runnable action1, Runnable action2) {
        action1.run();
        action2.run();
    }

    // 자원 체크 및 지불 메서드 추가
    default boolean checkResources(Player player, Map<String, Integer> resources) {
        for (Map.Entry<String, Integer> entry : resources.entrySet()) {
            if (player.getResource(entry.getKey()) < entry.getValue()) {
                return false; // 자원이 부족하면 false 반환
            }
        }
        return true; // 자원이 충분하면 true 반환
    }

    default void payResources(Player player, Map<String, Integer> resources) {
        for (Map.Entry<String, Integer> entry : resources.entrySet()) {
            player.addResource(entry.getKey(), -entry.getValue());
        }
    }

    default Map<String, Object> toMap() {
        return Map.of(
                "id", getId(),
                "name", getName(),
                "description", getDescription()
        );
    }


}
