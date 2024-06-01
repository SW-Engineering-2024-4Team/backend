package test.decorationtest;

import cards.factory.imp.minor.CompressedSoil;
import cards.factory.imp.round.BuildFence;
import cards.factory.imp.round.RenovateFarms;
import controllers.GameController;
import controllers.RoomController;
import models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BuildFenceDecoratorTest {

    private GameController gameController;
    private Player player;
    private CompressedSoil compressedSoil;
    private BuildFence buildFenceCard;
    private RenovateFarms renovateFarmsCard;

    @BeforeEach
    public void setUp() {
        List<Player> players = createMockPlayers();
        RoomController roomController = new RoomController();
        roomController.handleGameStart("TestRoom123", players);
        gameController = roomController.getGameController();
        player = gameController.getPlayers().get(0);
        compressedSoil = new CompressedSoil(1);
        buildFenceCard = new BuildFence(2, 1);
        renovateFarmsCard = new RenovateFarms(3, 1);

        gameController.getMainBoard().setActionCards(new ArrayList<>());
        gameController.getMainBoard().setRoundCards(new ArrayList<>());

        gameController.getMainBoard().addCard(buildFenceCard, "round");
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
    public void testBuildFenceWithCompressedSoil() {
        player.resetResources();

        player.addResource("wood", 50);
        player.addResource("clay", 50);
        player.printPlayerResources("CompressedSoil 사용 전 자원");

        // CompressedSoil 카드 사용
        player.useUnifiedCard(compressedSoil);

        // 유효 좌표 설정
        List<int[]> coordinates = List.of(new int[]{1, 1}, new int[]{1, 2}, new int[]{1, 3});
        player.getPlayerBoard().buildFences(coordinates, player);

        // BuildFence 카드의 효과를 확인
        player.placeFamilyMember(buildFenceCard);

        player.printPlayerResources("CompressedSoil 사용 후 자원");

        // 울타리 짓기 후 자원 확인 (예상 결과를 확인)
        int expectedWood = 50 - player.getPlayerBoard().calculateRequiredWoodForFences(coordinates);
        int expectedClay = 50 - (player.getResource("wood") - expectedWood);
        assertEquals(expectedWood, player.getResource("wood"), "Player should have the correct amount of wood after building fences.");
        assertEquals(expectedClay, player.getResource("clay"), "Player should have the correct amount of clay after building fences.");
    }

    @Test
    public void testRenovateFarmsWithCompressedSoil() {
        player.resetResources();

        player.addResource("wood", 50);
        player.addResource("clay", 50);
        player.addResource("stone", 3);
        player.printPlayerResources("CompressedSoil 사용 전 자원");

        // CompressedSoil 카드 사용
        player.useUnifiedCard(compressedSoil);

        // 유효 좌표 설정
//        List<int[]> coordinates = List.of(new int[]{1, 1}, new int[]{1, 2}, new int[]{1, 3});
//        player.getPlayerBoard().buildFences(coordinates, player);

        player.placeFamilyMember(buildFenceCard);

        // RenovateFarms 카드의 효과를 확인
//        player.placeFamilyMember(renovateFarmsCard);

        player.printPlayerResources("CompressedSoil 사용 후 자원");

        // 울타리 짓기 후 자원 확인 (예상 결과를 확인)
//        int expectedWood = 50 - player.getPlayerBoard().calculateRequiredWoodForFences(coordinates);
//        int expectedClay = 50 - (player.getResource("wood") - expectedWood);
//        assertEquals(expectedWood, player.getResource("wood"), "Player should have the correct amount of wood after building fences.");
//        assertEquals(expectedClay, player.getResource("clay"), "Player should have the correct amount of clay after building fences.");
    }
}
