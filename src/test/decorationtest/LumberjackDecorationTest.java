package test.decorationtest;

import cards.common.ActionRoundCard;
import cards.common.UnifiedCard;
import cards.factory.imp.action.Bush;
import cards.factory.imp.action.Bush2;
import cards.factory.imp.action.Forest;
import cards.factory.imp.occupation.Lumberjack;
import controllers.GameController;
import controllers.RoomController;
import models.MainBoard;
import models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LumberjackDecorationTest {
    private GameController gameController;
    private Player player1;
    private Player player2;
    private ActionRoundCard bushCard;
    private ActionRoundCard bush2Card;
    private ActionRoundCard forestCard;
    private Lumberjack lumberjackCard;

    @BeforeEach
    public void setUp() {
        List<Player> players = createMockPlayers();
        RoomController roomController = new RoomController();
        roomController.handleGameStart("TestRoom123", players);
        gameController = roomController.getGameController();
        player1 = gameController.getPlayers().get(0);
        player2 = gameController.getPlayers().get(1);

        bushCard = new Bush(1);
        bush2Card = new Bush2(2);
        forestCard = new Forest(3);
        lumberjackCard = new Lumberjack(4); // 직업 카드는 따로 인스턴스화하지 않음

        // Initialize MainBoard
        MainBoard mainBoard = gameController.getMainBoard();
        mainBoard.setActionCards(new ArrayList<>(Arrays.asList(bushCard, bush2Card, forestCard)));
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
    public void testLumberjackDecoration() {
        List<Integer> beforeHashes = new ArrayList<>();
        for (ActionRoundCard card : gameController.getMainBoard().getActionCards()) {
            beforeHashes.add(card.hashCode());
        }
        for (ActionRoundCard card : gameController.getMainBoard().getRoundCards()) {
            beforeHashes.add(card.hashCode());
        }

        gameController.getMainBoard().printCardLists();

        player1.resetResources();

        // 나무꾼 직업 카드 사용
        player1.useUnifiedCard(lumberjackCard);

        gameController.getMainBoard().printCardLists();

        List<Integer> afterHashes = new ArrayList<>();
        for (ActionRoundCard card : gameController.getMainBoard().getActionCards()) {
            afterHashes.add(card.hashCode());
        }
        for (ActionRoundCard card : gameController.getMainBoard().getRoundCards()) {
            afterHashes.add(card.hashCode());
        }

        // 해시코드 비교
        boolean hashChanged = false;
        for (int i = 0; i < beforeHashes.size(); i++) {
            if (!beforeHashes.get(i).equals(afterHashes.get(i))) {
                hashChanged = true;
                break;
            }
        }

        assertTrue(hashChanged, "The card lists should be different after using Lumberjack.");

        // 누적 자원 확인
        gameController.getMainBoard().accumulateResources();

        // Bush 카드 사용
        player1.placeFamilyMember(bushCard);
        player1.printPlayerResources("bush1 이후");
        assertEquals(2, player1.getResource("wood"), "Player should have 2 wood after using Bush card.");

        // Bush2 카드 사용
        player1.placeFamilyMember(bush2Card);
        player1.printPlayerResources("bush2 이후");
        assertEquals(5, player1.getResource("wood"), "Player should have 5 wood after using Bush2 card.");

        // Forest 카드 사용
//        player.placeFamilyMember(forestCard);
//        assertEquals(10, player.getResource("wood"), "Player should have 9 wood after using Forest card.");

        assertTrue(bushCard.isOccupied(), "Bush card should be occupied.");
        assertTrue(bush2Card.isOccupied(), "Bush2 card should be occupied.");
//        assertTrue(forestCard.isOccupied(), "Forest card should be occupied.");
    }

    @Test
    public void testLumberjackDecoration2() {
        player1.resetResources();
        player2.resetResources();

        // 플레이어 1이 나무꾼 직업 카드 사용
        player1.useUnifiedCard(lumberjackCard);

        gameController.getMainBoard().printCardLists();

        // 누적 자원 확인
        gameController.getMainBoard().accumulateResources();

        // 플레이어 1이 Bush 카드 사용
        player1.placeFamilyMember(bushCard);
        player1.printPlayerResources("bush1 이후");
        assertEquals(2, player1.getResource("wood"), "Player 1 should have 2 wood after using Bush card.");

        // 플레이어 2가 Bush 카드 사용
        player2.placeFamilyMember(bush2Card);
        player2.printPlayerResources("bush2 이후");
        assertEquals(2, player2.getResource("wood"), "Player 2 should have 2 wood after using Bush2 card.");

    }


}
