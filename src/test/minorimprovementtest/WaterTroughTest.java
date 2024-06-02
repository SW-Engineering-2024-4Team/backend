package test.minorimprovementtest;

import cards.factory.imp.minor.WaterTrough;
import models.FenceArea;
import models.Player;
import controllers.GameController;
import controllers.RoomController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WaterTroughTest {

    private GameController gameController;
    private Player player;
    private WaterTrough waterTrough;

    @BeforeEach
    public void setUp() {
        List<Player> players = createMockPlayers();
        RoomController roomController = new RoomController();
        roomController.handleGameStart("TestRoom123", players);
        gameController = roomController.getGameController();
        player = gameController.getPlayers().get(0);


        // 펜스 영역 설정
        player.addResource("wood", 20);
        List<int[]> selectedPositions = Arrays.asList(
                new int[]{1, 1},
                new int[]{1, 2},
                new int[]{2, 2}
        );
        player.getPlayerBoard().buildFences(selectedPositions, player);
        // WaterTrough 카드 생성
        waterTrough = new WaterTrough(1);

        // 카드를 플레이어에게 추가

        player.addResource("clay", 1);
        player.useUnifiedCard(waterTrough);
        player.resetResources();
    }

    private List<Player> createMockPlayers() {
        List<Player> players = new ArrayList<>();
        GameController mockGameController = new GameController("TestRoom123", new RoomController(), players);
        for (int i = 1; i <= 4; i++) {
            players.add(new Player("player" + i, "Player " + i, mockGameController));
        }
        return players;
    }

    @Test
    public void testWaterTroughEffect() {
        Set<FenceArea> managedFences = player.getPlayerBoard().getManagedFenceAreas();
        int capacity = player.getPlayerBoard().getAnimalCapacity();
        player.getPlayerBoard().printFenceAreas();
    }
}
