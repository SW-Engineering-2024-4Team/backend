package test;

import cards.common.ActionRoundCard;
import cards.common.CommonCard;
import controllers.GameController;
import controllers.RoomController;
import models.FamilyMember;
import models.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameRoundTest {
    public static void main(String[] args) {
        // 가상 플레이어 목록 생성
        List<Player> players = createMockPlayers();

        // RoomController를 생성하고 가상 플레이어 목록을 전달하여 게임 시작
        RoomController roomController = new RoomController();
        roomController.handleGameStart("TestRoom123", players);

        // GameController를 가져와서 초기화 상태 확인
        GameController gameController = roomController.getGameController();

        // 모든 플레이어의 상태를 확인
        for (Player player : gameController.getPlayers()) {
            System.out.println("Player ID: " + player.getId());
            System.out.println("Occupation Cards: " + getCardNames(player.getOccupationCards()));
            System.out.println("Minor Improvement Cards: " + getCardNames(player.getMinorImprovementCards()));
            System.out.println("Resources: " + player.getResources());
            System.out.println("Player Board Initialized: " + player.getPlayerBoard().getTiles().length + "x" + player.getPlayerBoard().getTiles()[0].length);
            System.out.println("Family Members on Board: ");
            printFamilyMembers(player);
            System.out.println("----------");
        }

        // 메인 보드의 카드 배치 상태 확인
        System.out.println("Main Board Action Cards: " + getCardNames(gameController.getMainBoard().getActionCards()));
        System.out.println("Main Board Round Cards: " + getCardNames(gameController.getMainBoard().getRoundCards()));
        System.out.println("Main Board Major Improvement Cards: " + getCardNames(gameController.getMainBoard().getMajorImprovementCards()));

        gameController.testGame();

//        testBecomeFirstPlayer(gameController);

        // 4라운드 진행



        // 모든 플레이어의 상태를 확인
        for (Player player : gameController.getPlayers()) {
            System.out.println("Player ID: " + player.getId());
            System.out.println("Occupation Cards: " + getCardNames(player.getOccupationCards()));
            System.out.println("Minor Improvement Cards: " + getCardNames(player.getMinorImprovementCards()));
            System.out.println("Resources: " + player.getResources());
            System.out.println("Player Board Initialized: " + player.getPlayerBoard().getTiles().length + "x" + player.getPlayerBoard().getTiles()[0].length);
            System.out.println("Family Members on Board: ");
            printFamilyMembers(player);
            System.out.println("----------");
        }

        // 메인 보드의 카드 배치 상태 확인
        System.out.println("Main Board Action Cards: " + getCardNames(gameController.getMainBoard().getActionCards()));
        System.out.println("Main Board Round Cards: " + getCardNames(gameController.getMainBoard().getRoundCards()));
        System.out.println("Main Board Major Improvement Cards: " + getCardNames(gameController.getMainBoard().getMajorImprovementCards()));
    }

    // 선 플레이어 테스트


    private static List<Player> createMockPlayers() {
        List<Player> players = new ArrayList<>();
        RoomController roomController = new RoomController();
        GameController mockGameController = new GameController("TestRoom123", roomController, players);
        for (int i = 1; i <= 4; i++) {
            Player player = new Player("player" + i, "Player " + i, mockGameController);
            initializeFamilyMembers(player);
            players.add(player);
        }
        return players;
    }

    private static void initializeFamilyMembers(Player player) {
        // 기본 가족 구성원 설정
        player.getPlayerBoard().getFamilyMembers()[1][0] = new FamilyMember(1, 0, true);
        player.getPlayerBoard().getFamilyMembers()[2][0] = new FamilyMember(2, 0, true);
    }

    private static void printFamilyMembers(Player player) {
        FamilyMember[][] familyMembers = player.getPlayerBoard().getFamilyMembers();
        for (int i = 0; i < familyMembers.length; i++) {
            for (int j = 0; j < familyMembers[i].length; j++) {
                if (familyMembers[i][j] != null) {
                    FamilyMember member = familyMembers[i][j];
                    System.out.println("  Family Member at (" + i + ", " + j + ") - Adult: " + member.isAdult());
                }
            }
        }
    }

    private static List<String> getCardNames(List<? extends CommonCard> cards) {
        List<String> cardNames = new ArrayList<>();
        for (CommonCard card : cards) {
            cardNames.add(card.getName());
        }
        return cardNames;
    }

    private static void testBecomeFirstPlayer(GameController gameController) {
        List<Player> turnOrder = gameController.getTurnOrder();
        Player initialFirstPlayer = turnOrder.get(0);

        List<Player> TurnOrder = gameController.getTurnOrder();
        // 새로운 턴 오더 출력
        System.out.println("turn order:");
        for (Player player : TurnOrder) {
            System.out.println(player.getName());
        }

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

        System.out.println("Initial first player: " + initialFirstPlayer.getName());

        // 선 플레이어 변경
        ActionRoundCard actionCard = (ActionRoundCard) gameController.getMainBoard().getActionCards().get(0); // 임의의 ActionRoundCard 사용
        actionCard.becomeFirstPlayer(playerToBecomeFirst);

        // prepareRound 메서드 호출하여 새로운 턴 순서 적용
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
        assert playerToBecomeFirst.equals(newFirstPlayer) : "The player should become the first player.";
    }

}