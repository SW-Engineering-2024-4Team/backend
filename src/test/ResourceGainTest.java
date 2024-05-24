package test;

import cards.action.AccumulativeActionCard;
import cards.action.NonAccumulativeActionCard;
import cards.common.ActionRoundCard;
import controllers.GameController;
import controllers.RoomController;
import models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResourceGainTest {
    private GameController gameController;
    private Player player;
    private ActionRoundCard actionCard;
    private ActionRoundCard nonAccumulativeCard;

    @BeforeEach
    public void setUp() {
        List<Player> players = createMockPlayers();
        RoomController roomController = new RoomController();
        roomController.handleGameStart("TestRoom123", players);
        gameController = roomController.getGameController();
        player = gameController.getPlayers().get(0);

        actionCard = new AccumulativeActionCard(1, "Accumulative Test Card", "This is an accumulative test card.");
        nonAccumulativeCard = new NonAccumulativeActionCard(2, "Non-Accumulative Test Card", "This is a non-accumulative test card.");
        gameController.getMainBoard().getActionCards().add(actionCard);
        gameController.getMainBoard().getActionCards().add(nonAccumulativeCard);
    }

    private List<Player> createMockPlayers() {
        List<Player> players = new ArrayList<>();
        GameController mockGameController = new GameController("TestRoom123", new RoomController(), players);
        for (int i = 1; i <= 4; i++) {
            players.add(new Player("player" + i, "Player " + i, mockGameController));
        }
        return players;
    }

    private void printPlayerResources(String message) {
        System.out.println(message);
        for (Map.Entry<String, Integer> resource : player.getResources().entrySet()) {
            System.out.println("  " + resource.getKey() + ": " + resource.getValue());
        }
    }

    // TODO 양 자원 추가(가족 구성원 추가와 함께 테스트)
    @Test
    public void testGainResources() {
        printPlayerResources("Resources before testGainResources:");

        Map<String, Integer> resources = new HashMap<>();
        resources.put("wood", 3);
        resources.put("clay", 2);
        actionCard.gainResources(player, resources);

        printPlayerResources("Resources after testGainResources:");

        assertEquals(3, player.getResource("wood"), "Player should have 3 wood.");
        assertEquals(2, player.getResource("clay"), "Player should have 2 clay.");
    }

    @Test
    public void testAccumulateResources() {
        printPlayerResources("Resources before testAccumulateResources:");

        Map<String, Integer> initialResources = new HashMap<>();
        initialResources.put("wood", 1);
        initialResources.put("clay", 2);
        ((AccumulativeActionCard) actionCard).getAccumulatedResources().putAll(initialResources);

        gameController.getMainBoard().accumulateResources();

        Map<String, Integer> accumulatedResources = ((AccumulativeActionCard) actionCard).getAccumulatedResources();
        printPlayerResources("Accumulated resources after first accumulation:");
        assertEquals(2, accumulatedResources.get("wood"), "Accumulated wood should be 2.");
        assertEquals(4, accumulatedResources.get("clay"), "Accumulated clay should be 4.");

        player.placeFamilyMember(actionCard);
        actionCard.gainResources(player, accumulatedResources);
        printPlayerResources("Resources after testAccumulateResources:");
        assertEquals(2, player.getResource("wood"), "Player should have 2 wood.");
        assertEquals(4, player.getResource("clay"), "Player should have 4 clay.");
        assertTrue(actionCard.isOccupied(), "Card should be occupied after placing a family member.");
    }

    @Test
    public void testNonAccumulativeCardGainResources() {
        printPlayerResources("Resources before testNonAccumulativeCardGainResources:");

        Map<String, Integer> resources = new HashMap<>();
        resources.put("wood", 3);
        nonAccumulativeCard.gainResources(player, resources);

        printPlayerResources("Resources after testNonAccumulativeCardGainResources:");

        assertEquals(3, player.getResource("wood"), "Player should have 3 wood from non-accumulative card.");
    }
}
