package test.occupationtest;

import cards.factory.imp.occupation.Roofer;
import controllers.GameController;
import controllers.RoomController;
import enums.RoomType;
import models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RooferTest {
    private GameController gameController;
    private Player player;
    private Roofer rooferCard;

    @BeforeEach
    public void setUp() {
        List<Player> players = createMockPlayers();
        RoomController roomController = new RoomController();
        roomController.handleGameStart("TestRoom123", players);
        gameController = roomController.getGameController();
        player = gameController.getPlayers().get(0);
        rooferCard = new Roofer(1);
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
    public void testRooferCard() {
        player.resetResources();
        player.addResource("food", 1); // 음식 1개 추가
        player.useUnifiedCard(rooferCard);
        assertEquals(2, player.getResource("stone"), "Player should have 1 stone after using Roofer card with 1 room.");

        gameController.getMainBoard().resetFamilyMembersOnCards();

        player.addResource("wood", 5);
        player.getPlayerBoard().buildHouse(1, 1, RoomType.WOOD); // 두 번째 방 추가
        player.resetResources();
        player.addResource("food", 1); // 음식 1개 추가
        player.useUnifiedCard(rooferCard);
        assertEquals(3, player.getResource("stone"), "Player should have 2 stones after using Roofer card with 2 rooms.");
    }
}
