package com.example.agricola.cards.common;


import com.example.agricola.cards.majorimprovement.MajorImprovementCard;
import com.example.agricola.controller.GameController;
import com.example.agricola.enums.RoomType;
import com.example.agricola.models.*;
import com.example.agricola.service.GameService;


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

            // TODO 양 자원
            // 보드 외부에 먼저 배치가 됨.
            // 몇 마리 배치해야 하는지
            // 빈 공간을 알려주시고
            // 프론트가 어떤 좌표에 플레이어가 배치하고 싶어함.
            // 플레이어가 어떤 좌표에 동물 배치했는지 알려주시고(배치 할 수 있는지 없는지)
            // 배치를 못하는 경우

            if (resource.equals("sheep")) {
                System.out.println("양 자원 시작");
                // 양 자원일 경우
                for (int i = 0; i < amount; i++) {
                    // 양 객체를 추가하여 플레이어에게 배치 요청
                    Animal newSheep = new Animal(-1, -1, resource);
                    player.addNewAnimal(newSheep);
                }
                // 배치된 양의 수를 카운트하여 자원으로 추가
                int placedSheep = player.placeNewAnimals();
                System.out.println("placedSheep = " + placedSheep);
                player.addResource(resource, placedSheep);
            } else {
                // 일반 자원일 경우
                player.addResource(resource, amount);
            }
        }
    }


    /*
    * 프론트에게 사용 가능한 카드 목록 보여줌
    * 프론트가 구매한 카드명 보여줌
    * 구매했다는 걸 프론트에게 알려줌
    * */
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
        System.out.println("purchaseMajorImprovemetnCard 속 cardList");

        GameService gameService = player.getGameService();
        MainBoard mainBoard = gameService.getMainBoard();

        mainBoard.printCardLists();
        List<CommonCard> majorImprovementCards = mainBoard.getAvailableMajorImprovementCards();

        // TODO 플레이어가 카드를 선택하는 로직 (예시로 랜덤 선택)
        MajorImprovementCard selectedCard = null;
        if (!majorImprovementCards.isEmpty()) {
            selectedCard = (MajorImprovementCard) majorImprovementCards.get(new Random().nextInt(majorImprovementCards.size()));
        }

        if (selectedCard != null && player.checkResources(selectedCard.getPurchaseCost())) {
            player.payResources(selectedCard.getPurchaseCost());
            player.addMajorImprovementCard(selectedCard);
            selectedCard.setPurchased(true); // 카드가 구매되었음을 표시
            System.out.println(selectedCard.getName() + " 카드가 성공적으로 구매되었습니다.");
        } else {
            System.out.println("자원이 부족하거나 카드가 존재하지 않아 구매할 수 없습니다.");
        }

    }

    /*
    *
    * */
    default void triggerBreadBaking(Player player) {
        List<CommonCard> majorImprovementCards = player.getMajorImprovementCards();
        List<BakingCard> bakingCards = majorImprovementCards.stream()
                .filter(card -> card instanceof BakingCard)
                .map(card -> (BakingCard) card)
                .collect(Collectors.toList());

        if (!bakingCards.isEmpty()) {
            // TODO 플레이어가 카드를 선택하는 로직
            BakingCard selectedCard = player.selectBakingCard(bakingCards);
            selectedCard.triggerBreadBaking(player);
        } else {
            System.out.println("사용 가능한 설비가 없습니다.");
            // 빵굽기 가능한 설비가 없다는 메시지 표시
        }
    }

    // 기물 짓기


    /*
    * 집, 외양간, 밭
    * 한번에 기물만 생성
    *
    * 1. 프론트에게 선택 가능한 좌표를 보여주고
    * 2. 프론트가 좌표를 선택하면*/

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
                player.buildBarn(x, y);
            } else {
                System.out.println("외양간을 지을 수 없습니다.");
            }
        } else {
            System.out.println("외양간을 지을 수 있는 위치가 없습니다.");
        }
    }

