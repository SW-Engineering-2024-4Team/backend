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


        // 1라운드 진행
        gameController.testGame();

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
        player.getPlayerBoard().getFamilyMembers()[0][1] = new FamilyMember(0, 1, true);
        player.getPlayerBoard().getFamilyMembers()[0][2] = new FamilyMember(0, 2, true);
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
}