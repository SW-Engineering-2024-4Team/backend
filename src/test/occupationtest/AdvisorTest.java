package test.occupationtest;

import cards.factory.imp.occupation.Advisor;
import controllers.GameController;
import controllers.RoomController;
import models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdvisorTest {
    private GameController gameController;
    private Player player1;
    private Advisor advisorCard;

    @BeforeEach
    public void setUp() {
        List<Player> players = createMockPlayers(2); // Change this value to test different player counts
        RoomController roomController = new RoomController();
        roomController.handleGameStart("TestRoom123", players);
        gameController = roomController.getGameController();
        player1 = gameController.getPlayers().get(0);

        advisorCard = new Advisor(1);
    }

    private List<Player> createMockPlayers(int count) {
        List<Player> players = new ArrayList<>();
        GameController mockGameController = new GameController("TestRoom123", new RoomController(), players);
        for (int i = 1; i <= count; i++) {
            players.add(new Player("player" + i, "Player " + i, mockGameController));
        }
        return players;
    }

    @Test
    public void testAdvisorCardWithOnePlayer() {
        setUp(1);
        player1.resetResources();
        // 플레이어가 Advisor 카드를 사용
        player1.useUnifiedCard(advisorCard);
        // 자원 확인
        assertEquals(2, player1.getResource("grain"), "Player should have 2 grain.");
        assertEquals(0, player1.getResource("clay"), "Player should have 0 clay.");
        assertEquals(0, player1.getResource("stone"), "Player should have 0 stone.");
        assertEquals(0, player1.getResource("sheep"), "Player should have 0 sheep.");
    }

    @Test
    public void testAdvisorCardWithTwoPlayers() {
        setUp(2);
        player1.resetResources();
        // 플레이어가 Advisor 카드를 사용
        player1.useUnifiedCard(advisorCard);
        // 자원 확인
        assertEquals(0, player1.getResource("grain"), "Player should have 0 grain.");
        assertEquals(3, player1.getResource("clay"), "Player should have 3 clay.");
        assertEquals(0, player1.getResource("stone"), "Player should have 0 stone.");
        assertEquals(0, player1.getResource("sheep"), "Player should have 0 sheep.");
    }

    @Test
    public void testAdvisorCardWithThreePlayers() {
        setUp(3);
        player1.resetResources();
        // 플레이어가 Advisor 카드를 사용
        player1.useUnifiedCard(advisorCard);
        // 자원 확인
        assertEquals(0, player1.getResource("grain"), "Player should have 0 grain.");
        assertEquals(0, player1.getResource("clay"), "Player should have 0 clay.");
        assertEquals(2, player1.getResource("stone"), "Player should have 2 stone.");
        assertEquals(0, player1.getResource("sheep"), "Player should have 0 sheep.");
    }

    @Test
    public void testAdvisorCardWithFourPlayers() {
        setUp(4);
        player1.resetResources();
        // 플레이어가 Advisor 카드를 사용
        player1.useUnifiedCard(advisorCard);
        // 자원 확인
        assertEquals(0, player1.getResource("grain"), "Player should have 0 grain.");
        assertEquals(0, player1.getResource("clay"), "Player should have 0 clay.");
        assertEquals(0, player1.getResource("stone"), "Player should have 0 stone.");
        assertEquals(1, player1.getResource("sheep"), "Player should have 2 sheep.");
    }

    private void setUp(int playerCount) {
        List<Player> players = createMockPlayers(playerCount);
        RoomController roomController = new RoomController();
        roomController.handleGameStart("TestRoom123", players);
        gameController = roomController.getGameController();
        player1 = gameController.getPlayers().get(0);
        advisorCard = new Advisor(1);
    }
}
