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

        for (Player player : players) {
            for (int j = 0; j < 7; j++) {
                player.addCard(occupationDeck.get(j), "occupation");
                player.addCard(minorImprovementDeck.get(j), "minorImprovement");
            }
        }
    }

    private void setupMainBoard() {
        List<CommonCard> actionCards = cardController.getDeck("actionCards");
        List<List<CommonCard>> roundCycles = cardController.getShuffledRoundCardsByCycle();
        List<CommonCard> majorImprovementCards = cardController.getDeck("majorImprovementCards");

        mainBoard.initializeBoard(actionCards, roundCycles, majorImprovementCards);
    }

    public void startGame() {
        while (currentRound <= 14) {
            prepareRound();
            playRound();
            if (isHarvestRound(currentRound)) {
                harvestPhase();
            }
            endRound();
            currentRound++;
        }
        endGame();
    }

    private void prepareRound() {
        mainBoard.revealRoundCard(currentRound);
        mainBoard.accumulateResources();
        for (Player player : players) {
            List<ExchangeableCard> exchangeableCards = player.getExchangeableCards(ExchangeTiming.ANYTIME);
            // 프론트엔드에 교환 가능 카드 목록 제공 로직 필요
            // WebSocketService.sendMessageToClient(player.getId(), "exchangeableCards", exchangeableCards);
        }
        if (nextTurnOrder != null && !nextTurnOrder.isEmpty()) {
            turnOrder = new ArrayList<>(nextTurnOrder);
        }
    }

    private void playRound() {
        boolean roundFinished = false;
        while (!roundFinished) {
            for (Player player : turnOrder) {
                if (player.hasAvailableFamilyMembers()) {
                    playerTurn(player.getId());
                }
            }
            roundFinished = checkIfRoundFinished();
        }
        resetFamilyMembers();
    }

    private void playerTurn(String playerID) {
        Player player = getPlayerByID(playerID);
        if (player != null) {
            // 현재 턴인 플레이어 정보를 프론트엔드에 전달
            // WebSocketService.sendMessageToClient(playerID, "Your turn to place a family member.");

            // 플레이어의 가족 구성원 배치와 카드 실행 로직
            // 좌표와 카드 정보는 프론트엔드에서 제공
            // int x = ...; // 프론트엔드에서 받은 x 좌표
            // int y = ...; // 프론트엔드에서 받은 y 좌표
            // CommonCard card = ...; // 프론트엔드에서 받은 카드 객체

            // player.placeFamilyMember(x, y, card);
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

    private void harvestPhase() {
        farmPhase();
        feedFamilyPhase();
        breedAnimalsPhase();
    }

    private void farmPhase() {
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
        notifyPlayers("농장 단계 완료. 가족 먹여살리기 단계로 진행하세요.");
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
        notifyPlayers("가족 먹여살리기 단계 완료. 가축 번식 단계로 진행하세요.");
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
        notifyPlayers("가축 번식 단계 완료. 수확 단계 종료.");
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
        for (Player player : players) {
            player.convertBabiesToAdults();
        }
        calculateAndRecordScores();
    }

    private void resetFamilyMembers() {
        for (Player player : players) {
            player.resetFamilyMembers();
        }
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
}
