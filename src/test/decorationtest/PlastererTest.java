package test.occupationtest;

import cards.common.ActionRoundCard;
import cards.factory.imp.action.FarmExpansion;
import cards.factory.imp.occupation.Plasterer;
import cards.factory.imp.round.RenovateFarms;
import controllers.GameController;
import controllers.RoomController;
import enums.RoomType;
import models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlastererTest {

    private GameController gameController;
    private Player player;
    private Plasterer plasterer;
    private FarmExpansion farmExpansionCard;
    private RenovateFarms renovateFarmsCard;

    @BeforeEach
    public void setUp() {
        List<Player> players = createMockPlayers();
        RoomController roomController = new RoomController();
        roomController.handleGameStart("TestRoom123", players);
        gameController = roomController.getGameController();
        player = gameController.getPlayers().get(0);
        plasterer = new Plasterer(1);
        farmExpansionCard = new FarmExpansion(2);
        renovateFarmsCard = new RenovateFarms(3, 1);

        gameController.getMainBoard().setActionCards(new ArrayList<>());
        gameController.getMainBoard().setRoundCards(new ArrayList<>());

        gameController.getMainBoard().addCard(farmExpansionCard, "action");
        gameController.getMainBoard().addCard(renovateFarmsCard, "round");
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
    public void testPlastererCard() {
        player.setChooseOption(true);
        player.resetResources();

//        player.getPlayerBoard().buildHouse(1, 1, RoomType.WOOD); // 한 방 추가
        player.addResource("wood", 50);
        player.addResource("clay", 50);
        player.addResource("stone", 3);
        player.printPlayerResources("초벽질공 사용 전 자원");

        player.useUnifiedCard(plasterer);

        // Plasterer 카드의 효과를 확인
        player.placeFamilyMember(farmExpansionCard); // 집 짓기
        player.placeFamilyMember(renovateFarmsCard); // 집 개조

        player.printPlayerResources("초벽질공 사용 후 자원");

        assertEquals(6, player.getResource("food"), "Player should have 6 food after using Plasterer for building and renovating.");
    }
}
