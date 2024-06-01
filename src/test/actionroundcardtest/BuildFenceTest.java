package test.actionroundcardtest;

import cards.common.ActionRoundCard;
import cards.factory.imp.round.BuildFence;
import cards.round.NonAccumulativeRoundCard;
import controllers.CardController;
import controllers.GameController;
import controllers.RoomController;
import models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BuildFenceTest {
    private GameController gameController;
    private Player player;
    private NonAccumulativeRoundCard buildFenceCard;

    @BeforeEach
    public void setUp() {
        List<Player> players = createMockPlayers();
        RoomController roomController = new RoomController();
        roomController.handleGameStart("TestRoom123", players);
        gameController = roomController.getGameController();
        player = gameController.getPlayers().get(0);

        buildFenceCard = new BuildFence(1,  1);
        gameController.getMainBoard().setActionCards(new ArrayList<>());
        gameController.getMainBoard().setRoundCards(new ArrayList<>());
        gameController.getMainBoard().getRoundCards().add(buildFenceCard);

        // 초기 자원 설정
        player.addResource("wood", 10);
    }

    private List<Player> createMockPlayers() {
        List<Player> players = new ArrayList<>();
        GameController mockGameController = new GameController("TestRoom123", new RoomController(), players);
        for (int i = 1; i <= 4; i++) {
            players.add(new Player("player" + i, "Player " + i, mockGameController));
        }
        return players;
    }

    private void printPlayerBoardWithFences(String message) {
        System.out.println(message);
        player.getPlayerBoard().printPlayerBoardWithFences(message, player.getPlayerBoard().getValidFencePositions());
    }

    @Test
    public void testBuildFence() {

        List<ActionRoundCard> actionRoundCards = gameController.getMainBoard().getRoundCards();
        for (ActionRoundCard actionRoundCard : actionRoundCards) {
            System.out.println("actionRoundCard = " + actionRoundCard.getName());
        }
        player.resetResources();
        player.addResource("wood", 50);  // 울타리 짓기 위한 자원 추가
        player.printPlayerResources("울타리 치기 전 자원.");

        // 초기 상태 출력
        printPlayerBoardWithFences("Player board before building fences:");

        // 울타리 짓기
        player.placeFamilyMember(buildFenceCard);
        Set<int[]> validPositions = player.getPlayerBoard().getValidFencePositions();
        printPlayerBoardWithFences("Player board after building fences:");

        player.printPlayerResources("울타리 친 후 자원.");

        // 검증: 울타리가 정상적으로 지어졌는지 확인
        assertTrue(validPositions.size() > 0, "Valid positions should be greater than 0.");
        assertTrue(gameController.getMainBoard().isCardOccupied(buildFenceCard));
        gameController.resetFamilyMembers();

        player.placeFamilyMember(buildFenceCard);
        validPositions = player.getPlayerBoard().getValidFencePositions();
        printPlayerBoardWithFences("Player board after 2nd building fences:");

        player.printPlayerResources("울타리 두 번 친 후 자원.");
    }
}
