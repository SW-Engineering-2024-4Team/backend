package test.actionroundcardtest;

import cards.action.NonAccumulativeActionCard;
import cards.common.ActionRoundCard;
import controllers.GameController;
import controllers.RoomController;
import models.FamilyMember;
import models.Player;
import enums.RoomType;
import models.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NewbornTest {
    private GameController gameController;
    private Player player;
    private ActionRoundCard actionCard;

    @BeforeEach
    public void setUp() {
        List<Player> players = createMockPlayers();
        RoomController roomController = new RoomController();
        roomController.handleGameStart("TestRoom123", players);
        gameController = roomController.getGameController();
        player = gameController.getPlayers().get(0);

        actionCard = new NonAccumulativeActionCard(1, "Add Newborn Test Card", "This is a test card for adding a newborn.");
        gameController.getMainBoard().getActionCards().add(actionCard);

        // 초기 집 배치
        player.getPlayerBoard().getTiles()[1][0] = new Room(RoomType.WOOD, 1, 0);
        player.getPlayerBoard().getTiles()[2][0] = new Room(RoomType.WOOD, 2, 0);
        player.getPlayerBoard().getFamilyMembers()[1][0] = new FamilyMember(1, 0, true);
        ((Room) player.getPlayerBoard().getTiles()[1][0]).setFamilyMember(player.getPlayerBoard().getFamilyMembers()[1][0]);
        player.getPlayerBoard().getFamilyMembers()[2][0] = new FamilyMember(2, 0, true);
        ((Room) player.getPlayerBoard().getTiles()[2][0]).setFamilyMember(player.getPlayerBoard().getFamilyMembers()[2][0]);
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
    public void testAddNewborn() {
        System.out.println("Before adding newborn:");
        printFamilyMembers();
        printEmptyRooms();

        // Add a newborn
        boolean addedNewborn = actionCard.addNewborn(player);

        System.out.println("After attempting to add newborn:");
        printFamilyMembers();
        printEmptyRooms();

        // 검증: 가족 구성원이 추가되지 않아야 함
        int familyMemberCount = 0;
        FamilyMember[][] familyMembers = player.getPlayerBoard().getFamilyMembers();
        for (FamilyMember[] row : familyMembers) {
            for (FamilyMember member : row) {
                if (member != null) {
                    familyMemberCount++;
                }
            }
        }

        assertEquals(2, familyMemberCount, "There should be 2 family members on the board.");
        assertFalse(addedNewborn, "Newborn should not be added when there are no empty rooms.");
    }

    @Test
    public void testAddNewbornWithNewRoom() {
        System.out.println("Before adding a new room:");
        printFamilyMembers();
        printEmptyRooms();

        // 자원 초기화 및 추가
        player.resetResources();
        player.addResource("wood", 10);  // 초기에 나무로 된 집을 짓기 위한 자원 추가

        // 나무 집 짓기
        actionCard.buildHouse(player);
        System.out.println("After building a new room:");
        printFamilyMembers();
        printEmptyRooms();

        // 가족 구성원 추가 시도
        boolean addedNewborn = actionCard.addNewborn(player);

        System.out.println("After attempting to add newborn:");
        printFamilyMembers();
        printEmptyRooms();

        // 검증: 가족 구성원이 추가되어야 함
        int familyMemberCount = 0;
        FamilyMember[][] familyMembers = player.getPlayerBoard().getFamilyMembers();
        for (FamilyMember[] row : familyMembers) {
            for (FamilyMember member : row) {
                if (member != null) {
                    familyMemberCount++;
                }
            }
        }

        assertEquals(3, familyMemberCount, "There should be 3 family members on the board.");
        assertTrue(addedNewborn, "Newborn should be added when there is an empty room.");
    }

    private void printFamilyMembers() {
        FamilyMember[][] familyMembers = player.getPlayerBoard().getFamilyMembers();
        for (int i = 0; i < familyMembers.length; i++) {
            for (int j = 0; j < familyMembers[i].length; j++) {
                if (familyMembers[i][j] != null) {
                    System.out.println("Family member at (" + i + ", " + j + ")");
                }
            }
        }
    }

    private void printEmptyRooms() {
        List<int[]> emptyRooms = player.getPlayerBoard().getEmptyRoomPositions();
        System.out.println("Empty rooms:");
        for (int[] room : emptyRooms) {
            System.out.println("Empty room at (" + room[0] + ", " + room[1] + ")");
        }
    }
}
