package com.example.agricola.service;

import com.example.agricola.cards.common.AccumulativeCard;
import com.example.agricola.cards.factory.imp.major.*;
import com.example.agricola.cards.factory.imp.minor.HardenedClay;
import com.example.agricola.cards.factory.imp.occupation.ShepherdCard;
import com.example.agricola.controller.CardController;
import com.example.agricola.models.*;
import com.example.agricola.cards.common.ActionRoundCard;
import com.example.agricola.cards.common.CommonCard;
import com.example.agricola.cards.common.ExchangeableCard;
import com.example.agricola.enums.ExchangeTiming;
import com.example.agricola.enums.RoomType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

@Service
public class GameService {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private void notifyPlayers(Map<String, Object> message) {
        System.out.println("Sending message to players: " + message); // 디버깅 메시지 추가
        simpMessagingTemplate.convertAndSend("/topic/game", message);
    }

    private void notifyPlayers(String message) {
        System.out.println("Sending message to players: " + message); // 디버깅 메시지 추가
        simpMessagingTemplate.convertAndSend("/topic/game", message);
    }

    public void notifyPlayer(Player player, Map<String, Object> message) {
        System.out.println("Sending message to player " + player.getId() + ": " + message); // 디버깅 메시지 추가
        simpMessagingTemplate.convertAndSend("/topic/game", message);
    }


    private String gameID;
    private List<Player> players;
    private List<Player> turnOrder;
    private List<Player> nextTurnOrder;
    private MainBoard mainBoard;
    private CardController cardController;
    private int currentRound;
    private final Object nextPhaseLock = new Object();
    private Set<String> readyPlayers = new HashSet<>();
    private Map<String, CommonCard> selectedCards = new ConcurrentHashMap<>(); // 맵 초기화
    private List<Integer> accumulatedResources = new ArrayList<>(Collections.nCopies(14, 0));
    private List<Integer> playerPositions = new ArrayList<>(Collections.nCopies(20, null));




    public void startGame(String gameID, List<Player> players) {
        this.gameID = gameID;
        this.players = players;
        this.mainBoard = new MainBoard();
        this.cardController = CardController.getInstance();
        this.currentRound = 1;
        this.turnOrder = new ArrayList<>(players);
        Collections.shuffle(this.turnOrder);
        initializeFirstFoods();

        System.out.println("Game started with ID: " + gameID);
        System.out.println("Players: " + players);
        System.out.println("MainBoard: " + mainBoard);

        for (Player player : players) {
            player.setGameService(this);
        }
        initializeGame();
    }

    private void initializeFirstFoods() {
        for (int i = 0; i < turnOrder.size(); i++) {
            Player player = turnOrder.get(i);
            if (i == 0) {
                player.addResource("food", 2);
                player.setFirstPlayer(true);
            } else {
                player.addResource("food", 3);
            }
        }
    }

    public void endGame() {
        System.out.println("Game ended with ID: " + gameID);
        this.gameID = null;
        this.players = null;
        this.turnOrder = null;
        this.nextTurnOrder = null;
        this.mainBoard = null;
        this.currentRound = 0;
    }

    public void initializeGame() {
        setupPlayers();
        setupMainBoard();
    }

    private void setupPlayers() {
        List<CommonCard> occupationDeck = cardController.getDeck("occupationCards");
        List<CommonCard> minorImprovementDeck = cardController.getDeck("minorImprovementCards");
        cardController.shuffleDeck(occupationDeck);
        cardController.shuffleDeck(minorImprovementDeck);

        int numPlayers = players.size();

//        for (int i = 0; i < 8; i++) {
//            players.get(i % numPlayers).addCard(occupationDeck.get(i), "occupation");
//        }
//
//        for (int i = 0; i < 8; i++) {
//            players.get(i % numPlayers).addCard(minorImprovementDeck.get(i), "minorImprovement");
//        }

        // TODO 테스트 셋업 직업카드:양보행자(ShepherdCard(42) index 7) 보조설비:경질자기(HardenedClay(46) index4)
        // 보조설비 다 갖는걸로

        for (Player player : players) {
            player.addCard(new ShepherdCard(42), ".");
            player.addCard(new HardenedClay(46), ".");
            player.addCard(new PotteryWorkshop(29), ".");
            player.addCard(new Hearth1(30), ".");
            player.addCard(new FurnitureWorkshop(31), ".");
            player.addCard(new StoneOven(32), ".");
            player.addCard(new Hearth2(33), ".");
            player.addCard(new ClayOven(34), ".");
        }
    }

    private void setupMainBoard() {
        List<ActionRoundCard> actionCards = cardController.getActionRoundCards();
        List<List<ActionRoundCard>> roundCycles = cardController.getShuffledRoundCardsByCycle();
        List<CommonCard> majorImprovementCards = cardController.getDeck("majorImprovementCards");

        mainBoard.initializeBoard(actionCards, roundCycles, majorImprovementCards);
    }

