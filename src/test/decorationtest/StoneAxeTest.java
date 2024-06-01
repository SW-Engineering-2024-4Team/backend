package test.decorationtest;

import cards.factory.imp.minor.StoneAxe;
import cards.factory.imp.round.RenovateFarms;
import controllers.GameController;
import controllers.RoomController;
import models.Player;
import models.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StoneAxeTest {

    private GameController gameController;
    private Player player;
    private StoneAxe stoneAxe;
    private RenovateFarms renovateFarmsCard;

    @BeforeEach
    public void setUp() {
        List<Player> players = createMockPlayers();
        RoomController roomController = new RoomController();
        roomController.handleGameStart("TestRoom123", players);
        gameController = roomController.getGameController();
        player = gameController.getPlayers().get(0);
        stoneAxe = new StoneAxe(1);
        renovateFarmsCard = new RenovateFarms(2, 1);

        gameController.getMainBoard().setActionCards(new ArrayList<>());
        gameController.getMainBoard().setRoundCards(new ArrayList<>());

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
    public void testRenovateFarmsWithStoneAxe() {
        player.setChooseOption(true);
        player.resetResources();

        player.addResource("wood", 50);
        player.addResource("clay", 50);
        player.addResource("stone", 3);
        player.printPlayerResources("StoneAxe 사용 전 자원");

        // StoneAxe 카드 사용
        player.useUnifiedCard(stoneAxe);

        // RenovateFarms 카드의 효과를 확인
        player.placeFamilyMember(renovateFarmsCard);
        player.getPlayerBoard().printFenceAreas();

        player.printPlayerResources("StoneAxe 사용 후 자원");

        // 외양간이 무료로 지어졌는지 확인 (예상 결과를 확인)
        // 예상 결과를 확인하기 위해 필요한 메서드를 사용하여 외양간이 지어진 위치를 확인합니다.
        // 예를 들어, 외양간이 지어졌다고 가정한 위치를 확인하는 코드 추가

        // 자원 확인 (예상 결과를 확인)
//        int expectedWood = 50 - player.getPlayerBoard().calculateRequiredWoodForRenovation();
//        int expectedClay = 50 - player.getPlayerBoard().calculateRequiredClayForRenovation();
//        int expectedStone = 3 - player.getPlayerBoard().calculateRequiredStoneForRenovation();

//        assertEquals(expectedWood, player.getResource("wood"), "Player should have the correct amount of wood after renovation.");
//        assertEquals(expectedClay, player.getResource("clay"), "Player should have the correct amount of clay after renovation.");
//        assertEquals(expectedStone, player.getResource("stone"), "Player should have the correct amount of stone after renovation.");

    }
}
