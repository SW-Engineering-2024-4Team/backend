package com.example.agricola.cards.factory.imp.round;

import com.example.agricola.cards.round.NonAccumulativeRoundCard;
import com.example.agricola.models.Player;
import com.example.agricola.service.GameService;

import java.util.LinkedHashMap;
import java.util.Map;

public class AddFamilyMember extends NonAccumulativeRoundCard {
    public AddFamilyMember(int id, int cycle) {
        super(id, "기본 가족 늘리기", "가족 구성원을 늘립니다. 한 후에 보조 설비 카드를 하나 사용합니다.", cycle);
    }

    @Override
    public void execute(Player player) {
        GameService gameService = player.getGameService();

        // 가족 구성원을 늘립니다. 한 후에 보조 설비 카드를 하나 사용합니다.
        executeThen(player,
                () -> addNewborn(player),
                () -> useMinorImprovementCard(player),
                gameService
        );
    }

    private void executeThen(Player player, Runnable action1, Runnable action2, GameService gameService) {
        Map<String, Object> options = new LinkedHashMap<>();
        options.put("option1", "가족 구성원 늘리기 후 보조 설비 카드 사용");
        options.put("option2", "가족 구성원 늘리기만");
        options.put("option3", "보조 설비 카드 사용만");

        System.out.println("Sending choice request to frontend.");
        gameService.sendChoiceRequestToFrontEnd(player.getId(), "Then", options);

        // 플레이어 선택 대기
        try {
            System.out.println("Waiting for player choice...");
            gameService.waitForPlayerChoice(player.getId()); // 대기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean choice = gameService.getPlayerThenChoice(player.getId());
        System.out.println("Player choice received: " + choice);

        // 선택에 따른 액션 실행
        if (choice) {
            System.out.println("Executing both actions: addNewborn and useMinorImprovementCard");
            action1.run();
            action2.run();
        } else {
            System.out.println("Executing action1: addNewborn");
            action1.run();
        }
    }

    private void handleTimeout(Player player, Runnable action1, Runnable action2) {
        // 기본 선택: 여기서는 단순히 첫 번째 행동을 수행하는 예시를 보여줍니다.
        action1.run();
        System.out.println("기본 선택으로 가족 구성원을 늘렸습니다.");
    }
}
