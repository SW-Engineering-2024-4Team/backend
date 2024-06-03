package com.example.agricola.cards.factory.imp.action;

import com.example.agricola.cards.action.NonAccumulativeActionCard;
import com.example.agricola.enums.RoomType;
import com.example.agricola.models.Player;
import com.example.agricola.models.PlayerBoard;
import com.example.agricola.service.GameService;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class FarmExpansion extends NonAccumulativeActionCard {

    public FarmExpansion(int id) {
        super(id, "농장 확장", "방 만들기 그리고/또는 외양간 짓기");
    }

    @Override
    public void execute(Player player) {
        GameService gameService = player.getGameService();

        // 방 만들기 그리고/또는 외양간 짓기
        executeAndOr(player,
                () -> {
                    System.out.println("buildHouse action is being called.");
                    buildHouse(player);
                },
                () -> {
                    System.out.println("buildBarn action is being called.");
                    buildBarn(player);
                },
                gameService
        );
    }

    private void executeAndOr(Player player, Runnable action1, Runnable action2, GameService gameService) {
        Map<String, Object> options = new LinkedHashMap<>();
        options.put("option1", "방 만들기 및 외양간 짓기");
        options.put("option2", "방 만들기만");
        options.put("option3", "외양간 짓기만");

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
            System.out.println("Executing both actions: buildHouse and buildBarn");
            action1.run();
            action2.run();
        } else if (choice == 1) {
            System.out.println("Executing action1: buildHouse");
            action1.run();
        } else if (choice == 2) {
            System.out.println("Executing action2: buildBarn");
            action2.run();
        } else {
            System.out.println("Invalid choice: " + choice); // 디버깅 메시지 추가
        }
    }



}
