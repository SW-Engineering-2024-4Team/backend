package cards.common;

import cards.majorimprovement.MajorImprovementCard;
import controllers.GameController;
import enums.RoomType;
import models.*;

import java.util.*;
import java.util.stream.Collectors;

public interface ActionRoundCard extends CommonCard {

    boolean isRevealed(); // RoundCard와 ActionCard 구분을 위해 추가
    void reveal(); // 라운드 카드를 공개하는 메서드
    boolean isAccumulative(); // 누적 가능한지 여부 확인 메서드
    boolean isOccupied(); // 카드가 점유되었는지 확인
    void setOccupied(boolean occupied); // 카드의 점유 상태 설정

    default void gainResources(Player player, Map<String, Integer> resources) {
        for (Map.Entry<String, Integer> entry : resources.entrySet()) {
            String resource = entry.getKey();
            int amount = entry.getValue();

            if (resource.equals("sheep")) {
                // 양 자원일 경우
                for (int i = 0; i < amount; i++) {
                    // 양 객체를 추가하여 플레이어에게 배치 요청
                    Animal newSheep = new Animal(-1, -1, resource);
                    player.addNewAnimal(newSheep);
                }
                // 배치된 양의 수를 카운트하여 자원으로 추가
                int placedSheep = player.placeNewAnimals();
                player.addResource(resource, placedSheep);
            } else {
                // 일반 자원일 경우
                player.addResource(resource, amount);
            }
        }
    }


    // 직업 카드 사용
    default void useOccupationCard(Player player) {
        // 직업 카드 사용 로직
        List<CommonCard> occupationCards = player.getOccupationCards();
        if (!occupationCards.isEmpty()) {
            // TODO 플레이어가 카드를 선택하는 로직 (예시로 첫 번째 카드를 선택했다고 가정)
            UnifiedCard selectedCard = (UnifiedCard) occupationCards.get(0);
            player.useUnifiedCard(selectedCard);
        }
    }

    // 보조 설비 카드 사용
    default void useMinorImprovementCard(Player player) {
        // 보조 설비 카드 사용 로직
        List<CommonCard> minorImprovementCards = player.getMinorImprovementCards();
        if (!minorImprovementCards.isEmpty()) {
            // TODO 플레이어가 카드를 선택하는 로직 (예시로 첫 번째 카드를 선택했다고 가정)
            UnifiedCard selectedCard = (UnifiedCard) minorImprovementCards.get(0);
            player.useUnifiedCard(selectedCard);
        }
    }

    // 주요 설비 카드 구매
    default void purchaseMajorImprovementCard(Player player) {
        GameController gameController = player.getGameController();
        MainBoard mainBoard = gameController.getMainBoard();
        List<CommonCard> majorImprovementCards = mainBoard.getMajorImprovementCards();

        // TODO 플레이어가 카드를 선택하는 로직 (예시로 첫 번째 카드를 선택했다고 가정)
        MajorImprovementCard selectedCard = null;
        for (CommonCard card : majorImprovementCards) {
            if (card instanceof MajorImprovementCard) {
                selectedCard = (MajorImprovementCard) card;
                break;
            }
        }
        String cardName = selectedCard.getName();

        MajorImprovementCard cardToPurchase = null;
        for (CommonCard card : majorImprovementCards) {
            if (card.getName().equals(cardName) && card instanceof MajorImprovementCard) {
                cardToPurchase = (MajorImprovementCard) card;
                break;
            }
        }

        if (cardToPurchase != null && player.checkResources(cardToPurchase.getPurchaseCost())) {
            player.payResources(cardToPurchase.getPurchaseCost());
            player.addMajorImprovementCard(cardToPurchase);
            mainBoard.removeMajorImprovementCard(cardToPurchase);
            System.out.println(cardName + " 카드가 성공적으로 구매되었습니다.");
        } else {
            System.out.println("자원이 부족하거나 카드가 존재하지 않아 구매할 수 없습니다.");
        }
    }
    default void triggerBreadBaking(Player player) {
        List<CommonCard> majorImprovementCards = player.getMajorImprovementCards();
        List<BakingCard> bakingCards = majorImprovementCards.stream()
                .filter(card -> card instanceof BakingCard)
                .map(card -> (BakingCard) card)
                .collect(Collectors.toList());

        if (!bakingCards.isEmpty()) {
            BakingCard selectedCard = player.selectBakingCard(bakingCards);
            selectedCard.triggerBreadBaking(player);
        } else {
            // 빵굽기 가능한 설비가 없다는 메시지 표시
        }
    }

