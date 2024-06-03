package com.example.agricola.service;

import com.example.agricola.cards.common.AccumulativeCard;
import com.example.agricola.controller.CardController;
import com.example.agricola.models.*;
import com.example.agricola.cards.common.ActionRoundCard;
import com.example.agricola.cards.common.CommonCard;
import com.example.agricola.cards.common.ExchangeableCard;
import com.example.agricola.cards.factory.imp.major.FurnitureWorkshop;
import com.example.agricola.cards.factory.imp.major.PotteryWorkshop;
import com.example.agricola.enums.ExchangeTiming;
import com.example.agricola.enums.RoomType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameService {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private void notifyPlayers(Map<String, Object> message) {
        simpMessagingTemplate.convertAndSend("/topic/game", message);
    }

    private void notifyPlayer(Player player, Map<String, Object> message) {
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
    private Map<String, CommonCard> selectedCards;


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

        for (int i = 0; i < 8; i++) {
            players.get(i % numPlayers).addCard(occupationDeck.get(i), "occupation");
        }

        for (int i = 0; i < 8; i++) {
            players.get(i % numPlayers).addCard(minorImprovementDeck.get(i), "minorImprovement");
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
        this.nextTurnOrder = nextTurnOrder;
    }

    public List<Player> getTurnOrder() {
        return turnOrder;
    }

    public void playGame(String gameID) {
        while (currentRound <= 14) {
            System.out.println("-------------------------------------------------------------------------------");
            System.out.println("Round " + currentRound + " starts.");
            prepareRound();
            playRound();
            if (isHarvestRound(currentRound)) {
                harvestPhase();
            }
            endRound();
            System.out.println("Round " + currentRound + " ends.");
            System.out.println("-------------------------------------------------------------------------------");
            currentRound++;
        }
        endGame();
    }

    private void prepareRound() {
        notifyPlayers("라운드 준비 단계 입니다.");
        mainBoard.revealRoundCard(currentRound);
        mainBoard.accumulateResources();

        // 현재 라운드 정보 수집
        Map<String, Object> roundInfo = new HashMap<>();
        roundInfo.put("currentRound", currentRound);

        // 오픈된 카드 정보 수집
        List<Map<String, Object>> openedCards = mainBoard.getRevealedRoundCards().stream()
                .map(card -> (Map<String, Object>) Map.of(
                        "id", (Object) card.getId(),
                        "name", (Object) card.getName(),
                        "description", (Object) card.getDescription()
                )).collect(Collectors.toList());
        roundInfo.put("openedCards", openedCards);

        // 누적된 자원 정보 수집
        List<Map<String, Object>> accumulatedResources = mainBoard.getActionCards().stream()
                .filter(card -> card instanceof AccumulativeCard)
                .map(card -> Map.of(
                        "id", card.getId(),
                        "name", card.getName(),
                        "description", card.getDescription(),
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

    private void playRound() {
        for (Player player : players) {
            List<ExchangeableCard> exchangeableCards = player.getExchangeableCards(ExchangeTiming.ANYTIME);
            // 교환 가능한 카드 정보를 프론트엔드로 전송
            Map<String, Object> exchangeableCardsInfo = Map.of(
                    "playerId", player.getId(),
                    "exchangeableCards", exchangeableCards.stream()
                            .filter(card -> {
                                // 자원이 충분한지 확인
                                Map<String, Integer> exchangeRate = card.getExchangeRate();
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
                                    for (Map.Entry<String, Integer> entry : exchangeRate.entrySet()) {
                                        int availableAmount = player.getResource(entry.getKey()) / entry.getValue();
                                        maxExchangeAmount = Math.min(maxExchangeAmount, availableAmount);
                                    }
                                    return Map.of(
                                            "id", commonCard.getId(),
                                            "name", commonCard.getName(),
                                            "description", commonCard.getDescription(),
                                            "exchangeRate", exchangeRate,
                                            "maxExchangeAmount", maxExchangeAmount
                                    );
                                } else {
                                    return Map.of(); // 기본값
                                }
                            })
                            .collect(Collectors.toList())
            );
            notifyPlayer(player, exchangeableCardsInfo);
        }

        boolean roundFinished = false;
        while (!roundFinished) {
            roundFinished = true;
            for (Player player : turnOrder) {
                if (player.hasAvailableFamilyMembers()) {
                    List<ActionRoundCard> availableCards = new ArrayList<>(mainBoard.getActionCards());
                    availableCards.addAll(mainBoard.getRevealedRoundCards());
                    availableCards.removeIf(card -> mainBoard.isCardOccupied(card));

                    // 사용 가능한 카드 정보를 프론트엔드로 전송
                    Map<String, Object> availableCardsInfo = Map.of(
                            "playerId", player.getId(),
                            "availableCards", availableCards.stream()
                                    .map(card -> Map.of(
                                            "id", card.getId(),
                                            "name", card.getName(),
                                            "description", card.getDescription()
                                    ))
                                    .collect(Collectors.toList())
                    );
                    notifyPlayer(player, availableCardsInfo);

                    if (!availableCards.isEmpty()) {
                        roundFinished = false;

                        // 플레이어 턴 진행
                        playerTurn(player.getId(), availableCards);
                        printFamilyMembersOnBoard();
                    }
                }
            }
        }
        resetFamilyMembers();
    }

    // 교환 요청을 처리하는 메서드
    public void handleExchangeRequest(String playerID, String cardName, String fromResource, String toResource, int amount) {
        Player player = getPlayerByID(playerID);
        ExchangeableCard card = (ExchangeableCard) mainBoard.getCardByName(cardName);

        if (card != null && (card.canExchange(ExchangeTiming.ANYTIME) || card.canExchange(ExchangeTiming.HARVEST))) {
            card.executeExchange(player, fromResource, toResource, amount);
        }
    }


    private void playerTurn(String playerID, List<ActionRoundCard> availableCards) {
        Player player = getPlayerByID(playerID);

        if (player != null) {

            // 플레이어에게 턴 정보를 프론트엔드로 전송
            Map<String, Object> playerTurnInfo = Map.of(
                    "playerId", player.getId(),
                    "availableCards", availableCards.stream()
                            .map(card -> Map.of(
                                    "id", card.getId(),
                                    "name", card.getName(),
                                    "description", card.getDescription()
                            ))
                            .collect(Collectors.toList()),
                    "message", "턴 정보를 아래와 같은 포맷으로 전송해주세요.: {\"playerID\": \"플레이어 ID\", \"cardName\": \"선택한 카드 이름\", \"familyMemberX\": 가족 구성원 X 좌표, \"familyMemberY\": 가족 구성원 Y 좌표}"
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
        }
    }

    // 프론트엔드에서 플레이어의 턴 정보를 받아오는 메서드
    public void receivePlayerTurn(String playerID, int cardID) {
        Player player = getPlayerByID(playerID);
        ActionRoundCard selectedCard = (ActionRoundCard) mainBoard.getCardById(cardID);
        player.placeFamilyMember(selectedCard);

        synchronized (this) {
            notify(); // 플레이어 턴 진행 후 대기 중인 스레드 깨움
        }
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
        notifyPlayers("수확 단계가 시작되었습니다.");
        farmPhase();
        feedFamilyPhase();
        breedAnimalsPhase();
    }

    private void farmPhase() {
//        for (Player player : players) {
//            PlayerBoard board = player.getPlayerBoard();
//            for (Tile[] row : board.getTiles()) {
//                for (Tile tile : row) {
//                    if (tile instanceof FieldTile) {
//                        FieldTile field = (FieldTile) tile;
//                        if (field.getCrops() > 0) {
//                            player.addResource("grain", 1);
//                            field.removeCrop(1);
//                        }
//                    }
//                }
//            }
//        }

        notifyPlayers(Map.of("message", "농장 단계가 시작되었습니다."));

        // 수확 단계에서 사용 가능한 교환 가능 카드 정보를 프론트엔드로 전송
        for (Player player : players) {
            List<ExchangeableCard> harvestExchangeableCards = player.getExchangeableCards(ExchangeTiming.ANYTIME);
            harvestExchangeableCards.addAll(player.getExchangeableCards(ExchangeTiming.HARVEST));

            Map<String, Object> harvestExchangeableCardsInfo = Map.of(
                    "playerId", player.getId(),
                    "harvestExchangeableCards", harvestExchangeableCards.stream()
                            .filter(card -> {
                                // 자원이 충분한지 확인
                                Map<String, Integer> exchangeRate = card.getExchangeRate();
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
                                    for (Map.Entry<String, Integer> entry : exchangeRate.entrySet()) {
                                        int availableAmount = player.getResource(entry.getKey()) / entry.getValue();
                                        maxExchangeAmount = Math.min(maxExchangeAmount, availableAmount);
                                    }
                                    return Map.of(
                                            "id", commonCard.getId(),
                                            "name", commonCard.getName(),
                                            "description", commonCard.getDescription(),
                                            "exchangeRate", exchangeRate,
                                            "maxExchangeAmount", maxExchangeAmount
                                    );
                                } else {
                                    return Map.of(); // 기본값
                                }
                            })
                            .collect(Collectors.toList())
            );
            notifyPlayer(player, harvestExchangeableCardsInfo);
        }
        for (Player player : players) {
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
        }
        waitForAllPlayers(); // 모든 플레이어가 다음 단계로 넘어갈 준비가 될 때까지 대기

    }

    private void feedFamilyPhase() {
//        notifyPlayers("가족 먹여살리기 단계가 시작되었습니다.");
//        for (Player player : players) {
//            int foodNeeded = calculateFoodNeeded(player);
//            if (player.getResource("food") >= foodNeeded) {
//                player.addResource("food", -foodNeeded);
//            } else {
//                int foodShortage = foodNeeded - player.getResource("food");
//                player.addResource("food", -player.getResource("food"));
//                player.addResource("beggingCard", foodShortage);
//            }
//        }
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

            Map<String, Object> animalPlacementInfo = Map.of(
                    "playerId", player.getId(),
                    "animalType", newAnimal.getType(),
                    "validPositions", validPositions.stream()
                            .map(pos -> Map.of("x", pos[0], "y", pos[1]))
                            .collect(Collectors.toList())
            );
            notifyPlayer(player, animalPlacementInfo);

            // 프론트엔드로부터 동물 배치 좌표를 기다림
            synchronized (this) {
                try {
                    wait(); // 프론트엔드에서 신호를 받을 때까지 대기
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
        waitForAllPlayers(); // 모든 플레이어가 다음 단계로 넘어갈 준비가 될 때까지 대기
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
        return round == 4 || round == 7 || round == 9 || round == 11 || round == 13 || round == 14;
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


    private void notifyPlayers(String message) {
        // 프론트엔드에 메시지를 보내는 로직 필요
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

    public void receiveSelectedPosition(String playerId, int x, int y) {
        selectedPositions.put(playerId, new int[]{x, y});
        System.out.println("Received selected position from player: " + playerId + " with coordinates: (" + x + ", " + y + ")");
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

}
