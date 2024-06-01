package test.actionroundcardtest;

import cards.action.NonAccumulativeActionCard;
import cards.action.TestAccumulativeActionCard;
import cards.common.ActionRoundCard;
import cards.factory.imp.round.BuildFence;
import cards.factory.imp.round.SheepMarket;
import cards.factory.imp.action.WanderingTheater;
import cards.factory.imp.action.Bush;
import cards.factory.imp.action.ClayMine;
import controllers.GameController;
import controllers.RoomController;
import models.*;
import cards.factory.imp.action.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ActionCardMixedTest {
    private GameController gameController;
    private Player player;
    private Player player2;
    private ActionRoundCard actionCard;
    private ActionRoundCard nonAccumulativeCard;
    private ActionRoundCard wanderingTheaterCard;
    private ActionRoundCard woodGatheringCard;
    private ActionRoundCard clayPitCard;
    private ActionRoundCard sheepMarket;
    private ActionRoundCard resourceMarket;
    private ActionRoundCard seed;
    private ActionRoundCard worker;
    private ActionRoundCard farmExpansion;
    private ActionRoundCard meetingPlace;
    private ActionRoundCard buildFence;
    @BeforeEach
    public void setUp() {
        List<Player> players = createMockPlayers();
        RoomController roomController = new RoomController();
        roomController.handleGameStart("TestRoom123", players);
        gameController = roomController.getGameController();
        player = gameController.getPlayers().get(0);
        player2 = gameController.getPlayers().get(1);

        Map<String, Integer> accumulatedAmounts = new HashMap<>();
        accumulatedAmounts.put("food", 1);
        accumulatedAmounts.put("wood", 2);

        actionCard = new TestAccumulativeActionCard(1);
        nonAccumulativeCard = new NonAccumulativeActionCard(2, "Non-Accumulative Test Card", "This is a non-accumulative test card.");
        wanderingTheaterCard = new WanderingTheater(3);
        woodGatheringCard = new Bush(4);
        clayPitCard = new ClayMine(5);
        sheepMarket = new SheepMarket(6, 1);
        resourceMarket = new ResourceMarket(7);
        seed = new Seed(8);
        worker = new Worker(9);
        farmExpansion = new FarmExpansion(10);
        meetingPlace = new MeetingPlace(11);


        // Initialize MainBoard
        MainBoard mainBoard = gameController.getMainBoard();
        mainBoard.setActionCards(new ArrayList<>());
        mainBoard.setRoundCards(new ArrayList<>());

        // Add cards to MainBoard
        mainBoard.getActionCards().add(actionCard);
        mainBoard.getActionCards().add(nonAccumulativeCard);
        mainBoard.getActionCards().add(wanderingTheaterCard);
        mainBoard.getActionCards().add(woodGatheringCard);
        mainBoard.getActionCards().add(clayPitCard);
        mainBoard.getRoundCards().add(sheepMarket);
        mainBoard.getRoundCards().add(resourceMarket);
        mainBoard.getActionCards().add(seed);
        mainBoard.getActionCards().add(worker);
        mainBoard.getActionCards().add(meetingPlace);
        mainBoard.getRoundCards().add((buildFence));

        // Reassign cards to ensure we have the same references
//        actionCard = mainBoard.getActionCards().stream().filter(card -> card.getId() == 1).findFirst().orElse(null);
//        nonAccumulativeCard = mainBoard.getActionCards().stream().filter(card -> card.getId() == 2).findFirst().orElse(null);
//        wanderingTheaterCard = mainBoard.getActionCards().stream().filter(card -> card.getId() == 3).findFirst().orElse(null);
//        woodGatheringCard = mainBoard.getActionCards().stream().filter(card -> card.getId() == 4).findFirst().orElse(null);
//        clayPitCard = mainBoard.getActionCards().stream().filter(card -> card.getId() == 5).findFirst().orElse(null);

    }

    private List<Player> createMockPlayers() {
        List<Player> players = new ArrayList<>();
        GameController mockGameController = new GameController("TestRoom123", new RoomController(), players);
        for (int i = 1; i <= 4; i++) {
            players.add(new Player("player" + i, "player" + i, mockGameController));
        }
        return players;
    }

    private void printPlayerResources(Player player, String message) {
        System.out.println(message);
        for (Map.Entry<String, Integer> resource : player.getResources().entrySet()) {
            System.out.println("  " + resource.getKey() + ": " + resource.getValue());
        }
    }

    private void printHouseTypes(String message) {
        System.out.println(message);
        Tile[][] tiles = player.getPlayerBoard().getTiles();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (tiles[i][j] instanceof Room) {
                    Room room = (Room) tiles[i][j];
                    switch (room.getType()) {
                        case WOOD:
                            System.out.print("[w]");
                            break;
                        case CLAY:
                            System.out.print("[c]");
                            break;
                        case STONE:
                            System.out.print("[s]");
                            break;
                    }
                } else {
                    System.out.print("[ ]");
                }
            }
            System.out.println();
        }
    }

    @Test
    public void testAndOrWithFarmExpansion() {
        player.resetResources();
        Set<int[]> validPositions = player.getPlayerBoard().getValidBarnPositions();
        player.getPlayerBoard().printPlayerBoardWithFences("농장확장 전", validPositions);

        printHouseTypes("농장확장 전");
        player.addResource("wood", 7);  // 방 만들기 자원 조건 충족을 위해 목재 자원 추가

        player.addResource("clay", 1);  // 외양간 짓기 자원 조건 충족을 위해 점토 자원 추가

        // 농장 확장 카드 실행
        player.placeFamilyMember(farmExpansion);

        validPositions = player.getPlayerBoard().getValidFencePositions();
        player.getPlayerBoard().printPlayerBoardWithFences("농장확장 후", validPositions);
        printHouseTypes("농장확장 후");

    }

    @Test
    public void testAndOrWithMeetingPlace() {
        List<Player> initTurnOrder = gameController.getTurnOrder();
        for (Player player : initTurnOrder) {
            System.out.println("player.getName() = " + player.getName());
        }

        // 플레이어 2가 회합 장소 카드 사용
        player2.placeFamilyMember(meetingPlace);

        // 플레이어 2가 turnOrder에 있는지 확인
        boolean playerExistsInTurnOrder = initTurnOrder.contains(player2);
        System.out.println("Player2 exists in turnOrder: " + playerExistsInTurnOrder);

        List<Player> nextTurnOrder = gameController.getTurnOrder();
        for (Player player : nextTurnOrder) {
            System.out.println("player.getName() = " + player.getName());
        }

        // 플레이어 2가 선 플레이어가 되었는지 확인
        assertEquals(player2, nextTurnOrder.get(0), player2.getName() + " should be the first player.");

        // 회합 장소 카드가 점유되었는지 확인
        assertTrue(meetingPlace.isOccupied(), "MeetingPlace card should be occupied after executing.");
    }




}