    // 기물 짓기

    // 집 짓기
    default void buildHouse(Player player) {
        PlayerBoard playerBoard = player.getPlayerBoard();
        Map<String, Integer> playerResources = player.getResources(); // 플레이어의 자원을 가져옴

        // 가능한 좌표를 가져옴
        Set<int[]> validPositions = playerBoard.getValidHousePositions();

        // TODO: 플레이어가 좌표를 선택하는 로직 (예시로 첫 번째 유효한 좌표를 선택했다고 가정)
        int[] selectedPosition = validPositions.iterator().next();
        int x = selectedPosition[0];
        int y = selectedPosition[1];

        // 기존 집의 타입을 가져옴
        RoomType currentRoomType = playerBoard.getExistingRoomType();

        if (currentRoomType == null) {
            System.out.println("기존 집 타입을 찾을 수 없습니다.");
            return;
        }

        if (playerBoard.canBuildHouse(x, y, currentRoomType, playerResources)) {
            Map<String, Integer> cost = player.getHouseResourceCost(currentRoomType);
            if (player.checkResources(cost)) {
                player.payResources(cost);
                player.getPlayerBoard().buildHouse(x, y, currentRoomType);
            } else {
                // 자원이 부족하다는 메시지 표시
                System.out.println("자원이 부족합니다.");
            }
        } else {
            // 집을 지을 수 없는 조건이라는 메시지 표시
            System.out.println("집을 지을 수 없습니다.");
        }
    }

    // 밭 일구기
    default void plowField(Player player) {

        PlayerBoard playerBoard = player.getPlayerBoard();

        // 가능한 좌표를 가져옴
        Set<int[]> validPositions = playerBoard.getValidPlowPositions();

        // TODO: 플레이어가 좌표를 선택하는 로직 (예시로 첫 번째 유효한 좌표를 선택했다고 가정)
        if (!validPositions.isEmpty()) {
            int[] selectedPosition = validPositions.iterator().next();
            int x = selectedPosition[0];
            int y = selectedPosition[1];

            playerBoard.plowField(x, y);
        } else {
            System.out.println("밭을 일굴 수 있는 위치가 없습니다.");
        }
    }

    // 외양간 짓기
    default void buildBarn(Player player) {

        PlayerBoard playerBoard = player.getPlayerBoard();

        // 가능한 좌표를 가져옴
        Set<int[]> validPositions = playerBoard.getValidBarnPositions();

        // TODO: 플레이어가 좌표를 선택하는 로직 (예시로 첫 번째 유효한 좌표를 선택했다고 가정)
        if (!validPositions.isEmpty()) {
            int[] selectedPosition = validPositions.iterator().next();
            int x = selectedPosition[0];
            int y = selectedPosition[1];

            if (playerBoard.canBuildBarn(x, y)) {
                playerBoard.buildBarn(x, y);
            } else {
                System.out.println("외양간을 지을 수 없습니다.");
            }
        } else {
            System.out.println("외양간을 지을 수 있는 위치가 없습니다.");
        }

    }

