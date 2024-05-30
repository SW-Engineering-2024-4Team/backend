package test.actionroundcardtest;

import cards.common.ActionRoundCard;
import cards.common.CommonCard;
import cards.factory.imp.round.AddFamilyMember;
import cards.round.NonAccumulativeRoundCard;
import controllers.GameController;
import controllers.RoomController;
import enums.RoomType;
import models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddFamilyMemberTest {
    private GameController gameController;
    private Player player;
    private NonAccumulativeRoundCard addFamilyMemberCard;

    @BeforeEach
    public void setUp() {
        List<Player> players = createMockPlayers();
        RoomController roomController = new RoomController();
        roomController.handleGameStart("TestRoom123", players);
        gameController = roomController.getGameController();
        player = gameController.getPlayers().get(0);

        addFamilyMemberCard = new AddFamilyMember(1, 1);
        gameController.getMainBoard().setActionCards(new ArrayList<>());
        gameController.getMainBoard().setRoundCards(new ArrayList<>());
        gameController.getMainBoard().getRoundCards().add(addFamilyMemberCard);

        // 초기 자원 설정 및 카드 추가
        player.addResource("wood", 10);
        player.addResource("reed", 5);
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
    public void testAddFamilyMember() {

        List<ActionRoundCard> actionRoundCards = gameController.getMainBoard().getRoundCards();
        for (ActionRoundCard actionRoundCard : actionRoundCards) {
            System.out.println("actionRoundCard = " + actionRoundCard.getName());
        }
        player.resetResources();
        player.addResource("wood", 10);  // 자원 추가
        player.printPlayerResources("가족 늘리기 전 자원.");
        

        // 가족 구성원 늘리기
        player.placeFamilyMember(addFamilyMemberCard);
        

        player.printPlayerResources("가족 구성원 늘린 후 자원.");

        // 검증: 가족 구성원이 늘어났는지 확인
//        assertTrue(player.getPlayerBoard().getFamilyMembers().size() > 2, "가족 구성원이 추가되어야 합니다.");
        assertTrue(gameController.getMainBoard().isCardOccupied(addFamilyMemberCard));

        // 보조 설비 카드가 사용되었는지 확인
        assertTrue(!player.getActiveCards().isEmpty(), "보조 설비 카드가 사용되어야 합니다.");

        gameController.resetFamilyMembers();

        player.getPlayerBoard().buildHouse(0,0, RoomType.WOOD);
        player.placeFamilyMember(addFamilyMemberCard);

        List<CommonCard> activeCards = player.getActiveCards();
        for (CommonCard card : activeCards) {
            System.out.println("ActiveCard " + card.getName());
        }
    }
}
