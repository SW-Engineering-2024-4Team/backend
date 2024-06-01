package test.decorationtest;

import cards.common.ActionRoundCard;
import cards.common.CommonCard;
import cards.factory.imp.round.SheepMarket;
import cards.factory.imp.occupation.LivestockMerchant;
import controllers.GameController;
import controllers.RoomController;
import models.MainBoard;
import models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LivestockMerchantDecorationTest {
    private GameController gameController;
    private Player player1;
    private Player player2;
    private ActionRoundCard sheepMarketCard;
    private LivestockMerchant livestockMerchantCard;

    @BeforeEach
    public void setUp() {
        List<Player> players = createMockPlayers();
        RoomController roomController = new RoomController();
        roomController.handleGameStart("TestRoom123", players);
        gameController = roomController.getGameController();
        player1 = gameController.getPlayers().get(0);
        player2 = gameController.getPlayers().get(1);

        sheepMarketCard = new SheepMarket(1, 1);
        livestockMerchantCard = new LivestockMerchant(2);

        // Initialize MainBoard
        MainBoard mainBoard = gameController.getMainBoard();
//        mainBoard.setActionCards(new ArrayList<>(Collections.singletonList(sheepMarketCard)));
//        mainBoard.setRoundCards(new ArrayList<>(Collections.singletonList(sheepMarketCard)));
        mainBoard.setActionCards(new ArrayList<>());
        mainBoard.setRoundCards(new ArrayList<>());
        mainBoard.getRoundCards().add(sheepMarketCard);

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
    public void testLivestockMerchantDecoration() {
        System.out.println("라운드카드");
        List<ActionRoundCard> actionRoundCards =  gameController.getMainBoard().getRoundCards();
        for (ActionRoundCard actionRoundCard : actionRoundCards) {
            System.out.println("actionRoundCard.getName() = " + actionRoundCard.getName());
        }
        gameController.getMainBoard().revealRoundCard(1);

        // 플레이어 1이 가축 상인 카드 사용
        player1.useUnifiedCard(livestockMerchantCard);
        player1.resetResources();
        player2.resetResources();
        player1.addResource("food", 1);

        // 플레이어 1이 Sheep Market 카드 사용
        player1.printPlayerResources("Sheep Market 이전");
        gameController.getMainBoard().accumulateResources();
        player1.placeFamilyMember(sheepMarketCard);

        // 누적 자원 확인
        gameController.getMainBoard().accumulateResources();

        // 플레이어 1이 Sheep Market 카드 사용
        player1.printPlayerResources("Sheep Market 이후");
        assertEquals(2, player1.getResource("sheep"), "Player 1 should have gained 2 sheep for using Sheep Market card with Livestock Merchant.");

        gameController.getMainBoard().resetFamilyMembersOnCards();
        gameController.getMainBoard().accumulateResources();

        // 플레이어 2가 Sheep Market 카드 사용
        player2.placeFamilyMember(sheepMarketCard);
        player2.printPlayerResources("Sheep Market 이후");
        assertEquals(1, player2.getResource("sheep"), "Player 2 should have gained 1 sheep for using Sheep Market card without Livestock Merchant.");
    }
}
