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

public class PlantFieldTest {
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

        actionCard = new NonAccumulativeActionCard(1, "Plant Field Test Card", "This is a plant field test card.");
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
}
