package test.minorimprovementtest;

import cards.factory.imp.minor.KitchenRoom;
import controllers.GameController;
import controllers.RoomController;
import enums.RoomType;
import models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KitchenRoomTest {

    private GameController gameController;
    private Player player;
    private KitchenRoom kitchenRoom;

    @BeforeEach
    public void setUp() {
        List<Player> players = createMockPlayers();
        RoomController roomController = new RoomController();
        roomController.handleGameStart("TestRoom123", players);
        gameController = roomController.getGameController();
        player = gameController.getPlayers().get(0);
        kitchenRoom = new KitchenRoom(1);

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
    public void testKitchenRoomEffect() {
        player.resetResources();
        player.addResource("wood", 10);
        player.addResource("clay", 10);

        player.printPlayerResources("부엌방 사용 전 자원");
        player.useUnifiedCard(kitchenRoom);
        player.printPlayerResources("부엌방 사용 후 자원");

        // 부엌방 효과로 인해 음식 3개가 추가되었는지 확인
        int expectedFood = 3;
        assertEquals(expectedFood, player.getResource("food"), "Player should have 3 food after the KitchenRoom effect.");
    }

    @Test
    public void testKitchenRoomEffectWithoutWoodHouse() {
        player.resetResources();
        player.addResource("wood", 10);
        player.addResource("clay", 10);
        player.getPlayerBoard().renovateRooms(RoomType.CLAY, player);

        player.printPlayerResources("부엌방 사용 전 자원");
        player.useUnifiedCard(kitchenRoom);
        player.printPlayerResources("부엌방 사용 후 자원");

        // 부엌방 효과가 적용되지 않았는지 확인
        int expectedFood = 0;
        assertEquals(expectedFood, player.getResource("food"), "Player should have 0 food because there is no wooden house.");
    }
}
