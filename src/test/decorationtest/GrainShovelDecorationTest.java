package test.decorationtest;

import cards.common.ActionRoundCard;
import cards.factory.imp.action.Seed;
import cards.factory.imp.minor.GrainShovel;
import controllers.GameController;
import controllers.RoomController;
import models.MainBoard;
import models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GrainShovelDecorationTest {
    private GameController gameController;
    private Player player1;
    private Player player2;
    private ActionRoundCard seedCard;
    private GrainShovel grainShovelCard;

    @BeforeEach
    public void setUp() {
        List<Player> players = createMockPlayers();
        RoomController roomController = new RoomController();
        roomController.handleGameStart("TestRoom123", players);
        gameController = roomController.getGameController();
        player1 = gameController.getPlayers().get(0);
        player2 = gameController.getPlayers().get(1);

        seedCard = new Seed(1);
        grainShovelCard = new GrainShovel(2);

        // Initialize MainBoard
        MainBoard mainBoard = gameController.getMainBoard();
        mainBoard.setActionCards(new ArrayList<>(Collections.singletonList(seedCard)));
        mainBoard.setRoundCards(new ArrayList<>());

        // 플레이어 1이 곡식용 삽 카드 사용
        player1.addResource("wood", 1); // 비용 지불(나무 1)
        player1.useUnifiedCard(grainShovelCard);
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
    public void testGrainShovelDecoration() {
        player1.resetResources();
        player2.resetResources();

        // 플레이어 1이 Seed 카드 사용
        player1.printPlayerResources("Seed 이전");
        player1.placeFamilyMember(seedCard);

//        // 누적 자원 확인
//        gameController.getMainBoard().accumulateResources();

        // 플레이어 1이 Seed 카드 사용
        player1.placeFamilyMember(seedCard);
        player1.printPlayerResources("Seed 이후");
        assertEquals(2, player1.getResource("grain"), "Player 1 should have gained 2 grain for using Seed card with Grain Shovel.");

        gameController.getMainBoard().resetFamilyMembersOnCards();
        // 플레이어 2가 Seed 카드 사용
        player2.placeFamilyMember(seedCard);
        player2.printPlayerResources("Seed 이후");
        assertEquals(1, player2.getResource("grain"), "Player 2 should have gained 1 grain for using Seed card without Grain Shovel.");
    }
}
