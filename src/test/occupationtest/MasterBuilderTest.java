package test.occupationtest;

import cards.common.CommonCard;
import cards.factory.imp.major.ClayOven;
import cards.factory.imp.major.FurnitureWorkshop;
import cards.factory.imp.occupation.MasterBuilder;
import cards.factory.imp.occupation.Roofer;
import cards.factory.imp.round.PurchaseMajor;
import controllers.GameController;
import controllers.RoomController;
import enums.RoomType;
import models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MasterBuilderTest {
    private GameController gameController;
    private Player player;
    private MasterBuilder masterBuilderCard;
    private ClayOven clayOven;
    private FurnitureWorkshop furnitureWorkshop;
    private PurchaseMajor purchaseMajorCard;

    @BeforeEach
    public void setUp() {
        List<Player> players = createMockPlayers();
        RoomController roomController = new RoomController();
        roomController.handleGameStart("TestRoom123", players);
        gameController = roomController.getGameController();
        player = gameController.getPlayers().get(0);
        masterBuilderCard = new MasterBuilder(1);
        furnitureWorkshop = new FurnitureWorkshop(2);
        purchaseMajorCard = new PurchaseMajor(3, 1);

        gameController.getMainBoard().setActionCards(new ArrayList<>());
        gameController.getMainBoard().setRoundCards(new ArrayList<>());
        gameController.getMainBoard().setMajorImprovementCards(new ArrayList<>());

        gameController.getMainBoard().addCard(furnitureWorkshop, "majorImprovement");
        gameController.getMainBoard().addCard(purchaseMajorCard, "round");

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
    public void testMasterBuilderCard() {
        List<CommonCard> majors = player.getMajorImprovementCards();
        System.out.println("보유 카드");
        for (CommonCard commonCard : majors) {
            System.out.println("commonCard = " + commonCard.getName());
        }
        player.setChooseOption(true);
        player.resetResources();

        player.getPlayerBoard().buildHouse(1, 1, RoomType.WOOD); // 한 방 추가
        player.addResource("wood", 10);
        player.addResource("stone", 3);
        player.printPlayerResources("구입 전 자원");

        player.useUnifiedCard(masterBuilderCard);


        player.placeFamilyMember(purchaseMajorCard); // 나무2, 돌2
        majors = player.getMajorImprovementCards();
        System.out.println("보유 카드");
        for (CommonCard commonCard : majors) {
            System.out.println("commonCard = " + commonCard.getName());
        }

        player.printPlayerResources("구입 후 자원");



        assertEquals(2, player.getResource("stone"), "Player should have 2 stone left after using Master Builder with 1 additional room.");
    }
}