//    // 울타리 짓기
//    // 프론트에게 좌표 요청
//    // 프론트가 좌표 set (1,1)(1,2)(1,3)
//    // 그거 갖고 울타리 지으면 됨
//    default void buildFence(Player player) {
//        // 플레이어가 울타리를 지을 수 있는 유효한 좌표를 가져옴.
//        Set<int[]> validPositions = player.getPlayerBoard().getValidFencePositions();
//        player.getPlayerBoard().printPlayerBoardWithFences("유효 좌표", validPositions);
//
//        // 지을 수 있는 좌표가 없으면 짓지 못함.
//        if (!validPositions.isEmpty()) {
//            List<int[]> selectedPositions = new ArrayList<>();
//
//            /*
//             * 프론트엔드가 한 칸의 좌표를 보내줄 때 마다 필요한 자원 계산 및 유효 좌표를 업데이트 하기 위한 변수
//             * 지을 좌표들을 모으기 위함.
//             * */
//
//            // TODO 좌표 무더기로 들어오면 펜스 짓기로. 하나씩 선택은 프론트에서 하기로
//            boolean fenceBuildingComplete = false;
//            while (!fenceBuildingComplete) {
//                // TODO 플레이어 좌표 입력 로직 (여기서는 유효 위치 중 하나를 선택하는 것으로 가정)
//                int[] position = validPositions.iterator().next();
//                selectedPositions.add(position);
//                validPositions = player.getPlayerBoard().getValidFencePositions();
//                // 유효 좌표가 없거나, 자원 부족등의 이유로 짓지 못하면 해당 좌표까지만 울타리를 지음
//                if (validPositions.isEmpty() || !player.canContinueFenceBuilding()) {
//                    fenceBuildingComplete = true;
//                }
//            }
//            // 선택된 좌표들의 모음으로 울타리를 지음
//            player.getPlayerBoard().buildFences(selectedPositions, player);
//            // 울타리 짓는데 필요한 자원을 차감
//            player.payResources(Map.of("wood", player.getPlayerBoard().calculateRequiredWoodForFences(selectedPositions)));
//            System.out.println("Fences built at: " + selectedPositions.stream().map(Arrays::toString).collect(Collectors.joining(", ")));
//        } else {
//            System.out.println("울타리를 지을 유효한 위치가 없습니다.");
//        }
//    }

    default void buildFence(Player player) {
        // 플레이어가 울타리를 지을 수 있는 유효한 좌표를 가져옴.
        Set<int[]> validPositions = player.getPlayerBoard().getValidFencePositions();
        player.getPlayerBoard().printPlayerBoardWithFences("유효 좌표", validPositions);

        // 지을 수 있는 좌표가 없으면 짓지 못함.
        if (!validPositions.isEmpty()) {
            List<int[]> selectedPositions = new ArrayList<>();

            boolean fenceBuildingComplete = false;
            while (!fenceBuildingComplete) {
                // 유효 위치 중 하나를 선택하는 로직 (가정)
                Iterator<int[]> iterator = validPositions.iterator();
                if (iterator.hasNext()) {
                    int[] position = iterator.next();
                    selectedPositions.add(position);
                    iterator.remove();  // 선택한 위치를 유효 위치에서 제거

                    // 유효 좌표 업데이트
                    validPositions = player.getPlayerBoard().getValidFencePositions();
                    validPositions.removeAll(selectedPositions);

                    // 선택된 좌표와 인접한 타일들만 남기기
                    validPositions.removeIf(pos -> !isAdjacent(selectedPositions.get(selectedPositions.size() - 1), pos));

                    // 유효 좌표가 없거나, 자원 부족 등의 이유로 짓지 못하면 해당 좌표까지만 울타리를 지음
                    if (validPositions.isEmpty() || !player.canContinueFenceBuilding()) {
                        fenceBuildingComplete = true;
                    }
                } else {
                    fenceBuildingComplete = true;
                }
            }

            // 선택된 좌표들의 모음으로 울타리를 지음
            player.getPlayerBoard().buildFences(selectedPositions, player);
            // 울타리 짓는데 필요한 자원을 차감
            player.payResources(Map.of("wood", player.getPlayerBoard().calculateRequiredWoodForFences(selectedPositions)));
            System.out.println("Fences built at: " + selectedPositions.stream().map(Arrays::toString).collect(Collectors.joining(", ")));
        } else {
            System.out.println("울타리를 지을 유효한 위치가 없습니다.");
        }
    }

    // 인접한 좌표인지 확인하는 메서드
    private boolean isAdjacent(int[] pos1, int[] pos2) {
        int dx = Math.abs(pos1[0] - pos2[0]);
        int dy = Math.abs(pos1[1] - pos2[1]);
        return (dx == 1 && dy == 0) || (dx == 0 && dy == 1);
    }







    // 곡식 심기 // 집, 외양간, 밭과 유사
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
        GameService gameService = player.getGameService();
        List<Player> turnOrder = gameService.getTurnOrder();

        int playerIndex = turnOrder.indexOf(player);
        if (playerIndex == -1) {
            throw new IllegalArgumentException("Player not found in the turn order.");
        }

        // 새로운 턴 오더 생성
        List<Player> newTurnOrder = new ArrayList<>();
        for (int i = playerIndex; i < turnOrder.size(); i++) {
            newTurnOrder.add(turnOrder.get(i));
        }
        for (int i = 0; i < playerIndex; i++) {
            newTurnOrder.add(turnOrder.get(i));
        }

        // 게임 컨트롤러에 새로운 턴 오더 설정
        gameService.setNextTurnOrder(newTurnOrder);
    }


    // 기물 변경

    // 집 고치기
    default void renovateRooms(Player player) {
        RoomType newType = player.chooseRoomTypeForRenovation();
        if (newType != null) {
            player.renovateHouse(newType);
        } else {
            System.out.println("더 이상 업그레이드할 수 있는 방이 없습니다.");
        }
    }


    // 객체 추가

    // 1. 가족 구성원 추가
    default boolean addNewborn(Player player) {
        if (player.addFamilyMember()) {
            // TODO 플레이어가 빈 방에 가족 구성원을 배치하는 로직 추가
            List<int[]> emptyRooms = player.getPlayerBoard().getEmptyRoomPositions();
            if (!emptyRooms.isEmpty()) {
                int[] selectedRoom = emptyRooms.get(0);
                FamilyMember newMember = player.getNewFamilyMember();
                player.placeFamilyMemberInRoom(newMember, selectedRoom[0], selectedRoom[1]);
                return true;
            } else {
                System.out.println("빈 방이 없습니다.");
            }
        } else {
            System.out.println("빈 방이 없습니다.");
        }
        return false;
    }


    // 추가 효과 확인 및 적용 메서드
    default void applyAdditionalEffects(Player player) {
        // 추가 효과가 필요한 경우 오버라이드하여 구현
    }

    public default boolean executesBuildOrRenovate() {
        boolean[] result = {false};

        // 기존 메서드들을 래핑하여 buildHouse 또는 renovateRooms 호출 여부 확인
        executeThen(new Player("dummy", "dummy", null),
                () -> { result[0] = true; },
                () -> {}
        );

        executeAndOr(new Player("dummy", "dummy", null),
                () -> { result[0] = true; },
                () -> {}
        );

        executeOr(new Player("dummy", "dummy", null),
                () -> { result[0] = true; },
                () -> {}
        );

        execute(new Player("dummy", "dummy", null));

        return result[0];
    }

}
