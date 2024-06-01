package test.decorationtest;

import cards.common.ActionRoundCard;
import cards.factory.imp.action.Worker;
import cards.factory.imp.minor.ClayPit;
import controllers.GameController;
import controllers.RoomController;
import models.MainBoard;
import models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClayPitDecorationTest {
    private GameController gameController;
    private Player player1;
    private Player player2;
    private ActionRoundCard dayLaborerCard;
    private ClayPit clayPitCard;

    @BeforeEach
    public void setUp() {
        List<Player> players = createMockPlayers();
        RoomController roomController = new RoomController();
        roomController.handleGameStart("TestRoom123", players);
        gameController = roomController.getGameController();
        player1 = gameController.getPlayers().get(0);
        player2 = gameController.getPlayers().get(1);

        dayLaborerCard = new Worker(1);
        clayPitCard = new ClayPit(2);

        // Initialize MainBoard
        MainBoard mainBoard = gameController.getMainBoard();
        mainBoard.setActionCards(new ArrayList<>(Collections.singletonList(dayLaborerCard)));
        mainBoard.setRoundCards(new ArrayList<>());

        // 플레이어 1이 양토 채굴장 카드 사용
        player1.addResource("food", 1); // 비용 지불(음식 1)
        player1.useUnifiedCard(clayPitCard);
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
    public void testClayPitDecoration() {
        player1.resetResources();
        player2.resetResources();

        // 플레이어 1이 Day Laborer 카드 사용
        player1.printPlayerResources("Day Laborer 이전");

        // 플레이어 1이 Day Laborer 카드 사용
        player1.placeFamilyMember(dayLaborerCard);
        player1.printPlayerResources("Day Laborer 이후");
        assertEquals(3, player1.getResource("clay"), "Player 1 should have gained 4 clay for using Day Laborer card with Clay Pit.");

        gameController.getMainBoard().resetFamilyMembersOnCards();

        // 플레이어 2가 Day Laborer 카드 사용
        player2.placeFamilyMember(dayLaborerCard);
        player2.printPlayerResources("Day Laborer 이후");
        assertEquals(0, player2.getResource("clay"), "Player 2 should have gained 1 clay for using Day Laborer card without Clay Pit.");
    }
}
