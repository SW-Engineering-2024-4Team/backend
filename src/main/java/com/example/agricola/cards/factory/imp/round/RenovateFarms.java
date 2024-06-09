package com.example.agricola.cards.factory.imp.round;

import com.example.agricola.cards.round.NonAccumulativeRoundCard;
import com.example.agricola.models.Player;
import com.example.agricola.service.GameService;

import java.util.LinkedHashMap;
import java.util.Map;

public class RenovateFarms extends NonAccumulativeRoundCard {
    public RenovateFarms(int id, int cycle) {
        super(id, "농장 개조", "집을 고칩니다. 한 후에 울타리를 칩니다.", cycle);
    }

    @Override
    public void execute(Player player) {
        GameService gameService = player.getGameService();

        // 집을 고치고 한 후에 울타리를 칩니다.
        executeThen(player,
                () -> {
                    System.out.println("renovateRooms action is being called.");
                    renovateRooms(player);
                },
                () -> {
                    System.out.println("buildFence action is being called.");
                    buildFence(player);
                },
                gameService
        );
    }

    private void executeThen(Player player, Runnable action1, Runnable action2, GameService gameService) {
        Map<String, Object> options = new LinkedHashMap<>();
        options.put("option1", "집 고치기 후 울타리 치기");
        options.put("option2", "집 고치기만");

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
            System.out.println("Executing both actions: renovateRooms and buildFence");
            action1.run();
            action2.run();
        } else {
            System.out.println("Executing action1: renovateRooms");
            action1.run();
        }
    }

    private void handleTimeout(Player player, Runnable action1) {
        // 기본 선택: 여기서는 단순히 첫 번째 행동을 수행하는 예시를 보여줍니다.
        action1.run();
        System.out.println("기본 선택으로 집 고치기를 수행했습니다.");
    }
}
