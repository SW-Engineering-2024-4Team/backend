package test.decorationtest;

import cards.common.ActionRoundCard;
import cards.common.UnifiedCard;
import cards.factory.imp.action.FarmExpansion;
import cards.factory.imp.round.RenovateFarms;
import cards.factory.imp.occupation.Plasterer;
import controllers.GameController;
import controllers.RoomController;
import models.MainBoard;
import models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlastererDecorationTest {
    private GameController gameController;
    private Player player1;
    private Player player2;
    private ActionRoundCard farmExpansionCard;
    private ActionRoundCard renovateFarmsCard;
    private Plasterer plastererCard;

    @BeforeEach
    public void setUp() {
        List<Player> players = createMockPlayers();
        RoomController roomController = new RoomController();
        roomController.handleGameStart("TestRoom123", players);
        gameController = roomController.getGameController();
        player1 = gameController.getPlayers().get(0);
        player2 = gameController.getPlayers().get(1);

        farmExpansionCard = new FarmExpansion(1);
        renovateFarmsCard = new RenovateFarms(2, 1);
        plastererCard = new Plasterer(3); // 직업 카드는 따로 인스턴스화하지 않음

        // Initialize MainBoard
        MainBoard mainBoard = gameController.getMainBoard();
        mainBoard.setActionCards(new ArrayList<>(Arrays.asList(farmExpansionCard)));
        mainBoard.setRoundCards(new ArrayList<>(Arrays.asList(renovateFarmsCard)));

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
    public void testPlastererDecoration() {
        player1.resetResources();
        player2.resetResources();

        // 플레이어 1이 Plasterer 직업 카드 사용
        player1.useUnifiedCard(plastererCard);

        gameController.getMainBoard().printCardLists();

        // 누적 자원 확인
        gameController.getMainBoard().accumulateResources();

        // 플레이어 1이 Farm Expansion 카드로 집을 짓기
        player1.addResource("wood", 10);
        player1.placeFamilyMember(farmExpansionCard);
        player1.printPlayerResources("Farm Expansion 이후");
        assertEquals(3, player1.getResource("food"), "Player 1 should have gained 3 food for building a house.");

    }
}
