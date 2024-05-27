package controllers;

import cards.common.ActionRoundCard;
import cards.common.BakingCard;
import cards.common.CommonCard;
import cards.common.ExchangeableCard;
import cards.majorimprovement.MajorImprovementCard;
import enums.ExchangeTiming;
import models.*;

import java.util.*;

public class GameController {
    private String gameID;
    private List<Player> players;
    private List<Player> turnOrder;
    private List<Player> nextTurnOrder;
    private MainBoard mainBoard;
    private CardController cardController;
    private RoomController roomController;
    private int currentRound;

    public GameController(String gameID, RoomController roomController, List<Player> players) {
        this.gameID = gameID;
        this.players = players;
        this.mainBoard = new MainBoard();
        this.cardController = CardController.getInstance();
        this.roomController = roomController;
        this.currentRound = 1;

        this.turnOrder = new ArrayList<>(players);
        // TODO 매 라운드별 프론트에 턴 넘기기
        Collections.shuffle(this.turnOrder);

        initializeFirstFoods();
    }

    // 다음 라운드의 턴 오더 설정
    public void setNextTurnOrder(List<Player> nextTurnOrder) {
        this.nextTurnOrder = nextTurnOrder;
    }

    // 기존 턴 오더 반환
    public List<Player> getTurnOrder() {
        return turnOrder;
    }

