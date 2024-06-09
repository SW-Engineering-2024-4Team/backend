//package cards.factory.imp.decorators.occupation;
//
//import cards.common.ActionRoundCard;
//import cards.decorators.UnifiedDecoratorNon;
//import models.Player;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//public class BuildFenceDecorator extends UnifiedDecoratorNon {
//
//    public BuildFenceDecorator(ActionRoundCard decoratedCard, Player appliedPlayer) {
//        super(decoratedCard, appliedPlayer);
//    }
//
//    @Override
//    public void buildFence(Player player) {
//        if (player.equals(appliedPlayer) && player.isCompressedSoilActive()) {
//            // 플레이어가 울타리를 지을 수 있는 유효한 좌표를 가져옴.
//            Set<int[]> validPositions = player.getPlayerBoard().getValidFencePositions();
//            player.getPlayerBoard().printPlayerBoardWithFences("유효 좌표", validPositions);
//
//            // 지을 수 있는 좌표가 없으면 짓지 못함.
//            if (!validPositions.isEmpty()) {
//                List<int[]> selectedPositions = new ArrayList<>();
//

//                boolean fenceBuildingComplete = false;
//                while (!fenceBuildingComplete) {

//                    int[] position = validPositions.iterator().next();
//                    selectedPositions.add(position);
//                    validPositions = player.getPlayerBoard().getValidFencePositions();
//                    // 유효 좌표가 없거나, 자원 부족등의 이유로 짓지 못하면 해당 좌표까지만 울타리를 지음
//                    if (validPositions.isEmpty() || !player.canContinueFenceBuilding()) {
//                        fenceBuildingComplete = true;
//                    }
//                }
//
//                // 선택된 좌표들의 모음으로 울타리를 지음
//                player.getPlayerBoard().buildFences(selectedPositions, player);
//
//                // 필요한 자원 계산
//                int woodNeeded = player.getPlayerBoard().calculateRequiredWoodForFences(selectedPositions);
//
//                // 자원 사용
//                if (woodNeeded > 0) {
//                    boolean useClay = player.chooseResource("wood", "clay", woodNeeded);
//                    if (useClay) {
//                        player.addResource("clay", -woodNeeded);
//                        System.out.println("다진 흙 효과: 울타리를 치기 위해 나무 대신 흙을 사용합니다.");
//                    } else {
//                        player.addResource("wood", -woodNeeded);
//                        System.out.println("울타리를 치기 위해 나무를 사용합니다.");
//                    }
//                }
//
//                System.out.println("Fences built at: " + selectedPositions.stream().map(Arrays::toString).collect(Collectors.joining(", ")));
//            } else {
//                System.out.println("울타리를 지을 유효한 위치가 없습니다.");
//            }
//        } else {
//            super.buildFence(player);
//        }
//    }
//
//    @Override
//    public void execute(Player player) {
//        if (decoratedCard.getName().equals("울타리")) {
//            buildFence(player);
//        } else {
//            executeThen(player,
//                    () -> renovateRooms(player),
//                    () -> buildFence(player)
//            );
//        }
//
//    }
//}
package com.example.agricola.cards.factory.imp.decorators.minorimprovement;


import com.example.agricola.cards.common.ActionRoundCard;
import com.example.agricola.cards.decorators.UnifiedDecoratorNon;
import com.example.agricola.models.Player;
import com.example.agricola.service.GameService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BuildFenceDecorator extends UnifiedDecoratorNon {

    public BuildFenceDecorator(ActionRoundCard decoratedCard, Player appliedPlayer) {
        super(decoratedCard, appliedPlayer);
    }

    @Override
    public void buildFence(Player player) {
        if (player.equals(appliedPlayer) && player.isCompressedSoilActive()) {
            GameService gameService = player.getGameService();
            String playerId = player.getId();

            // 프론트엔드에 울타리 요청을 보냄
            gameService.sendFenceRequestToFrontEnd(playerId);

            // 프론트엔드로부터 선택된 좌표를 받아옴
            synchronized (gameService) {
                try {
                    gameService.wait(); // 프론트엔드에서 신호를 받을 때까지 대기
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            List<int[]> selectedPositions = gameService.getSelectedFencePositions(playerId);

            if (selectedPositions != null && !selectedPositions.isEmpty()) {
                // 필요한 자원 계산
                int woodNeeded = player.getPlayerBoard().calculateRequiredWoodForFences(selectedPositions);

                // 자원 사용
                if (woodNeeded > 0) {
                    boolean useClay = player.chooseResource("wood", "clay", woodNeeded);
                    if (useClay) {
                        player.addResource("clay", -woodNeeded);
                        System.out.println("다진 흙 효과: 울타리를 치기 위해 나무 대신 흙을 사용합니다.");
                    } else {
                        player.addResource("wood", -woodNeeded);
                        System.out.println("울타리를 치기 위해 나무를 사용합니다.");
                    }
                }

                // 선택된 좌표들의 모음으로 울타리를 지음
                player.getPlayerBoard().buildFences(selectedPositions, player);
                System.out.println("Fences built at: " + selectedPositions.stream().map(Arrays::toString).collect(Collectors.joining(", ")));
            } else {
                System.out.println("울타리를 지을 유효한 위치가 없습니다.");
            }
        } else {
            super.buildFence(player);
        }
    }

    @Override
    public void execute(Player player) {
        if (decoratedCard.getName().equals("울타리")) {
            buildFence(player);
        } else {
            GameService gameService = player.getGameService();

            // 프론트엔드에 선택지 전송
            Map<String, Object> options = Map.of(
                    "option1", "방 고치기 후 울타리 치기",
                    "option2", "방 고치기만",
                    "option3", "울타리 치기만"
            );

            gameService.sendChoiceRequestToFrontEnd(player.getId(), "Then", options);

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

            boolean choice = gameService.getPlayerThenChoice(player.getId());

            // 선택에 따른 액션 실행
            if (choice) {
                renovateRooms(player);
                buildFence(player);
            } else {
                renovateRooms(player);
            }
        }
    }

    private void handleTimeout(Player player) {
        // 기본 선택: 여기서는 단순히 첫 번째 행동을 수행하는 예시를 보여줍니다.
        renovateRooms(player);
        System.out.println("기본 선택으로 방 고치기를 수행했습니다.");
    }

}
