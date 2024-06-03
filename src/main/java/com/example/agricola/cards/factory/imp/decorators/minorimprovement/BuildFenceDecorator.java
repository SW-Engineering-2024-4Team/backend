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
//                // TODO 좌표 무더기로 들어오면 펜스 짓기로. 하나씩 선택은 프론트에서 하기로
//                boolean fenceBuildingComplete = false;
//                while (!fenceBuildingComplete) {
//                    // TODO 플레이어 좌표 입력 로직 (여기서는 유효 위치 중 하나를 선택하는 것으로 가정)
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

import java.util.*;
import java.util.stream.Collectors;

public class BuildFenceDecorator extends UnifiedDecoratorNon {

    public BuildFenceDecorator(ActionRoundCard decoratedCard, Player appliedPlayer) {
        super(decoratedCard, appliedPlayer);
    }

    @Override
    public void buildFence(Player player) {
        if (player.equals(appliedPlayer) && player.isCompressedSoilActive()) {
            // 플레이어가 울타리를 지을 수 있는 유효한 좌표를 가져옴.
            Set<int[]> validPositions = player.getPlayerBoard().getValidFencePositions();
            player.getPlayerBoard().printPlayerBoardWithFences("유효 좌표", validPositions);

            // 지을 수 있는 좌표가 없으면 짓지 못함.
            if (!validPositions.isEmpty()) {
                List<int[]> selectedPositions = new ArrayList<>();

                boolean fenceBuildingComplete = false;
                while (!fenceBuildingComplete) {
                    Iterator<int[]> iterator = validPositions.iterator();
                    if (iterator.hasNext()) {
                        int[] position = iterator.next();
                        selectedPositions.add(position);
                        iterator.remove();  // 선택한 위치를 유효 위치에서 제거

                        // 임시로 selectedPositions에 추가된 좌표들을 기반으로 유효 좌표 계산
                        validPositions = player.getPlayerBoard().getValidFencePositions();
                        validPositions.removeAll(selectedPositions);

                        // 선택된 좌표와 인접한 타일들만 남기기
                        validPositions.removeIf(pos -> !isAdjacent(selectedPositions.get(selectedPositions.size() - 1), pos));

                        // 유효 좌표가 없거나, 자원 부족등의 이유로 짓지 못하면 해당 좌표까지만 울타리를 지음
                        if (validPositions.isEmpty() || !player.canContinueFenceBuilding()) {
                            fenceBuildingComplete = true;
                        }
                    } else {
                        fenceBuildingComplete = true;
                    }
                }

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


    // 인접한 좌표인지 확인하는 메서드
    private boolean isAdjacent(int[] pos1, int[] pos2) {
        int dx = Math.abs(pos1[0] - pos2[0]);
        int dy = Math.abs(pos1[1] - pos2[1]);
        return (dx == 1 && dy == 0) || (dx == 0 && dy == 1);
    }

    @Override
    public void execute(Player player) {
        if (decoratedCard.getName().equals("울타리")) {
            buildFence(player);
        } else {
            executeThen(player,
                    () -> renovateRooms(player),
                    () -> buildFence(player)
            );
        }
    }
}
