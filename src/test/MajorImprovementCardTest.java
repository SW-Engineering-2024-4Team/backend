package test;

import cards.common.CommonCard;
import cards.majorimprovement.TestMajorImprovementCard;
import controllers.GameController;
import controllers.RoomController;
import models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MajorImprovementCardTest {
    private GameController gameController;
    private Player player;
    private TestMajorImprovementCard majorImprovementCard;

    @BeforeEach
    public void setUp() {
        List<Player> players = createMockPlayers();
        RoomController roomController = new RoomController();
        roomController.handleGameStart("TestRoom123", players);
        gameController = roomController.getGameController();
        player = gameController.getPlayers().get(0);

        Map<String, Integer> purchaseCost = Map.of("clay", 3);
        Map<String, Integer> anytimeExchangeRate = Map.of("sheep", 1, "food", 2);
        Map<String, Integer> breadBakingExchangeRate = Map.of("grain", 1, "food", 2);

        majorImprovementCard = new TestMajorImprovementCard(1, "Test Hearth", "Allows baking bread.",
                purchaseCost, anytimeExchangeRate,
                breadBakingExchangeRate, 1, false);

        gameController.getMainBoard().getMajorImprovementCards().add(majorImprovementCard);
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
    public void testPurchaseMajorImprovementCard() {
        player.resetResources();
        player.addResource("clay", 3);

        List<CommonCard> cards = gameController.getMainBoard().getAvailableMajorImprovementCards();
        System.out.println("mainBoard Major Improvement Cards before purchasing: ");
        for (CommonCard card : cards) {
            System.out.println("card = " + card.getName());
        }
        System.out.println();

        System.out.println("플레이어가 다음 주요설비를 구입하려 합니다. " + majorImprovementCard.getName());
        // 구매 시도
        majorImprovementCard.execute(player);

        cards = gameController.getMainBoard().getAvailableMajorImprovementCards();
        System.out.println("mainBoard Major Improvement Cards after purchasing: ");
        for (CommonCard card : cards) {
            System.out.println("card = " + card.getName());
        }

        assertTrue(player.getMajorImprovementCards().contains(majorImprovementCard), "Player should have purchased the major improvement card.");
        assertEquals(0, player.getResource("clay"), "Player should have 0 clay after purchase.");
        assertFalse(gameController.getMainBoard().getAvailableMajorImprovementCards().contains(majorImprovementCard), "MainBoard should not have the major improvement card after purchase.");
    }

    @Test
    // TODO 교환 타이밍 고려
    public void testBreadBaking() {
        player.resetResources();
        player.addResource("grain", 3);
        player.addMajorImprovementCard(majorImprovementCard);

        majorImprovementCard.triggerBreadBaking(player);

        assertEquals(6, player.getResource("food"), "Player should have 6 food after baking bread with 3 grain.");
        assertEquals(0, player.getResource("grain"), "Player should have 0 grain after baking bread.");
    }
}
