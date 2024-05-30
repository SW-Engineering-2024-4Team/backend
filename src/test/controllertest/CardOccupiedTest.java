package test.controllertest;

import cards.action.AccumulativeActionCard;
import cards.common.ActionRoundCard;
import controllers.GameController;
import controllers.RoomController;
import models.FamilyMember;
import models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardOccupiedTest {
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

        Map<String, Integer> resources = new HashMap<>();
        resources.put("wood", 3);
        resources.put("clay", 2);

        actionCard = new AccumulativeActionCard(1, "Test Card", "This is a test card.", resources);
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

    @Test
    public void testResetFamilyMembers() {
        // Family members를 카드에 배치합니다.
        player.placeFamilyMember(actionCard);
        assertTrue(gameController.getMainBoard().isCardOccupied(actionCard), "Card should be occupied after placing a family member.");

        // Family members를 초기화합니다.
        gameController.resetFamilyMembers();

        // 초기화 후 카드가 점유되지 않았는지 확인합니다.
        assertFalse(gameController.getMainBoard().isCardOccupied(actionCard), "Card should not be occupied after resetting family members.");

        // 각 플레이어의 family members가 사용되지 않은 상태로 초기화되었는지 확인합니다.
        for (Player player : gameController.getPlayers()) {
            for (FamilyMember[] row : player.getPlayerBoard().getFamilyMembers()) {
                for (FamilyMember familyMember : row) {
                    if (familyMember != null) {
                        assertFalse(familyMember.isUsed(), "Family member should not be used after reset.");
                    }
                }
            }
        }
    }
}
