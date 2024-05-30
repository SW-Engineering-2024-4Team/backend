package test.actionroundcardtest;

import cards.action.NonAccumulativeActionCard;
import cards.common.ActionRoundCard;
import cards.common.CommonCard;
import cards.factory.imp.action.PlowField;
import cards.majorimprovement.MajorImprovementCard;
import cards.majorimprovement.TestMajorImprovementCard;
import controllers.GameController;
import controllers.RoomController;
import enums.RoomType;
import models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import cards.factory.imp.round.PlantSeed;

public class PlantFieldTest {
    private GameController gameController;
    private Player player;
    private ActionRoundCard actionCard;
    private ActionRoundCard plowField;
    private ActionRoundCard plantSeed;
    private MajorImprovementCard majorImprovementCard;

    @BeforeEach
    public void setUp() {
        List<Player> players = createMockPlayers();
        RoomController roomController = new RoomController();
        roomController.handleGameStart("TestRoom123", players);
        gameController = roomController.getGameController();
        player = gameController.getPlayers().get(0);

        actionCard = new NonAccumulativeActionCard(1, "Plant Field Test Card", "This is a plant field test card.");
        gameController.getMainBoard().getActionCards().add(actionCard);
        plowField = new PlowField(2);
        plantSeed = new PlantSeed(3, 1);

        Map<String, Integer> purchaseCost = Map.of("clay", 3);
        Map<String, Integer> anytimeExchangeRate = Map.of("sheep", 1, "food", 2);
        Map<String, Integer> breadBakingExchangeRate = Map.of("grain", 1, "food", 2);

        majorImprovementCard = new TestMajorImprovementCard(1, "Test Hearth", "Allows baking bread.",
                purchaseCost, anytimeExchangeRate,
                breadBakingExchangeRate, 1, false);

        MainBoard mainBoard = gameController.getMainBoard();
        mainBoard.setActionCards(new ArrayList<>());
        mainBoard.setRoundCards(new ArrayList<>());

        mainBoard.getActionCards().add(plowField);
        mainBoard.getRoundCards().add(plantSeed);
        player.addResource("wood", 7);
        player.addResource("food", 7);
        player.addResource("clay", 7);
        player.addResource("stone", 7);
        majorImprovementCard.execute(player);
    }

    private List<Player> createMockPlayers() {
        List<Player> players = new ArrayList<>();
        GameController mockGameController = new GameController("TestRoom123", new RoomController(), players);
        for (int i = 1; i <= 4; i++) {
            players.add(new Player("player" + i, "Player " + i, mockGameController));
        }
        return players;
    }