    public Map<String, Object> getGameState() {
        Map<String, Object> mainBoardState = Map.of(
                "cards", mainBoard.getAllCards().stream()
                        .map(card -> Map.of(
                                "id", card.getId(),
                                "name", card.getName(),
                                "description", card.getDescription()
                        ))
                        .collect(Collectors.toList())
        );

        List<Map<String, Object>> playersState = players.stream()
                .map(player -> Map.of(
                        "id", player.getId(),
                        "name", player.getName(),
                        "resources", player.getResources(),
                        "occupationCards", player.getOccupationCards().stream().map(card -> card.toMap()).collect(Collectors.toList()),
                        "minorImprovementCards", player.getMinorImprovementCards().stream().map(card -> card.toMap()).collect(Collectors.toList()),
                        "activeCards", player.getActiveCards().stream().map(card -> card.toMap()).collect(Collectors.toList()),
                        "playerBoard", Map.of(
                                "tiles", player.getPlayerBoard().getTiles(),
                                "fences", player.getPlayerBoard().getFences(),
                                "familyMembers", player.getPlayerBoard().getFamilyMembers(),
                                "animals", player.getPlayerBoard().getAnimals(),
                                "fenceAreas", player.getPlayerBoard().getManagedFenceAreas().stream().map(FenceArea::toMap).collect(Collectors.toList())
                        )
                ))
                .collect(Collectors.toList());

        return Map.of(
                "gameID", gameID,
                "currentRound", currentRound,
                "mainBoard", mainBoardState,
                "players", playersState
        );
    }

    public void setNextTurnOrder(List<Player> nextTurnOrder) {
        System.out.println("nextturnorder 호출됨");
        this.nextTurnOrder = nextTurnOrder;
    }

    public List<Player> getTurnOrder() {
        return turnOrder;
    }

    public void playGame(String gameID) {
        System.out.println("playGame method called with gameID: " + gameID); // 디버깅 메시지 추가

        while (currentRound <= 14) {
            System.out.println("-------------------------------------------------------------------------------");
            System.out.println("Round " + currentRound + " starts.");
//            prepareRound();
            System.out.println("after preparing round " + currentRound);
//            playRound();
            System.out.println("after playing round " + currentRound);
            if (isHarvestRound(currentRound)) {
                harvestPhase();
            }
            endRound();
            System.out.println("Round " + currentRound + " ends.");
            System.out.println("-------------------------------------------------------------------------------");
            if (currentRound == 5) {
                currentRound = 14;
                continue;
            }
            currentRound++;
        }
        endGame();
    }


    private void prepareRound() {
        mainBoard.revealRoundCard(currentRound);
        mainBoard.accumulateResources();

        // 현재 라운드 정보 수집
        Map<String, Object> roundInfo = new HashMap<>();
        roundInfo.put("currentRound", currentRound);

        // 오픈된 카드 정보 수집
        List<Map<String, Object>> openedCards = mainBoard.getRevealedRoundCards().stream()
                .map(card -> (Map<String, Object>) Map.of(
                        "id", (Object) card.getId(),
                        "name", (Object) card.getName()
//                        "description", (Object) card.getDescription()
                )).collect(Collectors.toList());
        roundInfo.put("openedCards", openedCards);

        // 누적된 자원 정보 수집
        List<Map<String, Object>> accumulatedResources = mainBoard.getActionCards().stream()
                .filter(card -> card instanceof AccumulativeCard)
                .map(card -> Map.of(
                        "id", card.getId(),
                        "name", card.getName(),
//                        "description", card.getDescription(),
                        "accumulatedResources", ((AccumulativeCard) card).getAccumulatedResources()
                )).collect(Collectors.toList());
        roundInfo.put("accumulatedResources", accumulatedResources);

        // 턴 오더 정보 수집
        List<String> turnOrderInfo = turnOrder.stream()
                .map(Player::getName)
                .collect(Collectors.toList());
        roundInfo.put("turnOrder", turnOrderInfo);

        // 플레이어에게 알림
        notifyPlayers(roundInfo);

        if (nextTurnOrder != null && !nextTurnOrder.isEmpty()) {
            turnOrder = new ArrayList<>(nextTurnOrder);
            nextTurnOrder.clear();
        }
    }