    // 울타리 짓기
    default void buildFence(Player player) {
        Set<int[]> validPositions = player.getPlayerBoard().getValidFencePositions();
        if (!validPositions.isEmpty()) {
            boolean fenceBuildingComplete = false;
            while (!fenceBuildingComplete) {
                // TODO 플레이어 좌표 입력 로직
                // 예시로 첫 번째 유효 위치 선택
                int[] position = validPositions.iterator().next();
                if (player.selectFenceTile(position[0], position[1])) {
                    validPositions = player.getPlayerBoard().getValidFencePositions();
                    if (validPositions.isEmpty() || !player.canContinueFenceBuilding()) {
                        fenceBuildingComplete = true;
                    }
                } else {
                    fenceBuildingComplete = true;
                }
            }
            player.finalizeFenceBuilding();
        } else {
            System.out.println("울타리를 지을 유효한 위치가 없습니다.");
        }
    }


    // 곡식 심기
    default void plantField(Player player) {
        PlayerBoard playerBoard = player.getPlayerBoard();

        // TODO: 플레이어가 곡식이나 야채를 선택하는 로직 (예시로 곡식을 선택했다고 가정)
        String cropType = "grain"; // 플레이어 선택에 따라 "grain" 또는 "vegetable"

        // TODO: 플레이어가 밭 위치를 선택하는 로직 (예시로 첫 번째 유효한 밭을 선택했다고 가정)
        Set<int[]> validFieldPositions = playerBoard.getValidFieldPositions();
        if (!validFieldPositions.isEmpty()) {
            int[] selectedPosition = validFieldPositions.iterator().next();
            int x = selectedPosition[0];
            int y = selectedPosition[1];

            int initialCrops = cropType.equals("grain") ? 3 : 2;

            playerBoard.plantField(x, y, initialCrops, cropType);
            Map<String, Integer> cost = Collections.singletonMap(cropType, 1);
            player.payResources(cost);
        } else {
            System.out.println("곡식이나 야채를 심을 수 있는 위치가 없습니다.");
        }
    }

    // 선 플레이어 되기
    default void becomeFirstPlayer(Player player) {
        GameController gameController = player.getGameController();
        List<Player> turnOrder = gameController.getTurnOrder();

        // 기존 선 플레이어를 찾음
        for (Player p : turnOrder) {
            if (p.isFirstPlayer()) {
                p.setFirstPlayer(false);
                break;
            }
        }

        // 새로운 선 플레이어 설정
        player.setFirstPlayer(true);

        // 새로운 턴 오더 생성
        List<Player> newTurnOrder = new ArrayList<>();
        newTurnOrder.add(player);
        for (Player p : turnOrder) {
            if (!p.equals(player)) {
                newTurnOrder.add(p);
            }
        }

        // 게임 컨트롤러에 새로운 턴 오더 설정
        gameController.setNextTurnOrder(newTurnOrder);
    }


    // 객체 추가

    // 1. 가족 구성원 추가
    default void addNewborn(Player player) {

        player.addFamilyMember(); // 가족 구성원을 추가
        // 플레이어가 빈 방에 가족 구성원을 배치하는 로직 추가
        List<int[]> emptyRooms = player.getPlayerBoard().getEmptyRoomPositions();
        if (!emptyRooms.isEmpty()) {
            // TODO: 플레이어가 빈 방을 선택하는 로직 (예시로 첫 번째 빈 방을 선택했다고 가정)
            int[] selectedRoom = emptyRooms.get(0);
            FamilyMember newMember = player.getNewFamilyMember();
            player.placeFamilyMemberInRoom(newMember, selectedRoom[0], selectedRoom[1]);
        } else {
            System.out.println("빈 방이 없습니다.");
        }
    }

    // 기물 변경

    // 집 고치기
    default void renovateRooms(Player player) {
        RoomType newType = player.chooseRoomTypeForRenovation();
        player.renovateHouse(newType);
    }

    // 추가 효과 확인 및 적용 메서드
    default void applyAdditionalEffects(Player player) {
        // 추가 효과가 필요한 경우 오버라이드하여 구현
    }
}
