package com.example.agricola.cards.factory.imp.round;

import com.example.agricola.cards.round.NonAccumulativeRoundCard;
import com.example.agricola.models.Player;
import com.example.agricola.service.GameService;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlantSeed extends NonAccumulativeRoundCard {

    public PlantSeed(int id, int cycle) {
        super(id, "곡식 활용", "씨 뿌리기 그리고/또는 빵 굽기", cycle);
    }

    @Override
    public void execute(Player player) {
        GameService gameService = player.getGameService();

        // 씨 뿌리기 그리고/또는 빵 굽기
        executeAndOr(player,
                () -> {
                    System.out.println("plantField action is being called.");
                    plantField(player);
                },
                () -> {
                    System.out.println("triggerBreadBaking action is being called.");
                    triggerBreadBaking(player);
                },
                gameService
        );
    }

    private void executeAndOr(Player player, Runnable action1, Runnable action2, GameService gameService) {
        Map<String, Object> options = new LinkedHashMap<>();
        options.put("option1", "씨 뿌리기 및 빵 굽기");
        options.put("option2", "씨 뿌리기만");
        options.put("option3", "빵 굽기만");

        System.out.println("Sending choice request to frontend.");
        gameService.sendChoiceRequestToFrontEnd(player.getId(), "AndOr", options);

        // 플레이어 선택 대기
        try {
            System.out.println("Waiting for player choice...");
            gameService.waitForPlayerChoice(player.getId()); // 대기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int choice = gameService.getPlayerChoice(player.getId());
        System.out.println("Player choice received: " + choice);

        // 선택에 따른 액션 실행
        if (choice == 0) {
            System.out.println("Executing both actions: plantField and triggerBreadBaking");
            action1.run();
            action2.run();
        } else if (choice == 1) {
            System.out.println("Executing action1: plantField");
            action1.run();
        } else if (choice == 2) {
            System.out.println("Executing action2: triggerBreadBaking");
            action2.run();
        } else {
            System.out.println("Invalid choice: " + choice); // 디버깅 메시지 추가
        }
    }

    private void handleTimeout(Player player, Runnable action1, Runnable action2) {
        // 기본 선택: 여기서는 단순히 첫 번째 행동을 수행하는 예시를 보여줍니다.
        action1.run();
        System.out.println("기본 선택으로 씨 뿌리기를 수행했습니다.");
    }
}
