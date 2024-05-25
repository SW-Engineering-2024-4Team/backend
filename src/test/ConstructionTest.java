package test;

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

    //    private void printPlayerBoard(String message, Set<int[]> validPositions) {
//        System.out.println(message);
//        Tile[][] tiles = player.getPlayerBoard().getTiles();
//        for (int i = 0; i < tiles.length; i++) {
//            for (int j = 0; j < tiles[i].length; j++) {
//                boolean isValidPosition = false;
//                for (int[] pos : validPositions) {
//                    if (pos[0] == i && pos[1] == j) {
//                        isValidPosition = true;
//                        break;
//                    }
//                }
//                if (tiles[i][j] == null) {
//                    if (isValidPosition) {
//                        System.out.print("[*]");
//                    } else {
//                        System.out.print("[ ]");
//                    }
//                } else if (tiles[i][j] instanceof Room) {
//                    System.out.print("[R]");
//                } else if (tiles[i][j] instanceof Barn) {
//                    System.out.print("[B]");
//                } else if (tiles[i][j] instanceof FieldTile) {
//                    System.out.print("[F]");
//                }
//            }
//            System.out.println();
//        }
//    }
    private void printPlayerBoardWithFences(String message, Set<int[]> validPositions) {
        System.out.println(message);
        Tile[][] tiles = player.getPlayerBoard().getTiles();
        boolean[][] fences = player.getPlayerBoard().getFences();
        int rows = tiles.length;
        int cols = tiles[0].length;

        // Print top border of the board
        System.out.print("   ");
        for (int j = 0; j < cols; j++) {
            System.out.print("   " + countFence(fences, -1, j) + "   ");
        }
        System.out.println();

        for (int i = 0; i < rows; i++) {
            // Print left border of the row
            System.out.print(countFence(fences, i, -1) + " ");

            // Print tiles and vertical fences
            for (int j = 0; j < cols; j++) {
                boolean isValidPosition = false;
                for (int[] pos : validPositions) {
                    if (pos[0] == i && pos[1] == j) {
                        isValidPosition = true;
                        break;
                    }
                }

                if (tiles[i][j] == null) {
                    if (isValidPosition) {
                        System.out.print("[*]");
                    } else {
                        System.out.print("[  ]");
                    }
                } else if (tiles[i][j] instanceof Room) {
                    System.out.print("[R]");
                } else if (tiles[i][j] instanceof Barn) {
                    System.out.print("[B]");
                } else if (tiles[i][j] instanceof FieldTile) {
                    System.out.print("[F]");
                }

                if (j < cols - 1) {
                    System.out.print(" " + countFence(fences, i, j) + " ");
                }
            }

            // Print right border of the row
            System.out.print(" " + countFence(fences, i, cols - 1));
            System.out.println();

            // Print horizontal fences
            if (i < rows - 1) {
                System.out.print("   ");
                for (int j = 0; j < cols; j++) {
                    System.out.print("   " + countFence(fences, i, j) + "   ");
                }
                System.out.println();
            }
        }

        // Print bottom border of the board
        System.out.print("   ");
        for (int j = 0; j < cols; j++) {
            System.out.print("   " + countFence(fences, rows - 1, j) + "   ");
        }
        System.out.println();
    }

    private int countFence(boolean[][] fences, int row, int col) {
        int count = 0;
        if (row >= 0 && col >= 0 && row < fences.length && col < fences[0].length) {
            if (fences[row][col]) count++;
        }
        if (row >= 0 && row < fences.length && col - 1 >= 0 && col - 1 < fences[0].length) {
            if (fences[row][col - 1]) count++;
        }
        if (row - 1 >= 0 && row - 1 < fences.length && col >= 0 && col < fences[0].length) {
            if (fences[row - 1][col]) count++;
        }
        if (row >= 0 && row < fences.length && col + 1 >= 0 && col + 1 < fences[0].length) {
            if (fences[row][col + 1]) count++;
        }
        return count;
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
                            System.out.print("[W]");
                            break;
                        case CLAY:
                            System.out.print("[C]");
                            break;
                        case STONE:
                            System.out.print("[S]");
                            break;
                    }
                } else {
                    System.out.print("[ ]");
                }
            }
            System.out.println();
        }
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

