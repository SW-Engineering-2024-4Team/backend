package com.example.agricola.cards.factory.imp.decorators.occupation;


import com.example.agricola.cards.common.ActionRoundCard;
import com.example.agricola.cards.decorators.UnifiedDecoratorNon;
import com.example.agricola.models.Player;
import com.example.agricola.service.GameService;

import java.util.Map;

public class BuildHouseDecorator extends UnifiedDecoratorNon {

    public BuildHouseDecorator(ActionRoundCard decoratedCard, Player appliedPlayer) {
        super(decoratedCard, appliedPlayer);
    }

    @Override
    public void buildHouse(Player player) {
        super.buildHouse(player);
        if (player.equals(appliedPlayer)) {
            player.addResource("food", 3);
            System.out.println("초벽질공 효과: 음식 3개를 가져옵니다.");
        }
    }

    @Override
    public void execute(Player player) {
        GameService gameService = player.getGameService();

        // 프론트엔드에 선택지 전송
        Map<String, Object> options = Map.of(
                "option1", "집 짓기 및 외양간 짓기",
                "option2", "집 짓기만",
                "option3", "외양간 짓기만"
        );

        gameService.sendChoiceRequestToFrontEnd(player.getId(), "AndOr", options);

        // 플레이어의 선택 대기
        long startTime = System.currentTimeMillis();
        long timeout = 5000; // 5초

        while (!gameService.hasPlayerChoice(player.getId())) {
            if (System.currentTimeMillis() - startTime > timeout) {
                System.out.println("시간 초과: 기본 선택을 수행합니다.");
                handleTimeout(player);
                return;
            }
            try {
                Thread.sleep(100); // 작은 시간 동안 대기
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        int choice = gameService.getPlayerChoice(player.getId());

        // 선택에 따른 액션 실행
        if (choice == 0) {
            buildHouse(player);
            buildBarn(player);
        } else if (choice == 1) {
            buildHouse(player);
        } else if (choice == 2) {
            buildBarn(player);
        }
    }

    private void handleTimeout(Player player) {
        // 기본 선택: 여기서는 단순히 첫 번째 행동을 수행하는 예시를 보여줍니다.
        buildHouse(player);
        System.out.println("기본 선택으로 집 짓기를 수행했습니다.");
    }

}