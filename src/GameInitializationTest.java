import cards.common.ActionRoundCard;
import cards.common.CommonCard;
import controllers.GameController;
import controllers.RoomController;
import models.Player;

import java.util.ArrayList;
import java.util.List;

public class GameInitializationTest {
    public static void main(String[] args) {
        // 가상 플레이어 목록 생성
        List<Player> players = createMockPlayers();

        // RoomController를 생성하고 가상 플레이어 목록을 전달하여 게임 시작
        RoomController roomController = new RoomController();
        roomController.handleGameStart("TestRoom123", players);

        // GameController를 가져와서 초기화 상태 확인
        GameController gameController = roomController.getGameController();

        // 모든 플레이어의 초기 덱, 자원, 보드 상태를 확인
        for (Player player : gameController.getPlayers()) {
            System.out.println("Player ID: " + player.getId());
            System.out.println("Occupation Cards: " + getCardNames(player.getOccupationCards()));
            System.out.println("Minor Improvement Cards: " + getCardNames(player.getMinorImprovementCards()));
            System.out.println("Initial Resources: " + player.getResources());
            System.out.println("Player Board Initialized: " + player.getPlayerBoard().getTiles().length + "x" + player.getPlayerBoard().getTiles()[0].length);
            System.out.println("----------");
        }

        // 메인 보드의 카드 배치 상태 확인
        System.out.println("Main Board Action Cards: " + getActionRoundCardNames(gameController.getMainBoard().getActionCards()));
        System.out.println("Main Board Round Cards: " + getActionRoundCardNames(gameController.getMainBoard().getRoundCards()));
        System.out.println("Main Board Major Improvement Cards: " + getCardNames(gameController.getMainBoard().getMajorImprovementCards()));
    }

    private static List<Player> createMockPlayers() {
        List<Player> players = new ArrayList<>();
        GameController mockGameController = new GameController("TestRoom123", new RoomController(), players);
        for (int i = 1; i <= 4; i++) {
            players.add(new Player("player" + i, "Player " + i, mockGameController));
        }
        return players;
    }

    private static List<String> getCardNames(List<? extends CommonCard> cards) {
        List<String> cardNames = new ArrayList<>();
        for (CommonCard card : cards) {
            cardNames.add(card.getName());
        }
        return cardNames;
    }

    private static List<String> getActionRoundCardNames(List<? extends ActionRoundCard> cards) {
        List<String> cardNames = new ArrayList<>();
        for (ActionRoundCard card : cards) {
            cardNames.add(card.getName());
        }
        return cardNames;
    }
}
