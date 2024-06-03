package com.example.agricola.cards.factory.imp.round;

import com.example.agricola.cards.round.NonAccumulativeRoundCard;
import com.example.agricola.models.Player;
import com.example.agricola.service.GameService;

import java.util.LinkedHashMap;
import java.util.Map;

public class PurchaseMajor extends NonAccumulativeRoundCard {
    public PurchaseMajor(int id, int cycle) {
        super(id, "주요 설비", "주요 설비 카드를 하나 구매합니다. 또는 보조 설비 카드 하나를 사용합니다.", cycle);
    }

    @Override
    public void execute(Player player) {
        GameService gameService = player.getGameService();

        executeOr(player,
                () -> {
                    System.out.println("purchaseMajorImprovementCard action is being called.");
                    purchaseMajorImprovementCard(player);
                },
                () -> {
                    System.out.println("useMinorImprovementCard action is being called.");
                    useMinorImprovementCard(player);
                },
                gameService
        );
    }

    private void executeOr(Player player, Runnable action1, Runnable action2, GameService gameService) {
        Map<String, Object> options = new LinkedHashMap<>();
        options.put("option1", "주요 설비 카드 구매");
        options.put("option2", "보조 설비 카드 사용");

        System.out.println("Sending choice request to frontend.");
        gameService.sendChoiceRequestToFrontEnd(player.getId(), "Or", options);

        // 플레이어 선택 대기
        try {
            System.out.println("Waiting for player choice...");
            gameService.waitForPlayerChoice(player.getId()); // 대기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean choice = gameService.getPlayerOrChoice(player.getId());
        System.out.println("Player choice received: " + choice);

        // 선택에 따른 액션 실행
        if (choice) {
            System.out.println("Executing action2: useMinorImprovementCard");
            action2.run();
        } else {
            System.out.println("Executing action1: purchaseMajorImprovementCard");
            action1.run();
        }
    }

    private void handleTimeout(Player player, Runnable action1) {
        // 기본 선택: 여기서는 단순히 첫 번째 행동을 수행하는 예시를 보여줍니다.
        action1.run();
        System.out.println("기본 선택으로 주요 설비 카드를 구매했습니다.");
    }
}
