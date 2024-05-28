package test.actionroundcardtest;

import cards.common.CommonCard;
import cards.common.UnifiedCard;
import cards.minorimprovement.TestMinorImprovementCard;
import cards.occupation.TestOccupationCard;
import controllers.GameController;
import controllers.RoomController;
import enums.ExchangeTiming;
import models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import cards.factory.imp.action.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class UnifiedCardUsageTest {
    private GameController gameController;
    private Player player;
    private TestOccupationCard occupationCard;
    private TestMinorImprovementCard minorImprovementCard;
    private Teaching1 teaching1;

    @BeforeEach
    public void setUp() {
        List<Player> players = createMockPlayers();
        RoomController roomController = new RoomController();
        roomController.handleGameStart("TestRoom123", players);
        gameController = roomController.getGameController();
        player = gameController.getPlayers().get(0);

        Map<String, Integer> occupationExchangeRate = Map.of("grain", 2, "food", 1);
        Map<String, Integer> occupationGainResources = Map.of("food", 2);

        Map<String, Integer> minorImprovementExchangeRate = Map.of("clay", 3, "stone", 1);
        Map<String, Integer> minorImprovementGainResources = Map.of("wood", 2);
        Map<String, Integer> minorImprovementCost = Map.of("food", 1);

        occupationCard = new TestOccupationCard(1, "Occupation Test Card", "This is an occupation test card.", occupationExchangeRate, occupationGainResources, 1, 4);
        teaching1 = new Teaching1(3);
        minorImprovementCard = new TestMinorImprovementCard(2, "Minor Improvement Test Card", "This is a minor improvement test card.", minorImprovementExchangeRate, minorImprovementGainResources, minorImprovementCost);
    }

    private List<Player> createMockPlayers() {
        List<Player> players = new ArrayList<>();
        GameController mockGameController = new GameController("TestRoom123", new RoomController(), players);
        for (int i = 1; i <= 4; i++) {
            players.add(new Player("player" + i, "Player " + i, mockGameController));
        }
        return players;
    }

    private void printPlayerCards(Player player, String type) {
        List<? extends CommonCard> cards = new ArrayList<>();
        if (type.equals("occupation")) {
            cards = player.getOccupationCards();
        }
        if (type.equals("minor")) {
            cards = player.getMinorImprovementCards();
        }
        if (type.equals("major")) {
            cards = player.getMajorImprovementCards();
        }
        if (type.equals("active")) {
            cards = player.getActiveCards();
        }
        System.out.println("type = " + type);
        for (CommonCard card : cards) {
            System.out.println("card = " + card.getName());
        }
    }


    // TODO 데고레이션 테스트
    @Test
    public void testOccupationCardUsage() {
        player.resetResources();
        player.addCard(occupationCard, "occupation");
        printPlayerCards(player,"occupation");
//        occupationCard.execute(player);
        player.useUnifiedCard(occupationCard);
        printPlayerCards(player,"active");

        assertEquals(2, player.getResource("food"), "Player should have 2 food after using occupation card.");
    }

    @Test
    public void testMinorImprovementCardUsage() {
        player.resetResources();
        player.addCard(minorImprovementCard, "minorImprovement");
        player.addResource("food", 1);  // 조건 충족을 위해 음식 자원을 추가
        minorImprovementCard.execute(player);

        assertEquals(2, player.getResource("wood"), "Player should have 2 wood after using minor improvement card.");
        assertEquals(0, player.getResource("food"), "Player should have 0 food after paying the cost.");
    }

    @Test
    public void testOccupationCardExchange() {
        player.resetResources();
        player.addCard(occupationCard, "occupation");
        player.addResource("grain", 4);
        occupationCard.executeExchange(player, "grain", "food", 4);

        assertEquals(2, player.getResource("food"), "Player should have 2 food after exchange.");
        assertEquals(0, player.getResource("grain"), "Player should have 0 grain after exchange.");
    }

    @Test
    public void testMinorImprovementCardExchange() {
        player.resetResources();
        player.addCard(minorImprovementCard, "minorImprovement");
        player.addResource("clay", 3);
        minorImprovementCard.executeExchange(player, "clay", "stone", 3);

        assertEquals(1, player.getResource("stone"), "Player should have 1 stone after exchange.");
        assertEquals(0, player.getResource("clay"), "Player should have 0 clay after exchange.");
    }

    @Test
    public void testCombinedCardUsage() {
        player.resetResources();
        player.addCard(occupationCard, "occupation");
        player.addCard(minorImprovementCard, "minorImprovement");
        player.addResource("food", 1);  // 조건 충족을 위해 음식 자원을 추가

        // 직업 카드 사용
        occupationCard.execute(player);
        assertEquals(3, player.getResource("food"), "Player should have 3 food after using occupation card.");

        // 보조 설비 카드 사용
        minorImprovementCard.execute(player);
        assertEquals(2, player.getResource("wood"), "Player should have 2 wood after using minor improvement card.");
        assertEquals(2, player.getResource("food"), "Player should have 1 food after paying the cost for minor improvement card.");
    }

    @Test
    public void testOccupationCardUsageWithTeaching1() {
        player.resetResources();
        player.addResource("food", 2); // 직업 카드를 사용하기 위해 필요한 자원을 추가합니다.
        printPlayerCards(player, "occupation");
        gameController.getMainBoard().addCard(teaching1, "action");

        // Teaching1 카드를 사용합니다.
        player.placeFamilyMember(teaching1);

        printPlayerCards(player, "active");

        assertEquals(0, player.getResource("food"), "Player should have 0 food after using Teaching1 card.");
        assertTrue(teaching1.isOccupied(), "Teaching1 card should be occupied after executing.");
        gameController.resetFamilyMembers();
        assertFalse(gameController.getMainBoard().isCardOccupied(teaching1));
    }
}
