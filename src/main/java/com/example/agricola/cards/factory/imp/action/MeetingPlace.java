package com.example.agricola.cards.factory.imp.action;

import com.example.agricola.cards.action.NonAccumulativeActionCard;
import com.example.agricola.models.Player;
import com.example.agricola.service.GameService;

import java.util.LinkedHashMap;
import java.util.Map;

public class MeetingPlace extends NonAccumulativeActionCard {

    public MeetingPlace(int id) {
        super(id, "회합 장소", "선 플레이어 되기 그리고/또는 보조 설비 카드 사용하기");
    }

    @Override
    public void execute(Player player) {
        GameService gameService = player.getGameService();

        // 선 플레이어 되기 그리고/또는 보조 설비 카드 사용하기
        executeAndOr(player,
                () -> becomeFirstPlayer(player),
                () -> useMinorImprovementCard(player),
                gameService
        );
    }

    private void executeAndOr(Player player, Runnable action1, Runnable action2, GameService gameService) {
        Map<String, Object> options = new LinkedHashMap<>();
        options.put("option1", "선 플레이어 되기 및 보조 설비 카드 사용하기");
        options.put("option2", "선 플레이어 되기만");
        options.put("option3", "보조 설비 카드 사용하기만");

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
            System.out.println("Executing both actions: becomeFirstPlayer and useMinorImprovementCard");
            action1.run();
            action2.run();
        } else if (choice == 1) {
            System.out.println("Executing action1: becomeFirstPlayer");
            action1.run();
        } else if (choice == 2) {
            System.out.println("Executing action2: useMinorImprovementCard");
            action2.run();
        } else {
            System.out.println("Invalid choice: " + choice); // 디버깅 메시지 추가
        }
    }
}