//    @Test
//    public void testBuildFence() {
//        // 자원 설정
//        player.resetResources();
//        player.addResource("wood", 10);
//
//        // 초기 상태 출력
//        printPlayerResources("Resources before building fence:");
//        Set<int[]> validPositions = player.getPlayerBoard().getValidFencePositions();
//        printPlayerBoardWithFences("Player board before building fence:", validPositions);
//
//        // 울타리 짓기
//        actionCard.buildFence(player);
//
//        // 상태 출력
//        printPlayerResources("Resources after building fence:");
//        validPositions = player.getPlayerBoard().getValidFencePositions();
//        printPlayerBoardWithFences("Player board after building fence:", validPositions);
//
//        // 검증
//        // 울타리 상태 검증 추가
//        boolean fenceBuilt = false;
//        for (boolean[] row : player.getPlayerBoard().getFences()) {
//            for (boolean hasFence : row) {
//                if (hasFence) {
//                    fenceBuilt = true;
//                    break;
//                }
//            }
//            if (fenceBuilt) break;
//        }
//        assertTrue(fenceBuilt, "A fence should be built at the expected position.");
//    }

    // TODO 펜스 짓기
    @Test
    public void testBuildFence() {
        // 자원 설정
        player.resetResources();
        player.addResource("wood", 10);

        // 초기 상태 출력
        printPlayerResources("Resources before building fence:");
        Set<int[]> validPositions = player.getPlayerBoard().getValidFencePositions();
        printPlayerBoardWithFences("Player board before building fence:", validPositions);

        // 울타리 짓기
        actionCard.buildFence(player);

        // 상태 출력
        printPlayerResources("Resources after building fence:");
        validPositions = player.getPlayerBoard().getValidFencePositions();
        printPlayerBoardWithFences("Player board after building fence:", validPositions);

        // 검증
        // 울타리 상태 검증 추가
        boolean fenceBuilt = false;
        for (boolean[] row : player.getPlayerBoard().getFences()) {
            for (boolean hasFence : row) {
                if (hasFence) {
                    fenceBuilt = true;
                    break;
                }
            }
            if (fenceBuilt) break;
        }
        assertTrue(fenceBuilt, "A fence should be built at the expected position.");
    }


    // 외양간 짓고 울타리 짓기
    @Test
    public void testValidFencePositionsWithBarnsAndOneFence() {
        // 플레이어의 자원 설정
        player.resetResources();
        player.addResource("wood", 6); // 외양간을 짓기 위한 나무 자원 추가 및 울타리를 위한 추가 자원

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

        // 첫 번째 울타리 짓기
        List<int[]> fencePositions = new ArrayList<>();
        int[] firstFencePosition = validPositions.iterator().next(); // 첫 번째 유효 위치 선택
        fencePositions.add(firstFencePosition);
        player.getPlayerBoard().buildFences(fencePositions);

        // 첫 번째 울타리를 지은 후 상태 출력
        printPlayerResources("Resources after building first fence:");
        validPositions = player.getPlayerBoard().getValidFencePositions();
        printPlayerBoardWithFences("Player board after building first fence:", validPositions);

        // 검증
        // 울타리 유효 위치 검증 추가
        boolean validFencePositionFound = false;
        for (int[] pos : validPositions) {
            if (player.getPlayerBoard().getTiles()[pos[0]][pos[1]] == null || player.getPlayerBoard().getTiles()[pos[0]][pos[1]] instanceof Barn) {
                validFencePositionFound = true;
                break;
            }
        }
        assertTrue(validFencePositionFound, "There should be valid fence positions around the barns and the first fence.");
    }
}