    //turnOrder에 turn을 추가하는함수 테스트위해 작성
    public void setTurnOrder(Player player) {
        this.turnOrder.add(player);
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

        // 직업 카드 분배
        for (int i = 0; i < 8; i++) {
            players.get(i % numPlayers).addCard(occupationDeck.get(i), "occupation");
        }

        // 보조 설비 카드 분배
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

    public void startGame() {
        while (currentRound <= 14) {
            System.out.println("-------------------------------------------------------------------------------");
            System.out.println("Round " + currentRound + " starts.");
            prepareRound();
            playRound();
            System.out.println("isHarvestRound(currentRound) = " + isHarvestRound(currentRound));
            if (isHarvestRound(currentRound)) {
                System.out.println("Harvest phase triggered at round " + currentRound);
                harvestPhase();
            }
            endRound();
            System.out.println("Round " + currentRound + " ends.");
            System.out.println("-------------------------------------------------------------------------------");
            currentRound++;
        }
        endGame();
    }

    public void testGame() {
        while (currentRound <= 4) {
            System.out.println("-------------------------------------------------------------------------------");
            System.out.println("Round " + currentRound + " starts.");
            prepareRound();
            playRound();
            System.out.println("isHarvestRound(currentRound) = " + isHarvestRound(currentRound));
            if (isHarvestRound(currentRound)) {
                System.out.println("Harvest phase triggered at round " + currentRound);
                harvestPhase();
            }
            endRound();
            System.out.println("Round " + currentRound + " ends.");
            System.out.println("-------------------------------------------------------------------------------");
            currentRound++;
        }
        endGame();
    }

    public void prepareRound() {
        System.out.println("Preparing round " + currentRound);
        mainBoard.revealRoundCard(currentRound);
        mainBoard.accumulateResources();
        for (Player player : players) {
            List<ExchangeableCard> exchangeableCards = player.getExchangeableCards(ExchangeTiming.ANYTIME);
            // TODO 프론트엔드에 교환 가능 카드 목록 제공 로직 필요
        }
        if (nextTurnOrder != null && !nextTurnOrder.isEmpty()) {
            turnOrder = new ArrayList<>(nextTurnOrder);
            nextTurnOrder.clear(); // 다음 라운드를 위해 nextTurnOrder 초기화
        }
    }

    public void playRound() {
        System.out.println("Playing round " + currentRound);
        boolean roundFinished = false;
        while (!roundFinished) {
            roundFinished = true;
            for (Player player : turnOrder) {
                if (player.hasAvailableFamilyMembers()) {
                    List<ActionRoundCard> availableCards = new ArrayList<>();
                    availableCards.addAll(mainBoard.getActionCards());
                    availableCards.addAll(mainBoard.getRevealedRoundCards());
                    availableCards.removeIf(card -> mainBoard.isCardOccupied(card));

                    if (!availableCards.isEmpty()) {
                        roundFinished = false;
                        System.out.println("Player " + player.getId() + "'s turn.");
                        playerTurn(player.getId());

                        printFamilyMembersOnBoard();
                    } else {
                        System.out.println("No available cards for player " + player.getId());
                    }
                }
            }
        }
        resetFamilyMembers();
    }

    private void playerTurn(String playerID) {
        Player player = getPlayerByID(playerID);

        if (player != null) {
            List<ActionRoundCard> availableCards = new ArrayList<>();
            availableCards.addAll(mainBoard.getActionCards());
            availableCards.addAll(mainBoard.getRevealedRoundCards());
            availableCards.removeIf(card -> mainBoard.isCardOccupied(card));

            System.out.println("Available cards before selection:");
            for (ActionRoundCard card : availableCards) {
                System.out.println("  Card: " + card.getName() + " (Occupied: " + mainBoard.isCardOccupied(card) + ")");
            }

            if (!availableCards.isEmpty()) {
                Random rand = new Random();
                ActionRoundCard selectedCard = availableCards.get(rand.nextInt(availableCards.size()));
                player.placeFamilyMember(selectedCard);

                System.out.println("Available cards after selection:");
                for (ActionRoundCard card : availableCards) {
                    System.out.println("  Card: " + card.getName() + " (Occupied: " + mainBoard.isCardOccupied(card) + ")");
                }

            } else {
                System.out.println("No available cards for player " + playerID);
            }
        }
    }

    private boolean checkIfRoundFinished() {
        for (Player player : players) {
            if (player.hasAvailableFamilyMembers()) {
                return false;
            }
        }
        return true;
    }

    public void resetFamilyMembers() {
        for (Player player : turnOrder) {
            player.resetFamilyMembers();
        }
        mainBoard.resetFamilyMembersOnCards();
    }

    public void harvestPhase() {
        System.out.println("Starting harvest phase.");
        farmPhase();
        feedFamilyPhase();
        breedAnimalsPhase();
        System.out.println("Harvest phase completed.");
    }

    public void farmPhase() {
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
//        notifyPlayers("농장 단계 완료. 가족 먹여살리기 단계로 진행하세요.");
        System.out.println("Farm phase completed.");
    }

    private void feedFamilyPhase() {
        for (Player player : players) {
            int foodNeeded = calculateFoodNeeded(player);
            if (player.getResource("food") >= foodNeeded) {
                player.addResource("food", -foodNeeded);
            } else {
                int foodShortage = foodNeeded - player.getResource("food");
                player.addResource("food", -player.getResource("food"));
                player.addResource("beggingCard", foodShortage);
            }
            List<ExchangeableCard> exchangeableCards = player.getExchangeableCards(ExchangeTiming.HARVEST);
        }
//        notifyPlayers("가족 먹여살리기 단계 완료. 가축 번식 단계로 진행하세요.");
        System.out.println("Feed family phase completed.");



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

    // 동물 번식 단계를 구현
    private void breedAnimalsPhase() {
        for (Player player : players) {
            List<Animal> newAnimals = player.getPlayerBoard().breedAnimals();
            for (Animal animal : newAnimals) {
                if (!player.addAnimal(animal)) {
                    System.out.println(animal.getType() + " 방생됨.");
                }
            }
            List<ExchangeableCard> exchangeableCards = player.getExchangeableCards(ExchangeTiming.HARVEST);
        }
//        notifyPlayers("가축 번식 단계 완료. 수확 단계 종료.");
        System.out.println("Breed animals phase completed.");

    }

    private Map<String, Integer> countAnimals(Player player) {
        Map<String, Integer> animalCounts = new HashMap<>();
        for (Animal[] row : player.getPlayerBoard().getAnimals()) {
            for (Animal animal : row) {
                if (animal != null) {
                    animalCounts.put(animal.getType(), animalCounts.getOrDefault(animal.getType(), 0) + 1);
                }
            }
        }
        return animalCounts;
    }

    private boolean isHarvestRound(int round) {
        return round == 4 || round == 7 || round == 9 || round == 11 || round == 13 || round == 14;
    }

    private void endRound() {
        System.out.println("Ending round " + currentRound);
        for (Player player : players) {
            player.convertBabiesToAdults();
        }
        calculateAndRecordScores();
    }

    private void calculateAndRecordScores() {
        for (Player player : players) {
            int score = calculateScoreForPlayer(player);
            player.setScore(score);
        }
        // WebSocketService.sendMessageToClient(gameID, "gameScores", getScores());
    }

    private int calculateScoreForPlayer(Player player) {
        return 0;
    }

    private List<Map<String, Object>> getScores() {
        List<Map<String, Object>> scores = new ArrayList<>();
        for (Player player : players) {
            Map<String, Object> playerScore = new HashMap<>();
            playerScore.put("playerID", player.getId());
            playerScore.put("score", player.getScore());
            scores.add(playerScore);
        }
        return scores;
    }

    public void endGame() {
        roomController.handleGameEnd();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void executeCard(String playerID, String cardID) {
        Player player = getPlayerByID(playerID);
        CommonCard card = getCardByID(cardID);
        card.execute(player);
    }

    private Player getPlayerByID(String playerID) {
        for (Player player : players) {
            if (player.getId().equals(playerID)) {
                return player;
            }
        }
        return null;
    }

    private CommonCard getCardByID(String cardID) {
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

    public void purchaseMajorImprovementCard(Player player, int cardId) {
        List<CommonCard> majorImprovementCards = mainBoard.getMajorImprovementCards();
        MajorImprovementCard cardToPurchase = null;

        for (CommonCard card : majorImprovementCards) {
            if (card.getId() == cardId && card instanceof MajorImprovementCard) {
                cardToPurchase = (MajorImprovementCard) card;
                break;
            }
        }

        if (cardToPurchase != null && player.checkResources(cardToPurchase.getPurchaseCost())) {
            player.payResources(cardToPurchase.getPurchaseCost());
            player.addMajorImprovementCard(cardToPurchase);
            mainBoard.removeMajorImprovementCard(cardToPurchase);
            if (cardToPurchase.hasImmediateBakingAction()) {
                cardToPurchase.triggerBreadBaking(player);
            }
        } else {
//            notifyPlayer(player, "자원이 부족하여 주요 설비 카드를 구매할 수 없습니다.");
        }
    }

    public void executeExchange(String playerId, int cardId, String fromResource, String toResource, int amount) {
        Player player = getPlayerByID(playerId);
        List<CommonCard> majorImprovementCards = player.getMajorImprovementCards();
        ExchangeableCard cardToUse = null;

        for (CommonCard card : majorImprovementCards) {
            if (card.getId() == cardId && card instanceof ExchangeableCard) {
                cardToUse = (ExchangeableCard) card;
                break;
            }
        }

        if (cardToUse != null && cardToUse.canExchange(ExchangeTiming.ANYTIME)) {
            cardToUse.executeExchange(player, fromResource, toResource, amount);
        } else {
            notifyPlayers("교환 기능을 사용할 수 없습니다.");
        }
    }

//    private void printAvailableCards() {
//        System.out.println("Available cards:");
//        List<ActionRoundCard> availableCards = new ArrayList<>();
//        availableCards.addAll(mainBoard.getActionCards());
//        availableCards.addAll(mainBoard.getRevealedRoundCards());
//        availableCards.removeIf(card -> mainBoard.canPlaceFamilyMember(card));
//
//        for (ActionRoundCard card : availableCards) {
//            System.out.println("  Card: " + card.getName());
//        }
//    }

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
    }
}
