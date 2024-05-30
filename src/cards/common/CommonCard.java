package cards.common;

import models.FamilyMember;
import models.Player;

import java.util.List;
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

//    default boolean executeWithCheck(Player player) {
//        try {
//            execute(player);
//            return true;
//        } catch (Exception e) {
//            System.out.println("Card execution failed: " + e.getMessage());
//            return false;
//        }
//    }
//
//    default boolean executeAndOr(Player player, Runnable... actions) {
//        boolean allSuccess = true;
//        for (Runnable action : actions) {
//            try {
//                action.run();
//            } catch (Exception e) {
//                allSuccess = false;
//                System.out.println("Action failed: " + e.getMessage());
//            }
//        }
//        return allSuccess;
//    }
//
//    default boolean executeOr(Player player, Runnable action1, Runnable action2) {
//        boolean choice = player.chooseOption();
//        try {
//            if (choice) {
//                action1.run();
//            } else {
//                action2.run();
//            }
//            return true;
//        } catch (Exception e) {
//            System.out.println("Action failed: " + e.getMessage());
//            return false;
//        }
//    }
//
//    default boolean executeThen(Player player, Runnable action1, Runnable action2) {
//        try {
//            action1.run();
//            action2.run();
//            return true;
//        } catch (Exception e) {
//            System.out.println("Action failed: " + e.getMessage());
//            return false;
//        }
//    }



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


}

