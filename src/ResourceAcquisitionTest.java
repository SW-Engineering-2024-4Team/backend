import cards.common.ActionRoundCard;
import controllers.GameController;
import controllers.RoomController;
import models.Player;

import java.util.ArrayList;
import java.util.List;

public class ResourceAcquisitionTest {

    public static void main(String[] args) {
        // 가상 플레이어 목록 생성
        List<Player> players = createMockPlayers();

        // RoomController를 생성하고 가상 플레이어 목록을 전달하여 게임 시작
        RoomController roomController = new RoomController();
        roomController.handleGameStart("TestRoom123", players);

        // GameController를 가져와서 초기화 상태 확인
        GameController gameController = roomController.getGameController();

        // 테스트용 액션 라운드 카드 생성 및 추가
        TestAccumulativeActionRoundCard testCard = new TestAccumulativeActionRoundCard(1, "Test Card", "Accumulates resources each round");
        gameController.getMainBoard().getActionCards().add(testCard);

        // 라운드 시작 전 자원 상태 출력
        System.out.println("Before round:");
        printPlayerResources(gameController.getPlayers());

        // 라운드 준비 및 진행
        gameController.prepareRound();
        gameController.playRound();

        // 라운드 종료 후 자원 상태 출력
        System.out.println("After round:");
        printPlayerResources(gameController.getPlayers());

        // 자원 누적 기능 테스트
        System.out.println("Testing resource accumulation:");
        testCard.accumulateResources();
        System.out.println("Accumulated resources: " + testCard.getAccumulatedResources());

        // 자원 획득 기능 테스트
        System.out.println("Testing resource gain:");
        testCard.gainResources(players.get(0), testCard.getAccumulatedResources());
        printPlayerResources(players);
    }

    private static List<Player> createMockPlayers() {
        List<Player> players = new ArrayList<>();
        RoomController roomController = new RoomController();
        GameController mockGameController = new GameController("TestRoom123", roomController, players);
        for (int i = 1; i <= 4; i++) {
            Player player = new Player("player" + i, "Player " + i, mockGameController);
            players.add(player);
        }
        return players;
    }

    private static void printPlayerResources(List<Player> players) {
        for (Player player : players) {
            System.out.println("Player ID: " + player.getId() + ", Resources: " + player.getResources());
        }
    }
}
