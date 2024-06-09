package test.decorationtest;

import cards.common.ActionRoundCard;
import cards.factory.imp.action.WanderingTheater;
import cards.factory.imp.occupation.Magician;
import controllers.GameController;
import controllers.RoomController;
import models.MainBoard;
import models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MagicianDecorationTest {
    private GameController gameController;
    private Player player1;
    private Player player2;
    private ActionRoundCard wanderingTheaterCard;
    private Magician magicianCard;

    @BeforeEach
    public void setUp() {
        List<Player> players = createMockPlayers();
        RoomController roomController = new RoomController();
        roomController.handleGameStart("TestRoom123", players);
        gameController = roomController.getGameController();
        player1 = gameController.getPlayers().get(0);
        player2 = gameController.getPlayers().get(1);

        wanderingTheaterCard = new WanderingTheater(1);
        magicianCard = new Magician(4); // 직업 카드는 따로 인스턴스화하지 않음

        // Initialize MainBoard
        MainBoard mainBoard = gameController.getMainBoard();
        mainBoard.setActionCards(new ArrayList<>(Collections.singletonList(wanderingTheaterCard)));
        mainBoard.setRoundCards(new ArrayList<>());
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
    public void testMagicianDecoration() {
        List<Integer> beforeHashes = new ArrayList<>();
        for (ActionRoundCard card : gameController.getMainBoard().getActionCards()) {
            beforeHashes.add(card.hashCode());
        }
        for (ActionRoundCard card : gameController.getMainBoard().getRoundCards()) {
            beforeHashes.add(card.hashCode());
        }

        gameController.getMainBoard().printCardLists();

        player1.resetResources();
        player2.resetResources();

        // 마술사 직업 카드 사용
        player1.useUnifiedCard(magicianCard);

//        assertTrue(hashChanged, "The card lists should be different after using Magician.");

        // 누적 자원 확인
        gameController.getMainBoard().accumulateResources();

        // Wandering Theater 카드 사용
//        player1.placeFamilyMember(wanderingTheaterCard);
//        player1.printPlayerResources("wanderingTheater 이후");
//        assertEquals(1, player1.getResource("food"), "Player 1 should have 1 food after using Wandering Theater card.");
//        assertEquals(1, player1.getResource("wood"), "Player 1 should have 1 wood after using Wandering Theater card.");
//        assertEquals(1, player1.getResource("grain"), "Player 1 should have 1 grain after using Wandering Theater card.");

        // 플레이어 2가 Wandering Theater 카드 사용
        player2.placeFamilyMember(wanderingTheaterCard);
        player2.printPlayerResources("wanderingTheater 이후");
        assertEquals(1, player2.getResource("food"), "Player 2 should have 1 food after using Wandering Theater card.");
        assertEquals(0, player2.getResource("wood"), "Player 2 should have 0 wood after using Wandering Theater card.");
        assertEquals(0, player2.getResource("grain"), "Player 2 should have 0 grain after using Wandering Theater card.");
    }
}
