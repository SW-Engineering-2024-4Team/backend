package test.minorimprovement;

import cards.factory.imp.major.FurnitureWorkshop;
import cards.factory.imp.minor.KitchenRoom;
import cards.factory.imp.minor.WoodYard;
import controllers.GameController;
import controllers.RoomController;
import enums.RoomType;
import models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WoodYardTest {

    private GameController gameController;
    private Player player;
    private WoodYard woodYard;
    private KitchenRoom kitchenRoom;
    private FurnitureWorkshop furnitureWorkshop;

    @BeforeEach
    public void setUp() {
        List<Player> players = createMockPlayers();
        RoomController roomController = new RoomController();
        roomController.handleGameStart("TestRoom123", players);
        gameController = roomController.getGameController();
        player = gameController.getPlayers().get(0);

        woodYard = new WoodYard(1);
        kitchenRoom = new KitchenRoom(2);
        gameController.getMainBoard().setMajorImprovementCards(new ArrayList<>());
        furnitureWorkshop = new FurnitureWorkshop(3);
        gameController.getMainBoard().addCard(furnitureWorkshop, "majorImprovement");

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
    public void testWoodYardEffectOnKitchenRoom() {
        player.resetResources();
        player.addResource("stone", 2);
        player.addResource("wood", 1);
        player.addResource("clay", 1);

        // WoodYard 카드 사용
        player.useUnifiedCard(woodYard);

        player.printPlayerResources("목재소 사용 후");

        // KitchenRoom 카드 사용
        player.useUnifiedCard(kitchenRoom);

        player.printPlayerResources("부엌방 사용 가능");

        // 부엌방 비용 확인
        Map<String, Integer> kitchenRoomCost = kitchenRoom.getPurchaseCost();
        assertEquals(0, player.getResource("wood"), "Player should have used 1 wood for KitchenRoom.");
        assertEquals(0, player.getResource("clay"), "Player should have used 1 clay for KitchenRoom.");
        assertEquals(0, player.getResource("stone"), "Player should have 1 stone left after paying for WoodYard.");
    }

    @Test
    public void testWoodYardEffectOnFurnitureWorkshop() {
        player.resetResources();
        player.addResource("stone", 4);
        player.addResource("wood", 2);

        // WoodYard 카드 사용
        player.useUnifiedCard(woodYard);

        // FurnitureWorkshop 카드 구매
        player.payResources(furnitureWorkshop.getPurchaseCost());

        // 가구 제작소 비용 확인
        Map<String, Integer> furnitureWorkshopCost = furnitureWorkshop.getPurchaseCost();
        assertEquals(1, player.getResource("wood"), "Player should have used 1 wood for FurnitureWorkshop.");
        assertEquals(0, player.getResource("stone"), "Player should have used 0 stones for FurnitureWorkshop.");
    }
}
