package test.actionroundcardtest;

import cards.common.ActionRoundCard;
import cards.common.CommonCard;
import cards.factory.imp.round.PurchaseMajor;
import cards.majorimprovement.MajorImprovementCard;
import cards.majorimprovement.TestMajorImprovementCard;
import cards.round.NonAccumulativeRoundCard;
import controllers.GameController;
import controllers.RoomController;
import models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PurchaseMajorTest {
    private GameController gameController;
    private Player player;
    private NonAccumulativeRoundCard purchaseMajorCard;

    @BeforeEach
    public void setUp() {
        List<Player> players = createMockPlayers();
        RoomController roomController = new RoomController();
        roomController.handleGameStart("TestRoom123", players);
        gameController = roomController.getGameController();
        player = gameController.getPlayers().get(0);


        gameController.getMainBoard().setActionCards(new ArrayList<>());
        gameController.getMainBoard().setRoundCards(new ArrayList<>());
        gameController.getMainBoard().setMajorImprovementCards(new ArrayList<>());

        // 테스트용 주요 설비 카드 설정
        Map<String, Integer> purchaseCost = Map.of("clay", 3);
        TestMajorImprovementCard majorImprovementCard = new TestMajorImprovementCard(
                1, "Test Hearth", "Allows baking bread.",
                purchaseCost, null, null, 1, false
        );
        gameController.getMainBoard().addCard(majorImprovementCard, "majorImprovement");
        purchaseMajorCard = new PurchaseMajor(1, 1);
        gameController.getMainBoard().addCard(purchaseMajorCard, "round");

        // 초기 자원 설정
        player.addResource("wood", 5);
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
    public void testPurchaseMajorOrUseMinor() {
        player.addResource("clay", 5);

        gameController.getMainBoard().printCardLists();

        player.setChooseOption(true); // 주요 설비 카드 구매 선택
        player.placeFamilyMember(purchaseMajorCard);
        List<CommonCard> majors = player.getMajorImprovementCards();
        System.out.println("보유 카드");
        for (CommonCard commonCard : majors) {
            System.out.println("commonCard = " + commonCard.getName());
        }

        gameController.resetFamilyMembers();

        player.setChooseOption(false);
        player.placeFamilyMember(purchaseMajorCard);
        List<CommonCard> minors = player.getMinorImprovementCards();
        System.out.println("보유 카드");
        for (CommonCard commonCard : minors) {
            System.out.println("commonCard = " + commonCard.getName());
        }

        majors = player.getMajorImprovementCards();
        System.out.println("보유 카드");
        for (CommonCard commonCard : majors) {
            System.out.println("commonCard = " + commonCard.getName());
        }


    }


}
