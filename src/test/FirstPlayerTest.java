package test;

import cards.action.NonAccumulativeActionCard;
import cards.common.ActionRoundCard;
import controllers.GameController;
import controllers.RoomController;
import models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FirstPlayerTest {
    private GameController gameController;

    @BeforeEach
    public void setUp() {
        List<Player> players = createMockPlayers();
        RoomController roomController = new RoomController();
        roomController.handleGameStart("TestRoom123", players);
        gameController = roomController.getGameController();

        // 모든 플레이어에게 액션 카드를 할당
        for (Player player : players) {
            player.setGameController(gameController);
        }

        // 임의의 액션 카드 추가
        ActionRoundCard actionCard = new NonAccumulativeActionCard(1, "Become First Player", "Test card for becoming the first player");
        gameController.getMainBoard().getActionCards().add(actionCard);
    }

    private List<Player> createMockPlayers() {
        List<Player> players = new ArrayList<>();
        RoomController roomController = new RoomController();
        GameController mockGameController = new GameController("TestRoom123", roomController, players);
        for (int i = 1; i <= 4; i++) {
            players.add(new Player("player" + i, "Player " + i, mockGameController));
        }
        return players;
    }

    @Test
    public void testBecomeFirstPlayer() {
        List<Player> turnOrder = gameController.getTurnOrder();
        Player initialFirstPlayer = turnOrder.get(0);

        System.out.println("Initial turn order:");
        for (Player player : turnOrder) {
            System.out.println(player.getName());
        }

        System.out.println("Initial first player: " + initialFirstPlayer.getName());

        // 첫 번째 플레이어가 아닌 플레이어를 선 플레이어로 설정
        Player playerToBecomeFirst = null;
        for (Player player : turnOrder) {
            if (!player.equals(initialFirstPlayer)) {
                playerToBecomeFirst = player;
                break;
            }
        }

        if (playerToBecomeFirst == null) {
            throw new IllegalStateException("No player found to become first player");
        }

        ActionRoundCard actionCard = (ActionRoundCard) gameController.getMainBoard().getActionCards().get(0); // 임의의 ActionRoundCard 사용
        actionCard.becomeFirstPlayer(playerToBecomeFirst);

        // 새로운 턴 오더를 적용
        gameController.prepareRound();

        // 새로운 턴 오더 확인
        List<Player> newTurnOrder = gameController.getTurnOrder();
        Player newFirstPlayer = newTurnOrder.get(0);
        System.out.println("New first player: " + newFirstPlayer.getName());

        // 새로운 턴 오더 출력
        System.out.println("New turn order:");
        for (Player player : newTurnOrder) {
            System.out.println(player.getName());
        }

        // 검증
        assertEquals(playerToBecomeFirst, newFirstPlayer, "The player should become the first player.");
    }
}
