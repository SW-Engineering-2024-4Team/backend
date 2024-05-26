package test.actionroundcardtest;

import cards.action.NonAccumulativeActionCard;
import cards.common.ActionRoundCard;
import controllers.GameController;
import controllers.RoomController;
import enums.RoomType;
import models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ConstructionTest {
    private GameController gameController;
    private Player player;
    private ActionRoundCard actionCard;

    @BeforeEach
    public void setUp() {
        List<Player> players = createMockPlayers();
        RoomController roomController = new RoomController();
        roomController.handleGameStart("TestRoom123", players);
        gameController = roomController.getGameController();
        player = gameController.getPlayers().get(0);

        actionCard = new NonAccumulativeActionCard(1, "Construction Test Card", "This is a construction test card.");
        gameController.getMainBoard().getActionCards().add(actionCard);
    }

    private List<Player> createMockPlayers() {
        List<Player> players = new ArrayList<>();
        GameController mockGameController = new GameController("TestRoom123", new RoomController(), players);
        for (int i = 1; i <= 4; i++) {
            players.add(new Player("player" + i, "Player " + i, mockGameController));
        }
        return players;
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

    // 지정한 위치의 타일 울타리 상태를 출력하는 메서드
    public void printTileFenceInfo(int x, int y) {
        boolean[][][] fences = player.getPlayerBoard().getFences();
        System.out.println("Fence information for tile at (" + x + ", " + y + "):");
        System.out.println("  Top: " + (fences[x][y][0] ? "Present" : "Absent"));
        System.out.println("  Bottom: " + (fences[x][y][1] ? "Present" : "Absent"));
        System.out.println("  Left: " + (fences[x][y][2] ? "Present" : "Absent"));
        System.out.println("  Right: " + (fences[x][y][3] ? "Present" : "Absent"));
    }

    private boolean isValidTile(int x, int y) {
        return x >= 0 && x < player.getPlayerBoard().getTiles().length && y >= 0 && y < player.getPlayerBoard().getTiles()[0].length;
    }


    private void buildHouse() {
        Set<int[]> validPositions = player.getPlayerBoard().getValidHousePositions();
        printPlayerBoardWithFences("Player board before building house:", validPositions);
        actionCard.buildHouse(player);
        validPositions = player.getPlayerBoard().getValidHousePositions();
        printPlayerBoardWithFences("Player board after building house:", validPositions);
    }

    private void plowField() {
        Set<int[]> validPositions = player.getPlayerBoard().getValidPlowPositions();
        printPlayerBoardWithFences("Player board before plowing field:", validPositions);
        actionCard.plowField(player);
        validPositions = player.getPlayerBoard().getValidPlowPositions();
        printPlayerBoardWithFences("Player board after plowing field:", validPositions);
    }

    private void buildBarn() {
        Set<int[]> validPositions = player.getPlayerBoard().getValidBarnPositions();
        printPlayerBoardWithFences("Player board before building barn:", validPositions);
        actionCard.buildBarn(player);
        validPositions = player.getPlayerBoard().getValidBarnPositions();
        printPlayerBoardWithFences("Player board after building barn:", validPositions);
    }

    @Test
    public void testBuildHouse() {
        player.resetResources();
        player.addResource("wood", 20);
        // 초기 상태 출력
        Set<int[]> validPositions = player.getPlayerBoard().getValidHousePositions();
        printPlayerBoardWithFences("Player board before building house:", validPositions);

        // 첫 번째 집 짓기
        actionCard.buildHouse(player);

        // 상태 출력
        validPositions = player.getPlayerBoard().getValidHousePositions();
        printPlayerBoardWithFences("Player board after building first house:", validPositions);

        // 두 번째 집 짓기
        actionCard.buildHouse(player);

        // 상태 출력
        validPositions = player.getPlayerBoard().getValidHousePositions();
        printPlayerBoardWithFences("Player board after building second house:", validPositions);

        // 검증
        Tile[][] tiles = player.getPlayerBoard().getTiles();
        int houseCount = 0;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (tiles[i][j] instanceof Room) {
                    houseCount++;
                }
            }
        }
        assertEquals(4, houseCount, "There should be four rooms (houses) built on the board.");
    }

    @Test
    public void testRenovateRooms() {
        player.resetResources();
        player.addResource("wood", 5);  // 초기에 나무로 된 집을 짓기 위한 자원 추가
        player.addResource("clay", 10); // 흙으로 집을 업그레이드하기 위한 자원 추가

        // 초기 상태 출력
        Set<int[]> validPositions = player.getPlayerBoard().getValidHousePositions();
        printHouseTypes("Player board before building and renovating house:");
        printPlayerBoardWithFences("Player board before building and renovating house:", validPositions);
        printPlayerResources("Player resources before renovating house.");

        // 나무 집 짓기
        actionCard.buildHouse(player);
        validPositions = player.getPlayerBoard().getValidHousePositions();
        printHouseTypes("Player board after building wooden house:");
        printPlayerBoardWithFences("Player board after building wooden house:", validPositions);
        printPlayerResources("Player resources after building house.");

        // 흙 집으로 업그레이드
        actionCard.renovateRooms(player);

        // 업그레이드 후 상태 출력
        printHouseTypes("Player board after renovating to clay house:");
        printPlayerBoardWithFences("Player board after renovating to clay house:", validPositions);
        printPlayerResources("Player resources after renovating to clay house.");

        // 검증
        Tile[][] tiles = player.getPlayerBoard().getTiles();
        boolean allRoomsUpgraded = true;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (tiles[i][j] instanceof Room) {
                    Room room = (Room) tiles[i][j];
                    if (room.getType() != RoomType.CLAY) {
                        allRoomsUpgraded = false;
                        break;
                    }
                }
            }
        }
        assertTrue(allRoomsUpgraded, "All rooms should be upgraded to clay house.");
    }

    @Test
    public void testRenovateHouseMultipleStages() {
        player.resetResources();
        player.addResource("wood", 10);  // 초기에 나무로 된 집을 짓기 위한 자원 추가
        player.addResource("clay", 10);  // 흙으로 집을 업그레이드하기 위한 자원 추가
        player.addResource("stone", 10); // 돌로 집을 업그레이드하기 위한 자원 추가

        // 초기 상태 출력
        Set<int[]> validPositions = player.getPlayerBoard().getValidHousePositions();
        printHouseTypes("Player board before building and renovating house:");
        printPlayerBoardWithFences("Player board before building and renovating house:", validPositions);
        printPlayerResources("Player resources before renovating house.");

        // 나무 집 짓기
        actionCard.buildHouse(player);
        validPositions = player.getPlayerBoard().getValidHousePositions();
        printHouseTypes("Player board after building wooden house:");
        printPlayerBoardWithFences("Player board after building wooden house:", validPositions);
        printPlayerResources("Player resources after building house.");

        // 흙 집으로 업그레이드
        actionCard.renovateRooms(player);
        printHouseTypes("Player board after renovating to clay house:");
        printPlayerBoardWithFences("Player board after renovating to clay house:", validPositions);
        printPlayerResources("Player resources after renovating to clay house.");

        // 흙 집 짓기
        actionCard.buildHouse(player);
        validPositions = player.getPlayerBoard().getValidHousePositions();
        printHouseTypes("Player board after building another clay house:");
        printPlayerBoardWithFences("Player board after building another clay house:", validPositions);
        printPlayerResources("Player resources after building another clay house.");

        // 돌 집으로 업그레이드
        actionCard.renovateRooms(player);
        printHouseTypes("Player board after renovating to stone house:");
        printPlayerBoardWithFences("Player board after renovating to stone house:", validPositions);
        printPlayerResources("Player resources after renovating to stone house.");

        // 돌 집에서 더 이상 업그레이드할 수 없는 경우
        actionCard.renovateRooms(player);
        printHouseTypes("Player board after attempting to renovate stone house:");
        printPlayerBoardWithFences("Player board after attempting to renovate stone house:", validPositions);
        printPlayerResources("Player resources after attempting to renovate stone house.");

        // 검증
        Tile[][] tiles = player.getPlayerBoard().getTiles();
        boolean allRoomsUpgradedToStone = true;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (tiles[i][j] instanceof Room) {
                    Room room = (Room) tiles[i][j];
                    if (room.getType() != RoomType.STONE) {
                        allRoomsUpgradedToStone = false;
                        break;
                    }
                }
            }
        }
        assertTrue(allRoomsUpgradedToStone, "All rooms should be upgraded to stone house.");
    }




    @Test
    public void testBuildBarn() {
        player.resetResources();
        player.addResource("wood", 4);
        // 초기 상태 출력
        printPlayerResources("Player Resources before building barn.");
        Set<int[]> validPositions = player.getPlayerBoard().getValidBarnPositions();
        printPlayerBoardWithFences("Player board before building barn:", validPositions);

        // 첫 번째 외양간 짓기
        actionCard.buildBarn(player);

        // 상태 출력
        validPositions = player.getPlayerBoard().getValidBarnPositions();
        printPlayerBoardWithFences("Player board after building first barn:", validPositions);

        // 두 번째 외양간 짓기
        actionCard.buildBarn(player);

        // 상태 출력
        validPositions = player.getPlayerBoard().getValidBarnPositions();
        printPlayerBoardWithFences("Player board after building second barn:", validPositions);
        printPlayerResources("Player resources after building second barn:");

        // 세 번째 외양간 짓기(자원이 부족하여 지을 수 없음)
        actionCard.buildBarn(player);
        printPlayerBoardWithFences("Player board after building third barn:", validPositions);
        printPlayerResources("Player resources after trying  to build third barn:");

        // 검증
        Tile[][] tiles = player.getPlayerBoard().getTiles();
        int barnCount = 0;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (tiles[i][j] instanceof Barn) {
                    barnCount++;
                }
            }
        }
        assertEquals(2, barnCount, "There should be two barns built on the board.");
    }


    @Test
    public void testPlowField() {
        // 초기 상태 출력
        Set<int[]> validPositions = player.getPlayerBoard().getValidPlowPositions();
        printPlayerBoardWithFences("Player board before plowing field:", validPositions);

        // 첫 번째 밭 일구기
        actionCard.plowField(player);

        // 상태 출력
        validPositions = player.getPlayerBoard().getValidPlowPositions();
        printPlayerBoardWithFences("Player board after plowing first field:", validPositions);

        // 두 번째 밭 일구기
        actionCard.plowField(player);

        // 상태 출력
        validPositions = player.getPlayerBoard().getValidPlowPositions();
        printPlayerBoardWithFences("Player board after plowing second field:", validPositions);

        // 검증
        Tile[][] tiles = player.getPlayerBoard().getTiles();
        int fieldCount = 0;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (tiles[i][j] instanceof FieldTile) {
                    fieldCount++;
                }
            }
        }
        assertEquals(2, fieldCount, "There should be two fields plowed on the board.");
    }

    // 집2,밭3, 외양간4을 랜덤하게 짓는 테스트 코드
    @Test
    public void testRandomConstruction() {
        player.resetResources();
        player.addResource("wood", 30);
        Random random = new Random();
        int housesBuilt = 0;
        int fieldsPlowed = 0;
        int barnsBuilt = 0;

        while (housesBuilt < 2 || fieldsPlowed < 3 || barnsBuilt < 4) {
            int choice = random.nextInt(3);

            if (choice == 0 && housesBuilt < 2) {
                buildHouse();
                housesBuilt++;
            } else if (choice == 1 && fieldsPlowed < 3) {
                plowField();
                fieldsPlowed++;
            } else if (choice == 2 && barnsBuilt < 4) {
                buildBarn();
                barnsBuilt++;
            }
        }

        // 최종 검증
        Tile[][] tiles = player.getPlayerBoard().getTiles();
        int houseCount = 0, fieldCount = 0, barnCount = 0;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (tiles[i][j] instanceof Room) houseCount++;
                else if (tiles[i][j] instanceof FieldTile) fieldCount++;
                else if (tiles[i][j] instanceof Barn) barnCount++;
            }
        }

        assertEquals(4, houseCount, "There should be two houses built on the board.");
        assertEquals(3, fieldCount, "There should be three fields plowed on the board.");
        assertEquals(4, barnCount, "There should be four barns built on the board.");
    }

    // 외양간 짓고 유효한 울타리 위치 확인
    @Test
    public void testValidFencePositionsWithBarns() {
        // 플레이어의 자원 설정
        player.resetResources();
        player.addResource("wood", 4); // 외양간을 짓기 위한 나무 자원 추가

        // 초기 상태 출력
        printPlayerResources("Resources before building barns:");
        Set<int[]> validPositions = player.getPlayerBoard().getValidBarnPositions();
        printPlayerBoardWithFences("Player board before building barns:", validPositions);

        // 외양간 2개 짓기
        actionCard.buildBarn(player);
        actionCard.buildBarn(player);

        // 외양간을 지은 후 상태 출력
        printPlayerResources("Resources after building barns:");
        validPositions = player.getPlayerBoard().getValidBarnPositions();
        printPlayerBoardWithFences("Player board after building barns:", validPositions);

        // 울타리 유효 위치 확인
        validPositions = player.getPlayerBoard().getValidFencePositions();
        printPlayerBoardWithFences("Player board with valid fence positions:", validPositions);

        // 검증
        // 울타리 유효 위치 검증 추가
        boolean validFencePositionFound = false;
        for (int[] pos : validPositions) {
            if (player.getPlayerBoard().getTiles()[pos[0]][pos[1]] instanceof Barn) {
                validFencePositionFound = true;
                break;
            }
        }
        assertTrue(validFencePositionFound, "There should be valid fence positions around the barns.");
    }

    @Test
    public void testBuildFence() {
        player.resetResources();
        player.addResource("wood", 20);

        // Initial state
        printPlayerResources("Resources before building fence:");
        Set<int[]> validPositions = player.getPlayerBoard().getValidFencePositions();
        printPlayerBoardWithFences("Player board before building fence:", validPositions);

        // Build fence
        List<int[]> selectedPositions = Arrays.asList(
                new int[]{1, 1},
                new int[]{1, 2},
                new int[]{2, 2}
        );
        player.getPlayerBoard().buildFences(selectedPositions, player);

        // State after building fence
        printPlayerResources("Resources after building fence:");
        validPositions = player.getPlayerBoard().getValidFencePositions();
        printPlayerBoardWithFences("Player board after building fence:", validPositions);

        printTileFenceInfo(1, 1);
        printTileFenceInfo(1, 2);
        printTileFenceInfo(2, 2);

        // Verification
        boolean fenceBuilt = false;
        for (boolean[][] row : player.getPlayerBoard().getFences()) {
            for (boolean[] tile : row) {
                for (boolean hasFence : tile) {
                    if (hasFence) {
                        fenceBuilt = true;
                        break;
                    }
                }
                if (fenceBuilt) break;
            }
            if (fenceBuilt) break;
        }
        assertTrue(fenceBuilt, "A fence should be built at the expected position.");
    }

    @Test
    public void testValidFencePositionsWithBarnsAndOneFence() {
        // 플레이어의 자원 설정
        player.resetResources();
        player.addResource("wood", 12); // 외양간을 짓기 위한 나무 자원 추가 및 울타리를 위한 추가 자원

        // 초기 상태 출력
        printPlayerResources("Resources before building barns:");
        Set<int[]> validPositions = player.getPlayerBoard().getValidBarnPositions();
        printPlayerBoardWithFences("Player board before building barns:", validPositions);

        // 외양간 2개 짓기
        player.getPlayerBoard().buildBarn(1, 1);
        player.getPlayerBoard().buildBarn(1, 2);

        // 외양간을 지은 후 상태 출력
        printPlayerResources("Resources after building barns:");
        validPositions = player.getPlayerBoard().getValidBarnPositions();
        printPlayerBoardWithFences("Player board after building barns:", validPositions);

        // 울타리 유효 위치 확인
        validPositions = player.getPlayerBoard().getValidFencePositions();
        printPlayerBoardWithFences("Player board with valid fence positions:", validPositions);

        // 울타리 위치 설정
        List<int[]> fencePositions = Arrays.asList(
                new int[]{1, 1},
                new int[]{1, 2},
                new int[]{1, 3}
        );
        player.getPlayerBoard().buildFences(fencePositions, player);

        // 울타리를 지은 후 상태 출력
        printPlayerResources("Resources after building fences:");
        validPositions = player.getPlayerBoard().getValidFencePositions();
        printPlayerBoardWithFences("Player board after building fences:", validPositions);

        printTileFenceInfo(1, 1);
        printTileFenceInfo(1, 2);
        printTileFenceInfo(1, 3);
        printTileFenceInfo(0, 1);
        printTileFenceInfo(2, 1);
        printTileFenceInfo(1, 0);
        printTileFenceInfo(1, 4);

        // 검증
        boolean validFencePositionFound = false;
        for (int[] pos : validPositions) {
            if (player.getPlayerBoard().getTiles()[pos[0]][pos[1]] == null || player.getPlayerBoard().getTiles()[pos[0]][pos[1]] instanceof Barn) {
                validFencePositionFound = true;
                break;
            }
        }
        assertTrue(validFencePositionFound, "There should be valid fence positions around the barns and the first fence.");
    }

    @Test
    public void testValidFencePositionsWithOneFenceAndBarns() {
        // 플레이어의 자원 설정
        player.resetResources();
        player.addResource("wood", 12); // 외양간을 짓기 위한 나무 자원 추가 및 울타리를 위한 추가 자원

        // 초기 상태 출력
        printPlayerResources("Resources before building barns:");
        Set<int[]> validPositions = player.getPlayerBoard().getValidBarnPositions();
        printPlayerBoardWithFences("Player board before building barns:", validPositions);

        // 울타리 유효 위치 확인
        validPositions = player.getPlayerBoard().getValidFencePositions();
        printPlayerBoardWithFences("Player board with valid fence positions:", validPositions);

        // 울타리 위치 설정
        List<int[]> fencePositions = Arrays.asList(
                new int[]{1, 1},
                new int[]{1, 2},
                new int[]{1, 3}
        );
        player.getPlayerBoard().buildFences(fencePositions, player);

        // 울타리를 지은 후 상태 출력
        printPlayerResources("Resources after building fences:");
        validPositions = player.getPlayerBoard().getValidFencePositions();
        printPlayerBoardWithFences("Player board after building fences:", validPositions);

        // 외양간 2개 짓기
        player.getPlayerBoard().buildBarn(1, 1);
        player.getPlayerBoard().buildBarn(1, 2);

        // 외양간을 지은 후 상태 출력
        printPlayerResources("Resources after building barns:");
        validPositions = player.getPlayerBoard().getValidBarnPositions();
        printPlayerBoardWithFences("Player board after building barns:", validPositions);

        printTileFenceInfo(1, 1);
        printTileFenceInfo(1, 2);
        printTileFenceInfo(1, 3);
        printTileFenceInfo(0, 1);
        printTileFenceInfo(2, 1);
        printTileFenceInfo(1, 0);
        printTileFenceInfo(1, 4);

        // 검증
        boolean validFencePositionFound = false;
        for (int[] pos : validPositions) {
            if (player.getPlayerBoard().getTiles()[pos[0]][pos[1]] == null || player.getPlayerBoard().getTiles()[pos[0]][pos[1]] instanceof Barn) {
                validFencePositionFound = true;
                break;
            }
        }
        assertTrue(validFencePositionFound, "There should be valid fence positions around the barns and the first fence.");
    }

    @Test
    public void testBarnAndFenceInteraction() {
        // 자원 설정
        player.resetResources();
        player.addResource("wood", 20); // 외양간을 짓기 위한 나무 자원 추가 및 울타리를 위한 추가 자원

        // 초기 상태 출력
        printPlayerResources("Resources before building barns:");
        Set<int[]> validPositions = player.getPlayerBoard().getValidBarnPositions();
        printPlayerBoardWithFences("Player board before building barns:", validPositions);

        // 외양간 2개 짓기
        player.getPlayerBoard().buildBarn(1, 1);
        player.getPlayerBoard().buildBarn(1, 2);
        player.addResource("wood", -4);

        // 외양간을 지은 후 상태 출력
        printPlayerResources("Resources after building barns:");
        validPositions = player.getPlayerBoard().getValidBarnPositions();
        printPlayerBoardWithFences("Player board after building barns:", validPositions);

        // 울타리 유효 위치 확인
        validPositions = player.getPlayerBoard().getValidFencePositions();
        printPlayerBoardWithFences("Player board with valid fence positions:", validPositions);

        // 울타리 위치 설정
        List<int[]> fencePositions = Arrays.asList(
                new int[]{1, 1},
                new int[]{1, 2},
                new int[]{1, 3}
        );
        player.getPlayerBoard().buildFences(fencePositions, player);

        // 울타리를 지은 후 상태 출력
        printPlayerResources("Resources after building fences:");
        validPositions = player.getPlayerBoard().getValidFencePositions();
        printPlayerBoardWithFences("Player board after building fences:", validPositions);

        validPositions = player.getPlayerBoard().getValidFencePositions();
        System.out.println("Valid positions:");
        for (int[] pos : validPositions) {
            System.out.println("Tile at (" + pos[0] + ", " + pos[1] + ")");
        }

        // 동물 배치
        Animal sheep1 = new Animal(1, 1, "sheep");
        player.getPlayerBoard().addAnimalToBoard(sheep1, 1, 1);

        Animal sheep2 = new Animal(1, 2, "sheep");
        player.getPlayerBoard().addAnimalToBoard(sheep2, 1, 2);

        Animal sheep3 = new Animal(1, 2, "sheep");
        player.getPlayerBoard().addAnimalToBoard(sheep3, 1, 2);

        Animal sheep4 = new Animal(1, 2, "sheep");
        player.getPlayerBoard().addAnimalToBoard(sheep4, 1, 2);

        // 동물을 배치한 후 상태 출력
        validPositions = player.getPlayerBoard().getValidAnimalPositions("sheep");
        printPlayerBoardWithFences("Player board after adding animals:", validPositions);
        validPositions = player.getPlayerBoard().getValidFencePositions();
        System.out.println("Valid positions:");
        for (int[] pos : validPositions) {
            System.out.println("Tile at (" + pos[0] + ", " + pos[1] + ")");
        }

        // 수용 능력 확인
        int capacity = player.getPlayerBoard().getAnimalCapacity();
        System.out.println("Animal capacity after adding animals and building barn inside fenced area: " + capacity);

        // 울타리 영역 디버깅 정보 출력
        player.getPlayerBoard().printFenceAreas();

        // 검증
        assertEquals(28, capacity, "The capacity should be 32 after building barns inside the fenced area and adding animals.");
    }

    // 외양간을 짓고 울타리를 두 개 짓는 테스트
    @Test
    public void testBarnAndFenceInteraction2() {
        // 자원 설정
        player.resetResources();
        player.addResource("wood", 20); // 외양간을 짓기 위한 나무 자원 추가 및 울타리를 위한 추가 자원

        // 초기 상태 출력
//        printPlayerResources("Resources before building barns:");
        Set<int[]> validPositions = player.getPlayerBoard().getValidBarnPositions();
        printPlayerBoardWithFences("Player board before building barns:", validPositions);

        // 외양간 2개 짓기
        player.getPlayerBoard().buildBarn(1, 1);
        player.getPlayerBoard().buildBarn(1, 2);
        player.addResource("wood", -4);

        // 외양간을 지은 후 상태 출력
        printPlayerResources("Resources after building barns:");
        validPositions = player.getPlayerBoard().getValidBarnPositions();
        printPlayerBoardWithFences("Player board after building barns:", validPositions);

        // 울타리 유효 위치 확인
        validPositions = player.getPlayerBoard().getValidFencePositions();
        printPlayerBoardWithFences("Player board with valid fence positions:", validPositions);

        // 첫 번째 울타리 위치 설정
        List<int[]> fencePositions = Arrays.asList(
                new int[]{1, 1},
                new int[]{1, 2},
                new int[]{1, 3}
        );
        player.getPlayerBoard().buildFences(fencePositions, player);

        // 첫 번째 울타리를 지은 후 상태 출력
        printPlayerResources("Resources after building first set of fences:");
        validPositions = player.getPlayerBoard().getValidFencePositions();
        printPlayerBoardWithFences("Player board after building first set of fences:", validPositions);

//        // 외양간 1개 짓기
//        player.getPlayerBoard().buildBarn(2, 2);

        // 두 번째 울타리 위치 설정
        List<int[]> secondFencePositions = Arrays.asList(
                new int[]{2, 2},
                new int[]{2, 3}
        );
        player.getPlayerBoard().buildFences(secondFencePositions, player);

        // 두 번째 울타리를 지은 후 상태 출력
        printPlayerResources("Resources after building second set of fences:");
        validPositions = player.getPlayerBoard().getValidFencePositions();
        printPlayerBoardWithFences("Player board after building second set of fences:", validPositions);

        // 외양간 1개 짓기
        player.getPlayerBoard().buildBarn(2, 2);

        // 동물 배치
        Animal sheep1 = new Animal(1, 1, "sheep");
        player.getPlayerBoard().addAnimalToBoard(sheep1, 1, 1);

        Animal sheep2 = new Animal(1, 2, "sheep");
        player.getPlayerBoard().addAnimalToBoard(sheep2, 1, 2);

        Animal sheep3 = new Animal(1, 2, "sheep");
        player.getPlayerBoard().addAnimalToBoard(sheep3, 1, 2);

        Animal sheep4 = new Animal(1, 2, "sheep");
        player.getPlayerBoard().addAnimalToBoard(sheep4, 1, 2);

        // 동물을 배치한 후 상태 출력
        printPlayerResources("Resources after adding animals:");

        // 수용 능력 확인
        int capacity = player.getPlayerBoard().getAnimalCapacity();
        System.out.println("Animal capacity after adding animals and building barn inside fenced area: " + capacity);

        // 울타리 영역 디버깅 정보 출력
        player.getPlayerBoard().printFenceAreas();

        // 검증
        assertEquals(32, capacity, "The capacity should be 32 after building barns inside the fenced area and adding animals.");

        // 울타리 영역 확인
        Set<FenceArea> managedAreas = player.getPlayerBoard().getManagedFenceAreas();
        assertEquals(2, managedAreas.size(), "There should be two distinct fence areas.");

        Iterator<FenceArea> areaIterator = managedAreas.iterator();
        FenceArea area1 = areaIterator.next();
        FenceArea area2 = areaIterator.next();

        if (area1.calculateInitialCapacity() == 32) {
            assertEquals(32, area1.calculateInitialCapacity(), "The init capacity of the first fence area should be 32.");
            assertEquals(8, area2.calculateInitialCapacity(), "The init capacity of the second fence area should be 8.");
        } else {
            assertEquals(32, area2.calculateInitialCapacity(), "The init capacity of the first fence area should be 32.");
            assertEquals(8, area1.calculateInitialCapacity(), "The init capacity of the second fence area should be 8.");
        }

    }

    // 수용 능력을 초과해서 동물을 넣을 수 있는지 없는지 확인하는 테스트
    @Test
    public void testAddingAnimalsToFenceArea() {
        // 자원 설정
        player.resetResources();
        player.addResource("wood", 20); // 울타리를 위한 자원 추가

        // 초기 상태 출력
        Set<int[]> validPositions = player.getPlayerBoard().getValidBarnPositions();
        printPlayerBoardWithFences("Player board before building barns:", validPositions);

        // 외양간 1개 짓기
        player.getPlayerBoard().buildBarn(1, 1);

        // 동물 배치
        Animal sheep1 = new Animal(1, 1, "sheep");
        player.getPlayerBoard().addAnimalToBoard(sheep1, 1, 1);

        Animal sheep2 = new Animal(1, 1, "sheep");
        player.getPlayerBoard().addAnimalToBoard(sheep2, 1, 2);

        Animal sheep3 = new Animal(1, 2, "sheep");
        player.getPlayerBoard().addAnimalToBoard(sheep3, 1, 2);


        validPositions = player.getPlayerBoard().getValidFencePositions();
        List<int[]> fencePos = Arrays.asList(
                new int[]{2, 2}
        );
        printPlayerBoardWithFences("Player board before building fences", validPositions);
        player.getPlayerBoard().buildFences(fencePos, player);
        validPositions = player.getPlayerBoard().getValidFencePositions();
        printPlayerBoardWithFences("Player board after building fences.", validPositions);

        // 추가 테스트: 울타리 (2,2)에 양을 3마리 추가
        Animal sheep4 = new Animal(2, 2, "sheep");
        player.getPlayerBoard().addAnimalToBoard(sheep4, 2, 2);

        Animal sheep5 = new Animal(2, 2, "sheep");
        player.getPlayerBoard().addAnimalToBoard(sheep5, 2, 2);

        Animal sheep6 = new Animal(2, 2, "sheep");
        player.getPlayerBoard().addAnimalToBoard(sheep6, 2, 2);

        // 최종 울타리 영역 디버깅 정보 출력
        player.getPlayerBoard().printFenceAreas();
    }



}