    public void sendExchangeableCardsInfoToFrontEnd(String playerId, ExchangeTiming timing) {
        Player player = getPlayerByID(playerId);
        if (player != null) {
            Map<String, Object> exchangeableCardsInfo = getExchangeableCardsInfo(player, timing);
            simpMessagingTemplate.convertAndSend("/topic/exchangeableCards", exchangeableCardsInfo);
        }
    }


//    private Map<String, Object> getExchangeableCardsInfo(Player player, ExchangeTiming timing) {
//        List<ExchangeableCard> exchangeableCards = player.getExchangeableCards(timing);
//        return Map.of(
//                "playerId", player.getId(),
//                "exchangeableCards", exchangeableCards.stream()
//                        .filter(card -> {
//                            // 자원이 충분한지 확인
//                            Map<String, Integer> exchangeRate = card.getExchangeRate();
//                            if (exchangeRate == null) {
//                                return false;
//                            }
//                            for (Map.Entry<String, Integer> entry : exchangeRate.entrySet()) {
//                                if (player.getResource(entry.getKey()) < entry.getValue()) {
//                                    return false;
//                                }
//                            }
//                            return true;
//                        })
//                        .map(card -> {
//                            if (card instanceof CommonCard) {
//                                CommonCard commonCard = (CommonCard) card;
//                                // 교환 가능한 최대 값 계산
//                                Map<String, Integer> exchangeRate = card.getExchangeRate();
//                                int maxExchangeAmount = Integer.MAX_VALUE;
//                                if (exchangeRate != null) {
//                                    for (Map.Entry<String, Integer> entry : exchangeRate.entrySet()) {
//                                        int availableAmount = player.getResource(entry.getKey()) / entry.getValue();
//                                        maxExchangeAmount = Math.min(maxExchangeAmount, availableAmount);
//                                    }
//                                }
//                                return Map.of(
//                                        "id", commonCard.getId(),
//                                        "name", commonCard.getName(),
//                                        "exchangeRate", exchangeRate,
//                                        "maxExchangeAmount", maxExchangeAmount
//                                );
//                            } else {
//                                return Map.of(); // 기본값
//                            }
//                        })
//                        .collect(Collectors.toList())
//        );
//    }

    private Map<String, Object> getExchangeableCardsInfo(Player player, ExchangeTiming timing) {
        List<ExchangeableCard> exchangeableCards = player.getExchangeableCards(timing);
        return Map.of(
                "playerId", player.getId(),
                "exchangeableCards", exchangeableCards.stream()
                        .filter(card -> {
                            // 자원이 충분한지 확인
                            Map<String, Integer> exchangeRate = card.getExchangeRate();
                            if (exchangeRate == null) {
                                return false;
                            }
                            for (Map.Entry<String, Integer> entry : exchangeRate.entrySet()) {
                                if (player.getResource(entry.getKey()) < entry.getValue()) {
                                    return false;
                                }
                            }
                            return true;
                        })
                        .map(card -> {
                            if (card instanceof CommonCard) {
                                CommonCard commonCard = (CommonCard) card;
                                // 교환 가능한 최대 값 계산
                                Map<String, Integer> exchangeRate = card.getExchangeRate();
                                int maxExchangeAmount = Integer.MAX_VALUE;
                                if (exchangeRate != null) {
                                    for (Map.Entry<String, Integer> entry : exchangeRate.entrySet()) {
                                        int availableAmount = player.getResource(entry.getKey()) / entry.getValue();
                                        maxExchangeAmount = Math.min(maxExchangeAmount, availableAmount);
                                    }
                                }
                                return Map.of(
                                        "id", commonCard.getId(),
                                        "name", commonCard.getName(),
                                        "exchangeRate", exchangeRate,
                                        "maxExchangeAmount", maxExchangeAmount
                                );
                            } else {
                                return Map.of(); // 기본값
                            }
                        })
                        .collect(Collectors.toList())
        );
    }





    private void playRound() {

        boolean roundFinished = false;
        while (!roundFinished) {
            roundFinished = true;
            for (Player player : turnOrder) {
                if (player.hasAvailableFamilyMembers()) {
                    List<ActionRoundCard> availableCards = new ArrayList<>(mainBoard.getActionCards());
                    availableCards.addAll(mainBoard.getRevealedRoundCards());
                    availableCards.removeIf(card -> mainBoard.isCardOccupied(card));

                    if (!availableCards.isEmpty()) {
                        roundFinished = false;

                        // 플레이어 턴 진행
                        playerTurn(player.getId(), availableCards);
                        printFamilyMembersOnBoard();
                        player.getActiveCards();
                    }
                }
            }
        }
        resetFamilyMembers();
    }

    // 교환 요청을 처리하는 메서드
    public void handleExchangeRequest(String playerID, int cardId) {
        Player player = getPlayerByID(playerID);
        ExchangeableCard card = (ExchangeableCard) player.getActiveCards()
                .stream()
                .filter(c -> c.getId() == cardId)
                .findFirst()
                .orElse(null);

        System.out.println("(CommonCard) card = " + (CommonCard) card);

        if (card != null && (card.canExchange(ExchangeTiming.ANYTIME) || card.canExchange(ExchangeTiming.HARVEST))) {
            card.executeExchange(player);
        }
        sendPlayerResourcesToFrontEnd(player);
    }



