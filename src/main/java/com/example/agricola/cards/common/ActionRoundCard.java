package com.example.agricola.cards.common;


import com.example.agricola.cards.majorimprovement.MajorImprovementCard;
import com.example.agricola.enums.RoomType;
import com.example.agricola.models.*;
import com.example.agricola.service.GameService;

import java.util.*;
import java.util.concurrent.CountDownLatch;
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
        System.out.println("카드 액션 처리 통신 확인완");
        player.getGameService().sendPlayerResourcesToFrontEnd(player);
    }


    /*
    * 프론트에게 사용 가능한 카드 목록 보여줌
    * 프론트가 구매한 카드명 보여줌
    * 구매했다는 걸 프론트에게 알려줌
    * */
// 직업 카드 사용
    // 직업 카드 사용
    default void useOccupationCard(Player player) {
        GameService gameService = player.getGameService();
        List<CommonCard> occupationCards = player.getOccupationCards();
        if (!occupationCards.isEmpty()) {
            gameService.sendCardListToFrontEnd(occupationCards, player.getId());

            // CountDownLatch를 사용하여 대기
            CountDownLatch latch = new CountDownLatch(1);
            gameService.setLatch(player.getId(), latch);

            try {
                latch.await(); // 프론트엔드에서 신호를 받을 때까지 대기
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            UnifiedCard selectedCard = (UnifiedCard) gameService.getSelectedCard(player.getId());
            if (selectedCard != null) {
                player.useUnifiedCard(selectedCard);
                // 자원 차감 후 업데이트된 정보를 프론트엔드로 전송
                gameService.sendPlayerResourcesToFrontEnd(player);

                // 프론트엔드에 플레이어의 보유 major improvement cards를 전송
//                gameService.sendActiveCardsListToFrontEnd(player);
            } else {
                System.out.println("카드 선택이 취소되었습니다.");
            }
        } else {
            System.out.println("사용할 수 있는 직업 카드가 없습니다.");
        }
    }



//    // 보조 설비 카드 사용
//    default void useMinorImprovementCard(Player player) {
//        // 보조 설비 카드 사용 로직
//        List<CommonCard> minorImprovementCards = player.getMinorImprovementCards();
//        if (!minorImprovementCards.isEmpty()) {
//            UnifiedCard selectedCard = (UnifiedCard) minorImprovementCards.get(0);
//            player.useUnifiedCard(selectedCard);
//        }
//    }

    // 보조 설비 카드 사용
    default void useMinorImprovementCard(Player player) {
        GameService gameService = player.getGameService();
        List<CommonCard> minorImprovementCards = player.getMinorImprovementCards();
        if (!minorImprovementCards.isEmpty()) {
            gameService.sendCardListToFrontEnd(minorImprovementCards, player.getId());

            // CountDownLatch를 사용하여 대기
            CountDownLatch latch = new CountDownLatch(1);
            gameService.setLatch(player.getId(), latch);

            try {
                latch.await(); // 프론트엔드에서 신호를 받을 때까지 대기
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            UnifiedCard selectedCard = (UnifiedCard) gameService.getSelectedCard(player.getId());
            if (selectedCard != null) {
                System.out.println("보조설비 카드를 사용합니다.");
                player.useUnifiedCard(selectedCard);
                // 자원 차감 후 업데이트된 정보를 프론트엔드로 전송
                gameService.sendPlayerResourcesToFrontEnd(player);

                // 프론트엔드에 플레이어의 보유 major improvement cards를 전송
                gameService.sendActiveCardsListToFrontEnd(player);
            } else {
                System.out.println("카드 선택이 취소되었습니다.");
            }
        } else {
            System.out.println("사용할 수 있는 보조 설비 카드가 없습니다.");
        }
    }

    default void purchaseMajorImprovementCard(Player player) {
        GameService gameService = player.getGameService();
        System.out.println("purchaseMajorImprovementCard 속 cardList");

        MainBoard mainBoard = gameService.getMainBoard();
        List<CommonCard> majorImprovementCards = mainBoard.getAvailableMajorImprovementCards();

        if (!majorImprovementCards.isEmpty()) {
            gameService.sendCardListToFrontEnd(majorImprovementCards, player.getId());

            // CountDownLatch를 사용하여 대기
            CountDownLatch latch = new CountDownLatch(1);
            gameService.setLatch(player.getId(), latch);

            try {
                latch.await(); // 프론트엔드에서 신호를 받을 때까지 대기
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            MajorImprovementCard selectedCard = (MajorImprovementCard) gameService.getSelectedCard(player.getId());
            if (selectedCard != null && player.checkResources(selectedCard.getPurchaseCost())) {
//                player.payResources(selectedCard.getPurchaseCost());
                selectedCard.execute(player);
                player.addMajorImprovementCard(selectedCard);
                player.addCard(selectedCard, ".");
                selectedCard.setPurchased(true); // 카드가 구매되었음을 표시
                player.addCard(selectedCard, "majorimprovementcard");
                System.out.println(selectedCard.getName() + " 카드가 성공적으로 구매되었습니다.");

                // 자원 차감 후 업데이트된 정보를 프론트엔드로 전송
                gameService.sendPlayerResourcesToFrontEnd(player);

                // 프론트엔드에 플레이어의 보유 major improvement cards를 전송
                gameService.sendActiveCardsListToFrontEnd(player);
            } else {
                System.out.println("자원이 부족하거나 카드가 존재하지 않아 구매할 수 없습니다.");
            }
        } else {
            System.out.println("구매할 수 있는 주요 설비 카드가 없습니다.");
        }
    }



    /*
    *
    * */
    // 빵굽기 트리거
    default void triggerBreadBaking(Player player) {
        GameService gameService = player.getGameService();
        List<CommonCard> majorImprovementCards = player.getActiveCards();
        List<CommonCard> bakingCards = majorImprovementCards.stream()
                .filter(card -> card instanceof BakingCard && ((BakingCard) card).hasBreadBakingExchangeRate())
                .map(card -> (BakingCard) card)
                .collect(Collectors.toList());

        if (!bakingCards.isEmpty()) {
            gameService.sendCardListToFrontEnd(bakingCards, player.getId());
            // CountDownLatch를 사용하여 대기
            CountDownLatch latch = new CountDownLatch(1);
            gameService.setLatch(player.getId(), latch);

            try {
                latch.await(); // 프론트엔드에서 신호를 받을 때까지 대기
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            BakingCard selectedCard = (BakingCard) gameService.getSelectedCard(player.getId());
            if (selectedCard != null) {
                selectedCard.triggerBreadBaking(player);
            } else {
                System.out.println("카드 선택이 취소되었습니다.");
            }
        } else {
            System.out.println("사용 가능한 설비가 없습니다.");
        }
        player.getGameService().sendPlayerResourcesToFrontEnd(player);
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

        if (!validPositions.isEmpty()) {
            // 가능한 좌표를 프론트엔드로 전송
            GameService gameService = player.getGameService();
            String playerId = player.getId();
            gameService.sendValidPositionsToFrontEnd(playerId, validPositions, "house");

            // 선택된 좌표를 프론트엔드로부터 받아옴
            CountDownLatch latch = new CountDownLatch(1);
            gameService.setLatch(playerId, latch); // latch를 gameService에 저장

            try {
                latch.await(); // 프론트엔드에서 신호를 받을 때까지 대기
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int[] selectedPosition = gameService.getSelectedPosition(playerId);
            int x = selectedPosition[0];
            int y = selectedPosition[1];

            System.out.println("Selected position: (" + x + ", " + y + ")");

            // 기존 집의 타입을 가져옴
            RoomType currentRoomType = playerBoard.getExistingRoomType();

            if (currentRoomType == null) {
                System.out.println("기존 집 타입을 찾을 수 없습니다.");
                return;
            }
            System.out.println("Current room type: " + currentRoomType);

            if (playerBoard.canBuildHouse(x, y, currentRoomType, playerResources)) {
                Map<String, Integer> cost = player.getHouseResourceCost(currentRoomType);
                if (player.checkResources(cost)) {
                    player.payResources(cost);
                    player.getPlayerBoard().buildHouse(x, y, currentRoomType);
                    System.out.println("House built at (" + x + ", " + y + ") with room type: " + currentRoomType);

                } else {
                    // 자원이 부족하다는 메시지 표시
                    System.out.println("자원이 부족합니다.");
                }
            } else {
                // 집을 지을 수 없는 조건이라는 메시지 표시
                System.out.println("집을 지을 수 없습니다.");
            }
        } else {
            // 유효한 건축 위치가 없다는 메시지 표시
            System.out.println("유효한 건축 위치가 없습니다.");
        }
    }



    // 밭 일구기
    default void plowField(Player player) {

        PlayerBoard playerBoard = player.getPlayerBoard();

        // 가능한 좌표를 가져옴
        Set<int[]> validPositions = playerBoard.getValidPlowPositions();

        if (!validPositions.isEmpty()) {
            // 가능한 좌표를 프론트엔드로 전송
            GameService gameService = player.getGameService();
            String playerId = player.getId();
            gameService.sendValidPositionsToFrontEnd(playerId, validPositions, "plow");

            // 선택된 좌표를 프론트엔드로부터 받아옴
            CountDownLatch latch = new CountDownLatch(1);
            gameService.setLatch(playerId, latch); // latch를 gameService에 저장
            try {
                latch.await(); // 프론트엔드에서 신호를 받을 때까지 대기
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int[] selectedPosition = gameService.getSelectedPosition(playerId);
            int x = selectedPosition[0];
            int y = selectedPosition[1];

            playerBoard.plowField(x, y);
            System.out.println("Plowed field at (" + x + ", " + y + ")");
        } else {
            System.out.println("밭을 일굴 수 있는 위치가 없습니다.");
        }
    }



    // 외양간 짓기
    default void buildBarn(Player player) {
        PlayerBoard playerBoard = player.getPlayerBoard();

        // 가능한 좌표를 가져옴
        Set<int[]> validPositions = playerBoard.getValidBarnPositions();

        if (!validPositions.isEmpty()) {
            // 가능한 좌표를 프론트엔드로 전송
            GameService gameService = player.getGameService();
            String playerId = player.getId();
            gameService.sendValidPositionsToFrontEnd(playerId, validPositions, "barn");

            // 선택된 좌표를 프론트엔드로부터 받아옴
            CountDownLatch latch = new CountDownLatch(1);
            gameService.setLatch(playerId, latch); // latch를 gameService에 저장

            System.out.println("프론트엔드에게 좌표 전송");
            try {
                latch.await(); // 프론트엔드에서 신호를 받을 때까지 대기
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int[] selectedPosition = gameService.getSelectedPosition(playerId);
            if (selectedPosition == null) {
                System.out.println("No position selected for barn.");
                return;
            }
            System.out.println("프론트엔드에게 좌표 받음");

            int x = selectedPosition[0];
            int y = selectedPosition[1];

            if (playerBoard.canBuildBarn(x, y)) {
                player.buildBarn(x, y);
                System.out.println("Barn built at (" + x + ", " + y + ")");
            } else {
                System.out.println("Cannot build barn at (" + x + ", " + y + ")");
            }
        } else {
            System.out.println("No valid positions for barn.");
        }
    }


    // 울타리 짓기
    default void buildFence(Player player) {
        // 플레이어가 울타리를 지을 수 있는 유효한 좌표를 가져옴.
        PlayerBoard playerBoard = player.getPlayerBoard();
        GameService gameService = player.getGameService();
        String playerId = player.getId();

        // 프론트엔드에 울타리 요청을 보냄
        gameService.sendFenceRequestToFrontEnd(playerId);

        // 프론트엔드로부터 선택된 좌표를 받아옴
        CountDownLatch latch = new CountDownLatch(1);
        gameService.setLatch(playerId, latch);

        try {
            latch.await(); // 프론트엔드에서 신호를 받을 때까지 대기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<int[]> selectedPositions = gameService.getSelectedFencePositions(playerId);

        if (selectedPositions != null && !selectedPositions.isEmpty()) {
            // 선택된 좌표들의 모음으로 울타리를 지음
            playerBoard.buildFences(selectedPositions, player);
            // 울타리 짓는데 필요한 자원을 차감
            player.payResources(Map.of("wood", playerBoard.calculateRequiredWoodForFences(selectedPositions)));
            System.out.println("Fences built at: " + selectedPositions.stream().map(Arrays::toString).collect(Collectors.joining(", ")));
        } else {
            System.out.println("울타리를 지을 유효한 위치가 없습니다.");
        }
    }








    // 곡식 심기
    default void plantField(Player player) {
        PlayerBoard playerBoard = player.getPlayerBoard();
        GameService gameService = player.getGameService();
        String playerId = player.getId();

        String cropType = "grain"; // 플레이어 선택에 따라 "grain" 또는 "vegetable"

        // 곡식을 심을 수 있는 유효한 좌표를 가져옴
        Set<int[]> validPositions = playerBoard.getValidFieldPositions();

        if (!validPositions.isEmpty()) {
            // 프론트엔드에 유효한 좌표를 보냄
            gameService.sendValidPositionsToFrontEnd(playerId, validPositions, "plantField");

            // 프론트엔드로부터 선택된 좌표를 받아옴
            CountDownLatch latch = new CountDownLatch(1);
            gameService.setLatch(playerId, latch);

            try {
                latch.await(); // 프론트엔드에서 신호를 받을 때까지 대기
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int[] selectedPosition = gameService.getSelectedPosition(playerId);

            if (selectedPosition != null) {
                int x = selectedPosition[0];
                int y = selectedPosition[1];

                int initialCrops = cropType.equals("grain") ? 3 : 2;

                playerBoard.plantField(x, y, initialCrops, cropType);
                Map<String, Integer> cost = Collections.singletonMap(cropType, 1);
                player.payResources(cost);
            } else {
                System.out.println("좌표를 선택하지 않았습니다.");
            }
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
        
        for (Player p : newTurnOrder) {
            System.out.println("p.getId() = " + p.getId());
        }

        // 게임 컨트롤러에 새로운 턴 오더 설정
        gameService.setNextTurnOrder(newTurnOrder);

        // 프론트엔드에 선 플레이어가 변경되었다는 정보를 전송
        gameService.sendTurnOrderInfo(player.getId(), turnOrder, newTurnOrder);
    }




    // 기물 변경

    // 집 고치기
    default void renovateRooms(Player player) {
        RoomType newType = player.chooseRoomTypeForRenovation();
        if (newType != null) {
            player.renovateHouse(newType);

            // 프론트엔드에 방이 업그레이드되었다는 정보를 전송
            GameService gameService = player.getGameService();
            gameService.sendRenovationInfo(player.getId(), newType);
        } else {
            System.out.println("더 이상 업그레이드할 수 있는 방이 없습니다.");
        }
    }



    // 객체 추가

    // 1. 가족 구성원 추가
    default boolean addNewborn(Player player) {
        if (player.addFamilyMember()) {
            List<int[]> emptyRooms = player.getPlayerBoard().getEmptyRoomPositions();
            if (!emptyRooms.isEmpty()) {
                int[] selectedRoom = emptyRooms.get(0);
                FamilyMember newMember = player.getNewFamilyMember();
                player.placeFamilyMemberInRoom(newMember, selectedRoom[0], selectedRoom[1]);

                // 프론트엔드에 가족 구성원이 추가된 위치를 전송
                GameService gameService = player.getGameService();
                gameService.sendFamilyMemberPosition(player.getId(), selectedRoom[0], selectedRoom[1]);

                return true;
            } else {
                System.out.println("빈 방이 없습니다.");
            }
        } else {
            System.out.println("빈 방이 없습니다.");
        }

        // 프론트엔드에 추가하지 못했다는 메시지를 전송
        GameService gameService = player.getGameService();
        gameService.sendFamilyMemberPosition(player.getId(), -1, -1);

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