    private void printPlayerBoardWithFields(String message) {
        System.out.println(message);
        Tile[][] tiles = player.getPlayerBoard().getTiles();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (tiles[i][j] instanceof FieldTile) {
                    FieldTile field = (FieldTile) tiles[i][j];
                    System.out.print("[F:" + field.getCrops() + "]");
                } else {
                    System.out.print("[ ]");
                }
            }
            System.out.println();
        }
    }

    public void printPlayerBoardWithFences(String message, Set<int[]> validPositions) {
        System.out.println(message);
        Tile[][] tiles = player.getPlayerBoard().getTiles();
        boolean[][][] fences = player.getPlayerBoard().getFences();
        int rows = tiles.length;
        int cols = tiles[0].length;
        int cellWidth = 5; // 각 셀의 너비를 고정

        // Print the top boundary of the board
        for (int i = 0; i < rows; i++) {
            // Print top fences
            for (int j = 0; j < cols; j++) {
                System.out.print("+");
                if (fences[i][j][0]) {
                    System.out.print("---");
                } else {
                    System.out.print("   ");
                }
            }
            System.out.println("+");

            // Print left fences and tiles
            for (int j = 0; j < cols; j++) {
                if (fences[i][j][2]) {
                    System.out.print("|");
                } else {
                    System.out.print(" ");
                }

                boolean isValidPosition = false;
                for (int[] pos : validPositions) {
                    if (pos[0] == i && pos[1] == j) {
                        isValidPosition = true;
                        break;
                    }
                }

                String tileString;
                if (tiles[i][j] == null) {
                    if (isValidPosition) {
                        tileString = "*";
                    } else {
                        tileString = "";
                    }
                } else if (tiles[i][j] instanceof Room) {
                    tileString = "r";
                } else if (tiles[i][j] instanceof Barn) {
                    tileString = "b";
                } else if (tiles[i][j] instanceof FieldTile) {
                    tileString = "f";
                } else {
                    tileString = "";
                }
                System.out.print(centerString(tileString, cellWidth));
            }
            if (fences[i][cols - 1][3]) {
                System.out.print("|");
            }
            System.out.println();
        }

        // Print the bottom boundary of the board
        for (int j = 0; j < cols; j++) {
            System.out.print("+");
            if (fences[rows - 1][j][1]) {
                System.out.print("---");
            } else {
                System.out.print("   ");
            }
        }
        System.out.println("+");
    }

    private String centerString(String str, int width) {
        int paddingSize = (width - str.length()) / 2;
        String padding = " ".repeat(paddingSize);
        return padding + str + padding;
    }

    private void printPlayerResources(String message) {
        System.out.println(message);
        for (Map.Entry<String, Integer> resource : player.getResources().entrySet()) {
            System.out.println("  " + resource.getKey() + ": " + resource.getValue());
        }
    }

    @Test
    public void testPlantField() {
        player.resetResources();
        player.addResource("grain", 5);  // 곡식 심기 위한 자원 추가

        // 초기 상태 출력
        printPlayerResources("Player resources before plowing and planting:");
        Set<int[]> validPositions = player.getPlayerBoard().getValidPlowPositions();
        printPlayerBoardWithFields("Player board before plowing field:");

        // 밭 일구기
        actionCard.plowField(player);
        validPositions = player.getPlayerBoard().getValidPlowPositions();
        printPlayerBoardWithFields("Player board after plowing first field:");
        printPlayerResources("Player resources after plowing field:");

        // 곡식 심기
        actionCard.plantField(player);
        printPlayerBoardWithFields("Player board after planting grain:");
        printPlayerResources("Player resources after planting grain:");

        // 검증
        Tile[][] tiles = player.getPlayerBoard().getTiles();
        boolean grainPlanted = false;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (tiles[i][j] instanceof FieldTile) {
                    FieldTile field = (FieldTile) tiles[i][j];
                    if (field.getCropType().equals("grain") && field.getCrops() == 3) {
                        grainPlanted = true;
                        break;
                    }
                }
            }
        }
        assertTrue(grainPlanted, "Grain should be planted in the field.");
    }

    @Test
    public void testPlowFieldWithPlowFieldCard() {
        Set<int[]> validPositions = player.getPlayerBoard().getValidPlowPositions();
        printPlayerBoardWithFences("Player board before plowing field:", validPositions);

        player.placeFamilyMember(plowField);

        // 상태 출력
        validPositions = player.getPlayerBoard().getValidPlowPositions();
        printPlayerBoardWithFences("Player board after plowing first field:", validPositions);

        player.placeFamilyMember(plowField);
        assertTrue(gameController.getMainBoard().isCardOccupied(plowField));
        gameController.resetFamilyMembers();
    }

    // TODO 카드 실행 조건이 안되는데도 카드가 점유되는 버그 발생
    @Test
    public void testPlantWithPlantSeed() {
        gameController.getMainBoard().revealRoundCard(1);
        player.resetResources();
        player.addResource("grain", 1);
        player.printPlayerResources("자원 상태");

        List<CommonCard> majorImprovementCards = player.getMajorImprovementCards();
        for (CommonCard card : majorImprovementCards) {
            System.out.println("majorImprovementCards = " + majorImprovementCards);
        }

        Set<int[]> validPositions = player.getPlayerBoard().getValidPlowPositions();
        printPlayerBoardWithFences("Player board before plowing field:", validPositions);

        player.placeFamilyMember(plowField);

        // 상태 출력
        validPositions = player.getPlayerBoard().getValidPlowPositions();
        printPlayerBoardWithFences("Player board after plowing first field:", validPositions);

        player.addResource("grain", 10);

        player.placeFamilyMember(plantSeed);
        player.printPlayerResources("자원 상태");


        assertTrue(gameController.getMainBoard().isCardOccupied(plowField));
        assertTrue(gameController.getMainBoard().isCardOccupied(plantSeed));
        gameController.resetFamilyMembers();

//        // 자원이 부족한 상태에서 밭 심으려고 시도
//        player.placeFamilyMember(plantSeed);
//        System.out.println("is card occupied? " + player.getGameController().getMainBoard().isCardOccupied(plantSeed));
//        assertTrue(!gameController.getMainBoard().isCardOccupied(plantSeed), "자원이 부족한 상태에서 밭 심으려고 시도");

//        // 빈 밭이 없는 상태에서 밭 심으려고 시도
//        player.addResource("grain", 1);
//        player.placeFamilyMember(plantSeed);
//        player.resetResources();
//
//        assertTrue(!gameController.getMainBoard().isCardOccupied(plantSeed), "카드가 사용되지 말아야 함.");




    }
}