    private void playerTurn(String playerID, List<ActionRoundCard> availableCards) {
        Player player = getPlayerByID(playerID);

        if (player != null) {
            sendExchangeableCardsInfoToFrontEnd(playerID, ExchangeTiming.ANYTIME);
            player.printActiveCardsLists();
            // 플레이어에게 턴 정보를 프론트엔드로 전송
            Map<String, Object> playerTurnInfo = Map.of(
                    "playerId", player.getId(),
                    "availableCards", availableCards.stream()
                            .map(card -> Map.of(
                                    "id", card.getId(),
                                    "name", card.getName()
//                                    "description", card.getDescription()
                            ))
                            .collect(Collectors.toList()),
                    "message", " {\"playerID\": \"플레이어 ID\", \"cardId\": \"선택한 카드 아이디\""
            );
            notifyPlayer(player, playerTurnInfo);

            // 플레이어의 턴을 기다림
            synchronized (this) {
                try {
                    wait(); // 프론트엔드에서 신호를 받을 때까지 대기
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            player.printActiveCardsLists();

        }
    }

//    // 프론트엔드에서 플레이어의 턴 정보를 받아오는 메서드
//    public void receivePlayerTurn(String playerID, int cardID) {
//        Player player = getPlayerByID(playerID);
//        ActionRoundCard selectedCard = (ActionRoundCard) mainBoard.getCardById(cardID);
//        player.placeFamilyMember(selectedCard);
//
//        synchronized (this) {
//            notify(); // 플레이어 턴 진행 후 대기 중인 스레드 깨움
//        }
//    }
// 프론트엔드에서 플레이어의 턴 정보를 받아오는 메서드
public void receivePlayerTurn(String playerID, int cardID) {
    Player player = getPlayerByID(playerID);
    ActionRoundCard selectedCard = (ActionRoundCard) mainBoard.getCardById(cardID);
    player.placeFamilyMember(selectedCard);
    updatePlayerPositions(playerID, cardID);
    synchronized (this) {
        notify(); // 플레이어 턴 진행 후 대기 중인 스레드 깨움
    }
}

    private void updatePlayerPositions(String playerID, int cardID) {
        int cardIndex = cardID - 1;


        playerPositions.set(cardIndex, Integer.parseInt(playerID));

        // 프론트엔드에 업데이트된 정보를 알림
        Map<String, Object> playerPositionInfo = new HashMap<>();
        playerPositionInfo.put("playerPositions", playerPositions);

        notifyPlayers(playerPositionInfo);
    }



    private void resetFamilyMembers() {
        for (Player player : turnOrder) {
            player.resetFamilyMembers();
        }
        mainBoard.resetFamilyMembersOnCards();
        // 프론트엔드에 메시지 전송
        notifyPlayers(Map.of("message", "라운드가 종료되었습니다. 가족 구성원이 집으로 돌아갑니다."));
    }

    private void harvestPhase() {
        farmPhase();
        feedFamilyPhase();
        breedAnimalsPhase();
    }

    private void farmPhase() {

        notifyPlayers(Map.of("message", "농장 단계가 시작되었습니다."));

        for (Player player : players) {
            sendExchangeableCardsInfoToFrontEnd(player.getId(), ExchangeTiming.HARVEST);
            sendExchangeableCardsInfoToFrontEnd(player.getId(), ExchangeTiming.ANYTIME);
            PlayerBoard board = player.getPlayerBoard();
            for (Tile[] row : board.getTiles()) {
                for (Tile tile : row) {
                    if (tile instanceof FieldTile) {
                        FieldTile field = (FieldTile) tile;
                        if (field.getCrops() > 0) {
                            player.addResource("grain", 1);
                            field.removeCrop(1);
                        }
                    }
                }
            }

            synchronized (this) {
                try {
                    wait(); // 프론트엔드에서 신호를 받을 때까지 대기
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void feedFamilyPhase() {
        notifyPlayers(Map.of("message", "가족 먹여살리기 단계가 시작되었습니다."));
        for (Player player : players) {
            int foodNeeded = calculateFoodNeeded(player);
            Map<String, Object> familyStatus = Map.of(
                    "playerId", player.getId(),
                    "familyStatus", player.getFamilyStatus(),
                    "foodNeeded", foodNeeded,
                    "beggingCards", Math.max(0, foodNeeded - player.getResource("food"))
            );
            notifyPlayer(player, familyStatus);

            if (player.getResource("food") >= foodNeeded) {
                player.addResource("food", -foodNeeded);
            } else {
                int foodShortage = foodNeeded - player.getResource("food");
                player.addResource("food", -player.getResource("food"));
                player.addResource("beggingCard", foodShortage);
            }
        }

        // 5초 동안 대기 후 단계 종료
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        notifyPlayers(Map.of("message", "가족 먹여살리기 단계가 종료되었습니다."));
    }

    private int calculateFoodNeeded(Player player) {
        int foodNeeded = 0;
        for (FamilyMember[] row : player.getPlayerBoard().getFamilyMembers()) {
            for (FamilyMember member : row) {
                if (member != null) {
                    foodNeeded += member.isAdult() ? 2 : 1;
                }
            }
        }
        return foodNeeded;
    }

    private void breedAnimalsPhase() {
        notifyPlayers("가축 번식 단계가 시작되었습니다.");

        for (Player player : players) {
            Animal newAnimal = player.getPlayerBoard().breedAnimals();
            if (newAnimal == null) {
                continue; // 번식 가능한 동물이 없는 경우 넘어감
            }

            // 유효한 동물 배치 위치를 프론트엔드로 전송
            Set<int[]> validPositions = player.getPlayerBoard().getValidAnimalPositions(newAnimal.getType());
            if (validPositions.isEmpty()) {
                System.out.println(newAnimal.getType() + " 방생됨.");
                continue;
            }

//            Map<String, Object> animalPlacementInfo = Map.of(
//                    "playerId", player.getId(),
//                    "animalType", newAnimal.getType(),
//                    "validPositions", validPositions.stream()
//                            .map(pos -> Map.of("x", pos[0], "y", pos[1]))
//                            .collect(Collectors.toList())
//            );
//            notifyPlayer(player, animalPlacementInfo);
//
//            // 프론트엔드로부터 동물 배치 좌표를 기다림
//            synchronized (this) {
//                try {
//                    wait(); // 프론트엔드에서 신호를 받을 때까지 대기
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
            // 배치된 양의 수를 카운트하여 자원으로 추가
            int placedSheep = player.placeNewAnimals();
            System.out.println("placedSheep = " + placedSheep);
            player.addResource("sheep", placedSheep);

        }
//        waitForAllPlayers(); // 모든 플레이어가 다음 단계로 넘어갈 준비가 될 때까지 대기
    }

    public void receiveAnimalPlacement(String playerId, String animalType, int x, int y) {
        Player player = getPlayerByID(playerId);
        if (player != null) {
            Animal newAnimal = new Animal(x, y, animalType);
            player.placeAnimalOnBoard(newAnimal, x, y);
        }

        synchronized (this) {
            notify(); // 동물 배치 후 대기 중인 스레드 깨움
        }

    }


    private boolean isHarvestRound(int round) {
        return round == 1 || round == 4 || round == 7 || round == 9 || round == 11 || round == 13 || round == 14;
    }

    private void endRound() {
        for (Player player : players) {
            player.convertBabiesToAdults();
        }
        calculateAndRecordScores();
    }

    private void calculateAndRecordScores() {
        for (Player player : players) {
            int score = calculateScoreForPlayer(player);
            player.setScore(score);
            System.out.println("setScore");

            // 점수 정보를 프론트엔드로 전송
            Map<String, Object> scoreInfo = Map.of(
                    "playerId", player.getId(),
                    "score", score
//                    "details", Map.of(
//                            "emptyTiles", player.getEmptyTilesCount(),
//                            "woodenRooms", player.getWoodenRoomsCount(),
//                            "stoneRooms", player.getStoneRoomsCount(),
//                            "familyMembers", player.getFamilyMembersCount(),
//                            "beggingCards", player.getBeggingCardsCount(),
//                            "grain", player.getGrainCount(),
//                            "sheep", player.getSheepCount()
                    );
//            );
            notifyPlayer(player, scoreInfo);
        }
    }

    private int calculateScoreForPlayer(Player player) {
        int score = 0;
        int emptyTiles = 0;
        int woodenRooms = 0;
        int stoneRooms = 0;
        int familyMembersCount = 0;

        PlayerBoard playerBoard = player.getPlayerBoard();

        Tile[][] tiles = playerBoard.getTiles();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (tiles[i][j] == null) {
                    emptyTiles++;
                } else if (tiles[i][j] instanceof Room) {
                    Room room = (Room) tiles[i][j];
                    if (room.getType() == RoomType.WOOD) {
                        woodenRooms++;
                    } else if (room.getType() == RoomType.STONE) {
                        stoneRooms++;
                    }
                }
                FamilyMember[][] familyMembers = playerBoard.getFamilyMembers();
                if (familyMembers[i][j] != null) {
                    familyMembersCount++;
                }
            }
        }

        score -= emptyTiles;

        Set<FenceArea> managedFenceAreas = playerBoard.getManagedFenceAreas();
        for (FenceArea area : managedFenceAreas) {
            if (area.hasBarn()) {
                score += area.getTileCount();
            }
        }

        score += woodenRooms;
        score += stoneRooms * 2;
        score += familyMembersCount * 3;

        for (CommonCard card : player.getMajorImprovementCards()) {
            if (card instanceof PotteryWorkshop) {
                score += ((PotteryWorkshop) card).calculateAdditionalPoints(player);
            } else if (card instanceof FurnitureWorkshop) {
                score += ((FurnitureWorkshop) card).calculateAdditionalPoints(player);
            }
        }

        score += calculateBeggingPoints(player);
        score += calculateGrainPoints(player);
        score += calculateSheepPoints(player);

        return score;
    }

    private int calculateBeggingPoints(Player player) {
        int beggingCard = player.getResource("beggingCard");
        return -3 * beggingCard;
    }

    private int calculateGrainPoints(Player player) {
        int grains = player.getResource("grain");
        if (grains == 0) {
            return -1;
        } else if (grains <= 3) {
            return 1;
        } else if (grains <= 6) {
            return 2;
        } else if (grains <= 7) {
            return 3;
        } else if (grains >= 8) {
            return 4;
        }
        return 0;
    }

    private int calculateSheepPoints(Player player) {
        int sheep = player.getResource("sheep");
        if (sheep == 0) {
            return -1;
        } else if (sheep <= 3) {
            return 1;
        } else if (sheep <= 5) {
            return 2;
        } else if (sheep <= 7) {
            return 3;
        } else if (sheep >= 8) {
            return 4;
        }
        return 0;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void executeCard(String playerID, String cardID) {
        Player player = getPlayerByID(playerID);
//        CommonCard card = getCardByID(cardID);
//        card.execute(player);
    }

    private Player getPlayerByID(String playerID) {
        for (Player player : players) {
            if (player.getId().equals(playerID)) {
                return player;
            }
        }
        return null;
    }

    private CommonCard getCardById(Player player, int cardId) {
        // Check Major Improvement Cards in MainBoard
        for (CommonCard card : mainBoard.getMajorImprovementCards()) {
            if (card.getId() == cardId) {
                return card;
            }
        }

        // Check Occupation and Minor Improvement Cards in Player's hand
        for (CommonCard card : player.getOccupationCards()) {
            if (card.getId() == cardId) {
                return card;
            }
        }

        for (CommonCard card : player.getMinorImprovementCards()) {
            if (card.getId() == cardId) {
                return card;
            }
        }

        return null;
    }

    public MainBoard getMainBoard() {
        return mainBoard;
    }

    public List<Player> getPlayers() {
        return players;
    }

    private void printFamilyMembersOnBoard() {
        System.out.println("Family members on board:");
        for (Player player : players) {
            FamilyMember[][] familyMembers = player.getPlayerBoard().getFamilyMembers();
            for (int i = 0; i < familyMembers.length; i++) {
                for (int j = 0; j < familyMembers[i].length; j++) {
                    if (familyMembers[i][j] != null && familyMembers[i][j].isUsed()) {
                        FamilyMember member = familyMembers[i][j];
                        System.out.println("  Player " + player.getId() + " - Family Member at (" + i + ", " + j + ") - Adult: " + member.isAdult());
                    }
                }
            }
        }
    }

    public void setMainBoard(MainBoard mainBoard) {
        this.mainBoard = mainBoard;
        System.out.println("MainBoard set to: " + mainBoard);
    }

    public void playerReadyForNextPhase(String playerId) {
        synchronized (nextPhaseLock) {
            readyPlayers.add(playerId);
            if (readyPlayers.size() == players.size()) {
                nextPhaseLock.notifyAll();
            }
        }
    }

    private void waitForAllPlayers() {
        synchronized (nextPhaseLock) {
            while (readyPlayers.size() < players.size()) {
                try {
                    nextPhaseLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            readyPlayers.clear();
        }
    }

    public List<Map<String, Object>> getOccupationCards(String playerId) {
        Player player = getPlayerByID(playerId);
        if (player != null) {
            return player.getOccupationCards().stream().map(CommonCard::toMap).collect(Collectors.toList());
        }
        return List.of();
    }

    public List<Map<String, Object>> getMinorImprovementCards(String playerId) {
        Player player = getPlayerByID(playerId);
        if (player != null) {
            return player.getMinorImprovementCards().stream().map(CommonCard::toMap).collect(Collectors.toList());
        }
        return List.of();
    }

    public List<Map<String, Object>> getAvailableMajorImprovementCards() {
        return mainBoard.getAvailableMajorImprovementCards().stream().map(CommonCard::toMap).collect(Collectors.toList());
    }

    public void sendCardListToFrontEnd(List<CommonCard> cards, String playerId) {
        List<Map<String, Object>> cardList = cards.stream().map(CommonCard::toMap).collect(Collectors.toList());
        simpMessagingTemplate.convertAndSend("/topic/cards/" + playerId, cardList);
        System.out.println("Sending card list to frontend for player " + playerId + ": " + cardList);
    }


    public void receiveSelectedCard(String playerId, int cardId) {
        Player player = getPlayerByID(playerId);
        if (player != null) {
            CommonCard selectedCard = getCardById(player, cardId);
            if (selectedCard != null) {
                selectedCards.put(playerId, selectedCard);
                System.out.println("Received selected card from player: " + playerId + " with cardId: " + cardId);
            }
        }
        CountDownLatch latch = latches.get(playerId);
        if (latch != null) {
            latch.countDown(); // 동기화된 스레드를 깨움
        }
    }


    public CommonCard getSelectedCard(String playerId) {
        return selectedCards.get(playerId);
    }

    private Map<String, int[]> selectedPositions = new HashMap<>();

    public void sendValidPositionsToFrontEnd(String playerId, Set<int[]> validPositions, String actionType) {
        Map<String, Object> message = Map.of(
                "playerId", playerId,
                "actionType", actionType,
                "validPositions", validPositions.stream()
                        .map(pos -> Map.of("x", pos[0], "y", pos[1]))
                        .collect(Collectors.toList())
        );
        simpMessagingTemplate.convertAndSend("/topic/validPositions", message);
    }

    private final Map<String, CountDownLatch> latches = new ConcurrentHashMap<>();


    public void setLatch(String playerId, CountDownLatch latch) {
        latches.put(playerId, latch);
    }


    public void receiveSelectedPosition(String playerId, int x, int y) {
        selectedPositions.put(playerId, new int[]{x, y});
        CountDownLatch latch = latches.get(playerId);
        if (latch != null) {
            latch.countDown(); // 동기화된 스레드를 깨움
        }
    }

    public int[] getSelectedPosition(String playerId) {
        return selectedPositions.get(playerId);
    }

    private Map<String, List<int[]>> selectedFencePositions = new HashMap<>();

    public void sendFenceRequestToFrontEnd(String playerId) {
        Map<String, Object> message = Map.of(
                "playerId", playerId,
                "actionType", "buildFence"
        );
        simpMessagingTemplate.convertAndSend("/topic/fenceRequest", message);
    }

    public void receiveSelectedFencePositions(String playerId, List<int[]> positions) {
        selectedFencePositions.put(playerId, positions);
        System.out.println("Received selected fence positions from player: " + playerId + " with positions: " + positions);
        CountDownLatch latch = latches.get(playerId);
        if (latch != null) {
            latch.countDown(); // 동기화된 스레드를 깨움
        }
    }

    public List<int[]> getSelectedFencePositions(String playerId) {
        return selectedFencePositions.get(playerId);
    }

    public void sendFamilyMemberPosition(String playerId, int x, int y) {
        Map<String, Object> message = Map.of(
                "playerId", playerId,
                "actionType", "addNewborn",
                "position", Map.of("x", x, "y", "y")
        );
        simpMessagingTemplate.convertAndSend("/topic/familyMemberPosition", message);
    }

    public void sendRenovationInfo(String playerId, RoomType newType) {
        Map<String, Object> message = Map.of(
                "playerId", playerId,
                "actionType", "renovateRooms",
                "newRoomType", newType.name()
        );
        simpMessagingTemplate.convertAndSend("/topic/renovationInfo", message);
    }

    public void sendTurnOrderInfo(String playerId, List<Player> currentTurnOrder, List<Player> newTurnOrder) {
        Map<String, Object> message = Map.of(
                "playerId", playerId,
                "actionType", "becomeFirstPlayer",
                "currentTurnOrder", currentTurnOrder.stream()
                        .map(player -> Map.of("id", player.getId(), "name", player.getName()))
                        .collect(Collectors.toList()),
                "newTurnOrder", newTurnOrder.stream()
                        .map(player -> Map.of("id", player.getId(), "name", player.getName()))
                        .collect(Collectors.toList())
        );
        simpMessagingTemplate.convertAndSend("/topic/turnOrderInfo", message);
    }


    public void sendAnimalPlacementInfo(String playerId, List<Map<String, Object>> placedAnimals, int remainingCapacity) {
        Map<String, Object> message = Map.of(
                "playerId", playerId,
                "placedAnimals", placedAnimals,
                "remainingCapacity", remainingCapacity
        );
        simpMessagingTemplate.convertAndSend("/topic/animalPlacement", message);
    }

    public void sendChoiceRequestToFrontEnd(String playerId, String choiceType, Map<String, Object> options) {
        Map<String, Object> message = new HashMap<>();
        message.put("playerId", playerId);
        message.put("choiceType", choiceType);
        message.put("options", options);

        simpMessagingTemplate.convertAndSend("/topic/choiceRequest", message);
    }

    private Map<String, CountDownLatch> playerLatches = new ConcurrentHashMap<>();

    public void receivePlayerChoice(String playerId, String choiceType, Object choice) {
        System.out.println("Received choice from player: " + playerId + " choiceType: " + choiceType + " choice: " + choice);
        Player player = getPlayerByID(playerId);
        if (player != null) {
            switch (choiceType) {
                case "AndOr":
                    player.setAndOrChoice((int) choice);
                    break;
                case "Then":
                    player.setThenChoice((boolean) choice);
                    break;
                case "Or":
                    player.setOrChoice((boolean) choice);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown choiceType: " + choiceType);
            }
        }

        CountDownLatch latch = playerLatches.get(playerId);
        if (latch != null) {
            latch.countDown(); // 대기 중인 스레드를 깨움
        }
    }


    public void waitForPlayerChoice(String playerId) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        playerLatches.put(playerId, latch);
        latch.await(); // 대기
    }

    // 플레이어가 선택을 했는지 확인하는 메서드
    public boolean hasPlayerChoice(String id) {
        Player player = getPlayerByID(id);
        return player != null && player.chooseOption();
    }

    // 플레이어의 선택을 반환하는 메서드
    public int getPlayerChoice(String id) {
        Player player = getPlayerByID(id);
        if (player == null) {
            throw new IllegalArgumentException("Player not found with id: " + id);
        }
        return player.chooseOptionForAndOr(); // 또는 적절한 선택을 반환
    }


    // 플레이어의 AND/OR 선택을 반환하는 메서드
    public int getPlayerAndOrChoice(String id) {
        Player player = getPlayerByID(id);
        if (player == null) {
            throw new IllegalArgumentException("Player not found with id: " + id);
        }
        return player.chooseOptionForAndOr();
    }

    // 플레이어의 THEN 선택을 반환하는 메서드
    public boolean getPlayerThenChoice(String id) {
        Player player = getPlayerByID(id);
        if (player == null) {
            throw new IllegalArgumentException("Player not found with id: " + id);
        }
        return player.chooseOptionForThen();
    }

    // 플레이어의 OR 선택을 반환하는 메서드
    public boolean getPlayerOrChoice(String id) {
        Player player = getPlayerByID(id);
        if (player == null) {
            throw new IllegalArgumentException("Player not found with id: " + id);
        }
        return player.chooseOptionForOr();
    }

    public void sendDecoratedCardInfo(Player player, List<ActionRoundCard> actionCards, List<ActionRoundCard> roundCards) {
        // ActionRoundCard 리스트를 CommonCard 리스트로 변환
        List<CommonCard> commonActionCards = actionCards.stream()
                .map(card -> (CommonCard) card)
                .collect(Collectors.toList());
        List<CommonCard> commonRoundCards = roundCards.stream()
                .map(card -> (CommonCard) card)
                .collect(Collectors.toList());

        // 프론트엔드로 전송
        sendCardListToFrontEnd(commonActionCards, player.getId());
        sendCardListToFrontEnd(commonRoundCards, player.getId());
    }

    private Map<String, Object> cardToMap(ActionRoundCard card) {
        // 카드 정보를 맵으로 변환하는 메서드
        return Map.of(
                "id", card.getId(),
                "name", card.getName(),
                "description", card.getDescription()
        );
    }

    private Map<String, String> resourceChoices = new ConcurrentHashMap<>();

    public void sendResourceChoiceRequestToFrontEnd(String playerId, String resource1, String resource2, int amount) {
        Map<String, Object> options = Map.of(
                "resource1", resource1,
                "resource2", resource2,
                "amount", amount
        );
        simpMessagingTemplate.convertAndSend("/topic/choiceRequest", options);
        System.out.println("Sending resource choice request to frontend for player " + playerId + ": " + options);

    }

    public String getChosenResource(String playerId) {
        synchronized (resourceChoices) {
            while (!resourceChoices.containsKey(playerId)) {
                try {
                    resourceChoices.wait(); // 프론트엔드에서 신호를 받을 때까지 대기
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return resourceChoices.remove(playerId);
        }
    }

    public void receiveChosenResource(String playerId, String chosenResource) {
        synchronized (resourceChoices) {
            resourceChoices.put(playerId, chosenResource);
            resourceChoices.notifyAll(); // 선택을 기다리는 스레드를 깨움
        }
    }
    public void sendMajorImprovementCardsToFrontEnd(Player player) {
        List<CommonCard> majorImprovementCards = player.getMajorImprovementCards();
        List<Map<String, Object>> cards = majorImprovementCards.stream()
                .map(CommonCard::toMap)
                .collect(Collectors.toList());

        Map<String, Object> message = new HashMap<>();
        message.put("playerId", player.getId());
        message.put("majorImprovementCards", cards);

        System.out.println("Sending major improvement cards to frontend for player " + player.getId() + ": " + cards);

        simpMessagingTemplate.convertAndSend("/topic/majorImprovementCards", message);
    }


    public void sendPlayerResourcesToFrontEnd(Player player) {
        Map<String, Object> message = new HashMap<>();
        message.put("playerId", player.getId());
        message.put("resources", player.getResources());

        System.out.println("Sending player resources to frontend for player " + player.getId() + ": " + player.getResources());

        simpMessagingTemplate.convertAndSend("/topic/playerResources", message);
    }

    public void sendActiveCardsListToFrontEnd(Player player) {
        List<CommonCard> activeCards = player.getActiveCards();
        List<Map<String, Object>> cards = activeCards.stream()
                .map(CommonCard::toMap)
                .collect(Collectors.toList());

        Map<String, Object> message = new HashMap<>();
        message.put("playerId", player.getId());
        message.put("majorImprovementCards", cards);

        System.out.println("Sending active cards to frontend for player " + player.getId() + ": " + cards);

        simpMessagingTemplate.convertAndSend("/topic/activeCards", message);
    }
}