//package cards.common;
//
//import cards.majorimprovement.MajorImprovementCard;
//import controllers.GameController;
//import enums.RoomType;
//import models.*;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//public interface ActionRoundCard extends CommonCard {
//
//    boolean isRevealed(); // RoundCard와 ActionCard 구분을 위해 추가
//    void reveal(); // 라운드 카드를 공개하는 메서드
//    boolean isAccumulative(); // 누적 가능한지 여부 확인 메서드
//    boolean isOccupied(); // 카드가 점유되었는지 확인
//    void setOccupied(boolean occupied); // 카드의 점유 상태 설정
//
//    default boolean gainResources(Player player, Map<String, Integer> resources) {
//        try {
//            for (Map.Entry<String, Integer> entry : resources.entrySet()) {
//                String resource = entry.getKey();
//                int amount = entry.getValue();
//
//                if (resource.equals("sheep")) {
//                    System.out.println("양 자원 시작");
//                    for (int i = 0; i < amount; i++) {
//                        Animal newSheep = new Animal(-1, -1, resource);
//                        player.addNewAnimal(newSheep);
//                    }
//                    int placedSheep = player.placeNewAnimals();
//                    System.out.println("placedSheep = " + placedSheep);
//                    player.addResource(resource, placedSheep);
//                } else {
//                    player.addResource(resource, amount);
//                }
//            }
//            return true; // 성공 시 true 반환
//        } catch (Exception e) {
//            System.out.println("Exception during gaining resources: " + e.getMessage());
//            return false; // 실패 시 false 반환
//        }
//    }
//
//    // 직업 카드 사용
//    default boolean useOccupationCard(Player player) {
//        try {
//            List<CommonCard> occupationCards = player.getOccupationCards();
//            if (!occupationCards.isEmpty()) {
//                UnifiedCard selectedCard = (UnifiedCard) occupationCards.get(0);
//                player.useUnifiedCard(selectedCard);
//                return true; // 성공 시 true 반환
//            }
//            return false; // 사용 가능한 직업 카드가 없으면 false 반환
//        } catch (Exception e) {
//            System.out.println("Exception during using occupation card: " + e.getMessage());
//            return false; // 예외 발생 시 false 반환
//        }
//    }
//
//    // 보조 설비 카드 사용
//    default boolean useMinorImprovementCard(Player player) {
//        try {
//            List<CommonCard> minorImprovementCards = player.getMinorImprovementCards();
//            if (!minorImprovementCards.isEmpty()) {
//                UnifiedCard selectedCard = (UnifiedCard) minorImprovementCards.get(0);
//                player.useUnifiedCard(selectedCard);
//                return true; // 성공 시 true 반환
//            }
//            return false; // 사용 가능한 보조 설비 카드가 없으면 false 반환
//        } catch (Exception e) {
//            System.out.println("Exception during using minor improvement card: " + e.getMessage());
//            return false; // 예외 발생 시 false 반환
//        }
//    }
//
//    // 주요 설비 카드 구매
//    default boolean purchaseMajorImprovementCard(Player player) {
//        try {
//            GameController gameController = player.getGameController();
//            MainBoard mainBoard = gameController.getMainBoard();
//            List<CommonCard> majorImprovementCards = mainBoard.getAvailableMajorImprovementCards();
//
//            MajorImprovementCard selectedCard = null;
//            if (!majorImprovementCards.isEmpty()) {
//                selectedCard = (MajorImprovementCard) majorImprovementCards.get(new Random().nextInt(majorImprovementCards.size()));
//            }
//
//            if (selectedCard != null && player.checkResources(selectedCard.getPurchaseCost())) {
//                player.payResources(selectedCard.getPurchaseCost());
//                player.addMajorImprovementCard(selectedCard);
//                selectedCard.setPurchased(true);
//                System.out.println(selectedCard.getName() + " 카드가 성공적으로 구매되었습니다.");
//                return true; // 성공 시 true 반환
//            } else {
//                System.out.println("자원이 부족하거나 카드가 존재하지 않아 구매할 수 없습니다.");
//                return false; // 실패 시 false 반환
//            }
//        } catch (Exception e) {
//            System.out.println("Exception during purchasing major improvement card: " + e.getMessage());
//            return false; // 예외 발생 시 false 반환
//        }
//    }
//
//    default boolean triggerBreadBaking(Player player) {
//        try {
//            List<CommonCard> majorImprovementCards = player.getMajorImprovementCards();
//            List<BakingCard> bakingCards = majorImprovementCards.stream()
//                    .filter(card -> card instanceof BakingCard)
//                    .map(card -> (BakingCard) card)
//                    .collect(Collectors.toList());
//
//            if (!bakingCards.isEmpty()) {
//                BakingCard selectedCard = player.selectBakingCard(bakingCards);
//                selectedCard.triggerBreadBaking(player);
//                return true; // 성공 시 true 반환
//            } else {
//                return false; // 빵굽기 가능한 설비가 없을 때 false 반환
//            }
//        } catch (Exception e) {
//            System.out.println("Exception during triggering bread baking: " + e.getMessage());
//            return false; // 예외 발생 시 false 반환
//        }
//    }
//
//    // 집 짓기
//    default boolean buildHouse(Player player) {
//        try {
//            PlayerBoard playerBoard = player.getPlayerBoard();
//            Map<String, Integer> playerResources = player.getResources();
//
//            Set<int[]> validPositions = playerBoard.getValidHousePositions();
//
//            if (!validPositions.isEmpty()) {
//                int[] selectedPosition = validPositions.iterator().next();
//                int x = selectedPosition[0];
//                int y = selectedPosition[1];
//
//                RoomType currentRoomType = playerBoard.getExistingRoomType();
//
//                if (currentRoomType == null) {
//                    System.out.println("기존 집 타입을 찾을 수 없습니다.");
//                    return false;
//                }
//                if (playerBoard.canBuildHouse(x, y, currentRoomType, playerResources)) {
//                    Map<String, Integer> cost = player.getHouseResourceCost(currentRoomType);
//                    if (player.checkResources(cost)) {
//                        player.payResources(cost);
//                        player.getPlayerBoard().buildHouse(x, y, currentRoomType);
//                        return true; // 성공 시 true 반환
//                    } else {
//                        System.out.println("자원이 부족합니다.");
//                        return false; // 자원이 부족할 때 false 반환
//                    }
//                } else {
//                    System.out.println("집을 지을 수 없습니다.");
//                    return false; // 집을 지을 수 없는 조건일 때 false 반환
//                }
//            } else {
//                System.out.println("유효한 위치가 없습니다.");
//                return false; // 유효한 위치가 없을 때 false 반환
//            }
//        } catch (Exception e) {
//            System.out.println("Exception during building house: " + e.getMessage());
//            return false; // 예외 발생 시 false 반환
//        }
//    }
//
//    // 밭 일구기
//    default boolean plowField(Player player) {
//        try {
//            PlayerBoard playerBoard = player.getPlayerBoard();
//            Set<int[]> validPositions = playerBoard.getValidPlowPositions();
//
//            if (!validPositions.isEmpty()) {
//                int[] selectedPosition = validPositions.iterator().next();
//                int x = selectedPosition[0];
//                int y = selectedPosition[1];
//
//                playerBoard.plowField(x, y);
//                return true; // 성공 시 true 반환
//            } else {
//                System.out.println("밭을 일굴 수 있는 위치가 없습니다.");
//                return false; // 밭을 일굴 위치가 없을 때 false 반환
//            }
//        } catch (Exception e) {
//            System.out.println("Exception during plowing field: " + e.getMessage());
//            return false; // 예외 발생 시 false 반환
//        }
//    }
//
//    // 외양간 짓기
//    default boolean buildBarn(Player player) {
//        try {
//            PlayerBoard playerBoard = player.getPlayerBoard();
//            Set<int[]> validPositions = playerBoard.getValidBarnPositions();
//
//            if (!validPositions.isEmpty()) {
//                int[] selectedPosition = validPositions.iterator().next();
//                int x = selectedPosition[0];
//                int y = selectedPosition[1];
//
//                if (playerBoard.canBuildBarn(x, y)) {
//                    player.buildBarn(x, y);
//                    return true; // 성공 시 true 반환
//                } else {
//                    System.out.println("외양간을 지을 수 없습니다.");
//                    return false; // 외양간을 지을 수 없는 조건일 때 false 반환
//                }
//            } else {
//                System.out.println("외양간을 지을 수 있는 위치가 없습니다.");
//                return false; // 외양간을 지을 위치가 없을 때 false 반환
//            }
//        } catch (Exception e) {
//            System.out.println("Exception during building barn: " + e.getMessage());
//            return false; // 예외 발생 시 false 반환
//        }
//    }
//
//    // 울타리 짓기
//    default boolean buildFence(Player player) {
//        try {
//            PlayerBoard playerBoard = player.getPlayerBoard();
//            Set<int[]> validPositions = playerBoard.getValidFencePositions();
//
//            if (!validPositions.isEmpty()) {
//                List<int[]> selectedPositions = new ArrayList<>();
//
//                boolean fenceBuildingComplete = false;
//                while (!fenceBuildingComplete) {
//                    int[] position = validPositions.iterator().next();
//                    selectedPositions.add(position);
//                    validPositions = player.getPlayerBoard().getValidFencePositions();
//                    if (validPositions.isEmpty() || !player.canContinueFenceBuilding()) {
//                        fenceBuildingComplete = true;
//                    }
//                }
//                player.getPlayerBoard().buildFences(selectedPositions, player);
//                player.payResources(Map.of("wood", player.getPlayerBoard().calculateRequiredWoodForFences(selectedPositions)));
//                System.out.println("Fences built at: " + selectedPositions.stream().map(Arrays::toString).collect(Collectors.joining(", ")));
//                return true; // 성공 시 true 반환
//            } else {
//                System.out.println("울타리를 지을 유효한 위치가 없습니다.");
//                return false; // 유효한 위치가 없을 때 false 반환
//            }
//        } catch (Exception e) {
//            System.out.println("Exception during building fence: " + e.getMessage());
//            return false; // 예외 발생 시 false 반환
//        }
//    }
//
//    // 곡식 심기
//    default boolean plantField(Player player) {
//        try {
//            PlayerBoard playerBoard = player.getPlayerBoard();
//            String cropType = "grain";
//
//            Set<int[]> validFieldPositions = playerBoard.getValidFieldPositions();
//            if (!validFieldPositions.isEmpty()) {
//                int[] selectedPosition = validFieldPositions.iterator().next();
//                int x = selectedPosition[0];
//                int y = selectedPosition[1];
//
//                int initialCrops = cropType.equals("grain") ? 3 : 2;
//
//                playerBoard.plantField(x, y, initialCrops, cropType);
//                Map<String, Integer> cost = Collections.singletonMap(cropType, 1);
//                player.payResources(cost);
//                return true; // 성공 시 true 반환
//            } else {
//                System.out.println("곡식이나 야채를 심을 수 있는 위치가 없습니다.");
//                return false; // 곡식이나 야채를 심을 위치가 없을 때 false 반환
//            }
//        } catch (Exception e) {
//            System.out.println("Exception during planting field: " + e.getMessage());
//            return false; // 예외 발생 시 false 반환
//        }
//    }
//
//    // 선 플레이어 되기
//    default boolean becomeFirstPlayer(Player player) {
//        try {
//            GameController gameController = player.getGameController();
//            List<Player> turnOrder = gameController.getTurnOrder();
//
//            int playerIndex = turnOrder.indexOf(player);
//            if (playerIndex == -1) {
//                throw new IllegalArgumentException("Player not found in the turn order.");
//            }
//
//            List<Player> newTurnOrder = new ArrayList<>();
//            for (int i = playerIndex; i < turnOrder.size(); i++) {
//                newTurnOrder.add(turnOrder.get(i));
//            }
//            for (int i = 0; i < playerIndex; i++) {
//                newTurnOrder.add(turnOrder.get(i));
//            }
//
//            gameController.setNextTurnOrder(newTurnOrder);
//            return true; // 성공 시 true 반환
//        } catch (Exception e) {
//            System.out.println("Exception during becoming first player: " + e.getMessage());
//            return false; // 예외 발생 시 false 반환
//        }
//    }
//
//    // 집 고치기
//    default boolean renovateRooms(Player player) {
//        try {
//            RoomType newType = player.chooseRoomTypeForRenovation();
//            if (newType != null) {
//                player.renovateHouse(newType);
//                return true; // 성공 시 true 반환
//            } else {
//                System.out.println("더 이상 업그레이드할 수 있는 방이 없습니다.");
//                return false; // 업그레이드할 방이 없을 때 false 반환
//            }
//        } catch (Exception e) {
//            System.out.println("Exception during renovating rooms: " + e.getMessage());
//            return false; // 예외 발생 시 false 반환
//        }
//    }
//
//    // 가족 구성원 추가
//    default boolean addNewborn(Player player) {
//        try {
//            if (player.addFamilyMember()) {
//                List<int[]> emptyRooms = player.getPlayerBoard().getEmptyRoomPositions();
//                if (!emptyRooms.isEmpty()) {
//                    int[] selectedRoom = emptyRooms.get(0);
//                    FamilyMember newMember = player.getNewFamilyMember();
//                    player.placeFamilyMemberInRoom(newMember, selectedRoom[0], selectedRoom[1]);
//                    return true; // 성공 시 true 반환
//                } else {
//                    System.out.println("빈 방이 없습니다.");
//                    return false; // 빈 방이 없을 때 false 반환
//                }
//            } else {
//                System.out.println("빈 방이 없습니다.");
//                return false; // 빈 방이 없을 때 false 반환
//            }
//        } catch (Exception e) {
//            System.out.println("Exception during adding newborn: " + e.getMessage());
//            return false; // 예외 발생 시 false 반환
//        }
//    }
//
//    // 추가 효과 확인 및 적용 메서드
//    default void applyAdditionalEffects(Player player) {
//        // 추가 효과가 필요한 경우 오버라이드하여 구현
//    }
//}

