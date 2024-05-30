//package test.decorationtest;
//
//import cards.common.CommonCard;
//import cards.factory.imp.action.WanderingTheaterActionCard;
//import cards.factory.imp.occupation.MagicianOccupationCard;
//import controllers.GameController;
//import controllers.RoomController;
//import models.Player;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class DecoratorTest {
//
//    private GameController gameController;
//    private Player player;
//    private MagicianOccupationCard magicianCard;
//
//    @BeforeEach
//    public void setUp() {
//        // Mock 플레이어 생성
//        List<Player> players = createMockPlayers();
//        player = players.get(0); // 첫 번째 플레이어를 선택
//
//        // RoomController를 통해 게임 시작 처리
//        RoomController roomController = new RoomController();
//        roomController.handleGameStart("TestRoom123", players);
//        gameController = roomController.getGameController();
//
//        // 1. 플레이어와 메인 보드의 카드 리스트를 초기화합니다.
//        player.getOccupationCards().clear(); // 플레이어가 가진 직업 카드 리스트 초기화
//        gameController.getMainBoard().getActionCards().clear(); // 메인 보드의 액션 카드 리스트 초기화
//        gameController.getMainBoard().getRoundCards().clear(); // 메인 보드의 라운드 카드 리스트 초기화
//        gameController.getMainBoard().getMajorImprovementCards().clear(); // 메인 보드의 주요 설비 카드 리스트 초기화
//
//        // 마술사 카드 객체 초기화
//        magicianCard = new MagicianOccupationCard(1, "Magician", "Gains 3 food, +1 wood and grain on Wandering Theater, exchange wood for food",
//                createExchangeRate(), createGainResources(), 1, 4);
//
//        // 유랑극단 행동 카드 객체 초기화 및 메인 보드에 추가
//        actionCard = new WanderingTheaterActionCard(2, "Wandering Theater", "Accumulates 1 food each round.");
//        actionCard.accumulateResources();
//        gameController.getMainBoard().addCard(actionCard, "action");
//
//        // 플레이어의 직업 카드에 마술사 카드 추가
//        player.addCard(magicianCard, "occupation");
//
//        // 테스트 전 플레이어와 메인보드 상태 확인
//        System.out.println("플레이어 보유 직업 카드: " + getCardNames(player.getOccupationCards()));
//        System.out.println("메인 보드의 행동 카드: " + getCardNames(gameController.getMainBoard().getAllCards()));
//    }
//
//    @Test
//    public void testCardInitialization() {
//        player.resetResources();
//        gameController.getMainBoard().accumulateResources();
//
//        // 플레이어의 직업 카드 확인
//        assertEquals(1, player.getOccupationCards().size(), "직업 카드 개수 확인");
//        assertEquals("Magician", player.getOccupationCards().get(0).getName(), "직업 카드 이름 확인");
//
//        // 메인 보드의 행동 카드 확인
//        assertEquals(1, gameController.getMainBoard().getActionCards().size(), "행동 카드 개수 확인");
//        assertEquals("Wandering Theater", gameController.getMainBoard().getActionCards().get(0).getName(), "행동 카드 이름 확인");
//
//        // 유랑극단 행동 카드 실행 및 플레이어 자원 확인
//        actionCard.execute(player);
//        assertEquals(1, player.getResources().get("food"), "플레이어의 음식 자원 확인");
//
//    }
//
//    // Mock 플레이어 생성
//    private List<Player> createMockPlayers() {
//        List<Player> players = new ArrayList<>();
//        GameController mockGameController = new GameController("TestRoom123", new RoomController(), players);
//        for (int i = 1; i <= 4; i++) {
//            players.add(new Player("player" + i, "Player " + i, mockGameController));
//        }
//        return players;
//    }
//
//    // 테스트를 위한 Mock 데이터 생성
//    private Map<String, Integer> createExchangeRate() {
//        Map<String, Integer> exchangeRate = new HashMap<>();
//        exchangeRate.put("wood", 1);
//        exchangeRate.put("food", 2);
//        return exchangeRate;
//    }
//
//    private Map<String, Integer> createGainResources() {
//        Map<String, Integer> gainResources = new HashMap<>();
//        gainResources.put("food", 3);
//        gainResources.put("wood", 1);
//        gainResources.put("grain", 1);
//        return gainResources;
//    }
//
//    // 카드 이름 목록을 반환하는 메서드
//    private List<String> getCardNames(List<CommonCard> cards) {
//        List<String> cardNames = new ArrayList<>();
//        for (CommonCard card : cards) {
//            cardNames.add(card.getName());
//        }
//        return cardNames;
//    }
//}
