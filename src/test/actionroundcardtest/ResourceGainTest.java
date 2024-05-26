package test.actionroundcardtest;

import cards.action.AccumulativeActionCard;
import cards.action.NonAccumulativeActionCard;
import cards.common.ActionRoundCard;
import controllers.GameController;
import controllers.RoomController;
import enums.RoomType;
import models.Animal;
import models.FamilyMember;
import models.Player;
import models.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResourceGainTest {
    private GameController gameController;
    private Player player;
    private ActionRoundCard actionCard;
    private ActionRoundCard nonAccumulativeCard;

    @BeforeEach
    public void setUp() {
        List<Player> players = createMockPlayers();
        RoomController roomController = new RoomController();
        roomController.handleGameStart("TestRoom123", players);
        gameController = roomController.getGameController();
        player = gameController.getPlayers().get(0);

        actionCard = new AccumulativeActionCard(1, "Accumulative Test Card", "This is an accumulative test card.");
        nonAccumulativeCard = new NonAccumulativeActionCard(2, "Non-Accumulative Test Card", "This is a non-accumulative test card.");
        gameController.getMainBoard().getActionCards().add(actionCard);
        gameController.getMainBoard().getActionCards().add(nonAccumulativeCard);
    }

    private List<Player> createMockPlayers() {
        List<Player> players = new ArrayList<>();
        GameController mockGameController = new GameController("TestRoom123", new RoomController(), players);
        for (int i = 1; i <= 4; i++) {
            players.add(new Player("player" + i, "Player " + i, mockGameController));
        }
        return players;
    }

    private void printPlayerResources(String message) {
        System.out.println(message);
        for (Map.Entry<String, Integer> resource : player.getResources().entrySet()) {
            System.out.println("  " + resource.getKey() + ": " + resource.getValue());
        }
    }

    @Test
    public void testGainResources() {
        printPlayerResources("Resources before testGainResources:");

        Map<String, Integer> resources = new HashMap<>();
        resources.put("wood", 3);
        resources.put("clay", 2);
        actionCard.gainResources(player, resources);

        printPlayerResources("Resources after testGainResources:");

        assertEquals(3, player.getResource("wood"), "Player should have 3 wood.");
        assertEquals(2, player.getResource("clay"), "Player should have 2 clay.");
    }

    @Test
    public void testAccumulateResources() {
        printPlayerResources("Resources before testAccumulateResources:");

        Map<String, Integer> initialResources = new HashMap<>();
        initialResources.put("wood", 1);
        initialResources.put("clay", 2);
        ((AccumulativeActionCard) actionCard).getAccumulatedResources().putAll(initialResources);

        gameController.getMainBoard().accumulateResources();

        Map<String, Integer> accumulatedResources = ((AccumulativeActionCard) actionCard).getAccumulatedResources();
        printPlayerResources("Accumulated resources after first accumulation:");
        assertEquals(2, accumulatedResources.get("wood"), "Accumulated wood should be 2.");
        assertEquals(4, accumulatedResources.get("clay"), "Accumulated clay should be 4.");

        player.placeFamilyMember(actionCard);
        actionCard.gainResources(player, accumulatedResources);
        printPlayerResources("Resources after testAccumulateResources:");
        assertEquals(2, player.getResource("wood"), "Player should have 2 wood.");
        assertEquals(4, player.getResource("clay"), "Player should have 4 clay.");
        assertTrue(actionCard.isOccupied(), "Card should be occupied after placing a family member.");
    }

    @Test
    public void testNonAccumulativeCardGainResources() {
        printPlayerResources("Resources before testNonAccumulativeCardGainResources:");

        Map<String, Integer> resources = new HashMap<>();
        resources.put("wood", 3);
        nonAccumulativeCard.gainResources(player, resources);

        printPlayerResources("Resources after testNonAccumulativeCardGainResources:");

        assertEquals(3, player.getResource("wood"), "Player should have 3 wood from non-accumulative card.");
    }

    @Test
    public void testGainResourcesWithAnimals() {
        printPlayerResources("Resources before testGainResourcesWithAnimals:");

        // 울타리 및 외양간 설정
        player.addResource("wood", 20);
        player.addResource("clay", 10);

        List<int[]> fencePositions = new ArrayList<>();
        fencePositions.add(new int[]{1, 1});
        fencePositions.add(new int[]{1, 2});
        fencePositions.add(new int[]{1, 3});

        player.getPlayerBoard().buildFences(fencePositions, player);

        // 외양간을 단독 타일에 배치
        player.getPlayerBoard().buildBarn(0, 2);

        // 초기 집 배치
        player.getPlayerBoard().getTiles()[1][0] = new Room(RoomType.WOOD, 1, 0);
        player.getPlayerBoard().getTiles()[2][0] = new Room(RoomType.WOOD, 2, 0);
        player.getPlayerBoard().getFamilyMembers()[1][0] = new FamilyMember(1, 0, true);
        player.getPlayerBoard().getFamilyMembers()[2][0] = new FamilyMember(2, 0, true);

        // 양 자원 추가
        Map<String, Integer> resources = new HashMap<>();
        resources.put("sheep", 4);

//        actionCard.gainResources(player, resources);

        // 양을 집, 울타리, 외양간에 각각 배치
        System.out.println("집에 배치");
        Animal sheep1 = new Animal(1, 0, "sheep");
        player.getPlayerBoard().addAnimalToBoard(sheep1, 1, 0);

        System.out.println("울타리에 배치");
        Animal sheep2 = new Animal(1, 1, "sheep");
        player.getPlayerBoard().addAnimalToBoard(sheep2, 1, 1);

        System.out.println("외양간에 배치");
        Animal sheep3 = new Animal(0, 2, "sheep");
        player.getPlayerBoard().addAnimalToBoard(sheep3, 0, 2);

        System.out.println("집에 배치");
        Animal sheep4 = new Animal(2, 0, "sheep");
        player.getPlayerBoard().addAnimalToBoard(sheep4, 2, 0);

        player.addResource("sheep", 4);
        // 수용 능력 확인
        int capacity = player.getPlayerBoard().getAnimalCapacity();
//        System.out.println("Animal capacity after gaining resources: " + capacity);

        // 검증
        assertEquals(8, capacity, "The capacity should be 8 after gaining resources and adding animals to the board.");
//        assertEquals(4, player.getResource("sheep"), "There should be 4 sheep resources.");
        printPlayerResources("Resources after testGainResourcesWithAnimals:");
    }


}
