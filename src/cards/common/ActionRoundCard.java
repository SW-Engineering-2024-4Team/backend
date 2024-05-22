package cards.common;

import cards.majorimprovement.MajorImprovementCard;
import enums.RoomType;
import models.Animal;
import models.MainBoard;
import models.Player;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public interface ActionRoundCard extends CommonCard {
    boolean isRevealed(); // RoundCard와 ActionCard 구분을 위해 추가
    void reveal(); // 라운드 카드를 공개하는 메서드
    boolean isAccumulative(); // 누적 가능한지 여부 확인 메서드

    // 자원 획득
    default void gainResources(Player player, Map<String, Integer> resources) {
        for (Map.Entry<String, Integer> entry : resources.entrySet()) {
            String resource = entry.getKey();
            int amount = entry.getValue();

            if (resource.equals("sheep")) {
                // 양 자원일 경우
                player.addResource(resource, amount);
                for (int i = 0; i < amount; i++) {
                    // 양 객체를 추가하여 플레이어 보드에 배치
                    player.getPlayerBoard().addAnimal(new Animal(-1, -1, resource));
                }
            } else {
                // 일반 자원일 경우
                player.addResource(resource, amount);
            }
        }
    }


    // 직업 카드 사용
    default void useOccupationCard(Player player) {
        // 직업 카드 사용 로직
        List<CommonCard> occupationCards = player.getOccupationCards();
        if (!occupationCards.isEmpty()) {
            // 플레이어가 카드를 선택하는 로직 (예시로 첫 번째 카드를 선택했다고 가정)
            UnifiedCard selectedCard = (UnifiedCard) occupationCards.get(0);
            player.useUnifiedCard(selectedCard);
        }
    }

    // 보조 설비 카드 사용
    default void useMinorImprovementCard(Player player) {
        // 보조 설비 카드 사용 로직
        List<CommonCard> minorImprovementCards = player.getMinorImprovementCards();
        if (!minorImprovementCards.isEmpty()) {
            // 플레이어가 카드를 선택하는 로직 (예시로 첫 번째 카드를 선택했다고 가정)
            UnifiedCard selectedCard = (UnifiedCard) minorImprovementCards.get(0);
            player.useUnifiedCard(selectedCard);
        }
    }

    // 주요 설비 카드 구매
    default void purchaseMajorImprovementCard(Player player, MainBoard mainBoard, int cardId) {
        // 메인 보드에서 구매 가능한 주요 설비 카드를 확인합니다.
        List<CommonCard> majorImprovementCards = mainBoard.getMajorImprovementCards();
        MajorImprovementCard cardToPurchase = null;

        for (CommonCard card : majorImprovementCards) {
            if (card.getId() == cardId && card instanceof MajorImprovementCard) {
                cardToPurchase = (MajorImprovementCard) card;
                break;
            }
        }

        if (cardToPurchase != null && player.checkResources(cardToPurchase.getPurchaseCost())) {
            player.payResources(cardToPurchase.getPurchaseCost());
            player.addMajorImprovementCard(cardToPurchase);
            mainBoard.removeMajorImprovementCard(cardToPurchase);
        } else {
            // 플레이어에게 구매가 불가능하다는 메시지를 표시합니다.
//            notifyPlayer(player, "자원이 부족하여 주요 설비 카드를 구매할 수 없습니다.");
        }
    }

    // 빵굽기 행동(주요 설비 카드 사용)
    default void triggerBreadBaking(Player player) {
        List<CommonCard> majorImprovementCards = player.getMajorImprovementCards();
        List<BakingCard> bakingCards = majorImprovementCards.stream()
                .filter(card -> card instanceof BakingCard)
                .map(card -> (BakingCard) card)
                .collect(Collectors.toList());

        if (!bakingCards.isEmpty()) {
            BakingCard selectedCard = player.selectBakingCard(bakingCards);
            selectedCard.triggerBreadBaking(player);
        } else {
            // 빵굽기 가능한 설비가 없다는 메시지 표시
        }
    }

    // 기물 짓기

    // 집 짓기
    default void buildHouse(Player player, int x, int y, RoomType type) {
        Map<String, Integer> playerResources = player.getResources(); // 플레이어의 자원을 가져옴
        if (player.getPlayerBoard().canBuildHouse(x, y, type, playerResources)) {
            Map<String, Integer> cost = player.getHouseResourceCost(type);
            if (player.checkResources(cost)) {
                player.payResources(cost);
                player.getPlayerBoard().buildHouse(x, y, type);
            } else {
                // 자원이 부족하다는 메시지 표시
                System.out.println("자원이 부족합니다.");
            }
        } else {
            // 집을 지을 수 없는 조건이라는 메시지 표시
            System.out.println("집을 지을 수 없습니다.");
        }
    }

    // 밭 일구기
    default void plowField(Player player, int x, int y) {
        player.getPlayerBoard().plowField(x, y);
    }

    // TODO 외양간 짓기
    default void buildBarn(Player player, int x, int y) {
        player.getPlayerBoard().buildBarn(x, y);
    }

    // TODO 울타리 치기
    default void buildFence(Player player, int x, int y) {
        if (player.selectFenceTile(x, y)) {
            Set<int[]> validPositions = player.getPlayerBoard().getValidFencePositions();
            // validPositions를 플레이어에게 제공하여 다음 위치를 선택하게 함
        } else {
            System.out.println("울타리를 선택하지 못했습니다.");
        }
    }

    default boolean finalizeFenceBuilding(Player player) {
        return player.finalizeFenceBuilding();
    }

    // 곡식 심기
    default void plantField(Player player, int x, int y, int initialCrops) {
        player.getPlayerBoard().plantField(x, y, initialCrops);
    }

    // 선 플레이어 되기
    default void becomeFirstPlayer(Player player) {
        player.setFirstPlayer(true);
    }

    // 객체 추가

    // 1. 가족 구성원 추가
    default void addNewborn(Player player) {
        player.addFamilyMember();
    }

    // 기물 변경
    default void renovateRooms(Player player) {
        /**
         * 집 고치기(방 업그레이드)
         * 설명: 자원을 지불하고 나무 집을 흙 집으로, 흙 집을 돌 집으로 업그레이드합니다.
         * 집 타입 자체가 발생하는 효과는 없지만, 추후 점수 계산시에 높은 등급의 집일수록 높은 점수를 받음.
         * 흐름:
         * 1. 플레이어의 방 타입을 확인(나무, 흙) // 돌 집은 더 이상 업그레이드를 할 수 없음.
         * getTiles? or 적절한 메서드 사용
         * 2. 플레이어의 자원이 충분한지 확인(집 개수 * 업그레이드 할 타입의 자원) 예: 나무->흙일시 흙 * 플레이어의 방 수
         * getResource->payResource
         * 집을 고칠 땐, 모든 집의 타입을 한번에 바꿔야 한다.
         * 3. 이전 집의 정보는 그대로 받은 채 집 타입만 업그레이드가 됨.
         * 플레이어 보드의 타일을 순회하며 Room 인스턴스일 경우  Room 클래스의 setType()을 사용해서 업그레이드
         *
         *
        * */
        RoomType newType = player.chooseRoomTypeForRenovation();
        player.renovateHouse(newType);
    }

//    // 자원 체크 및 지불
//    default boolean checkResources(Player player, Map<String, Integer> resources) {
//        for (Map.Entry<String, Integer> entry : resources.entrySet()) {
//            if (player.getResource(entry.getKey()) < entry.getValue()) {
//                return false; // 자원이 부족하면 false 반환
//            }
//        }
//        return true; // 자원이 충분하면 true 반환
//    }
//
//    default void payResources(Player player, Map<String, Integer> resources) {
//        for (Map.Entry<String, Integer> entry : resources.entrySet()) {
//            player.addResource(entry.getKey(), -entry.getValue());
//        }
//    }

    // 추가 효과 확인 및 적용 메서드
    default void applyAdditionalEffects(Player player) {
        // 추가 효과가 필요한 경우 오버라이드하여 구현
    }
}
