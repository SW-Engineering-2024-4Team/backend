package cards.factory.imp.major;

import cards.common.BakingCard;
import cards.majorimprovement.MajorImprovementCard;
import enums.ExchangeTiming;
import models.Player;

import java.util.HashMap;
import java.util.Map;

public class ClayOven extends MajorImprovementCard implements BakingCard {
    public ClayOven(int id) {
        super(
                id,
                "흙 가마",
                "빵굽기: 곡식 1개당 음식 5개로 교환 가능. 이 설비를 지을 때 즉시 빵굽기 행동을 할 수 있습니다. 추가점수 2점.",
                createPurchaseCost(),
                null,
                createBreadBakingExchangeRate(),
                2,
                true,
                ExchangeTiming.NONE
        );
    }

    /**
     * 카드의 구매 비용을 생성합니다.
     *
     * @return 구매 비용을 나타내는 맵
     */
    private static Map<String, Integer> createPurchaseCost() {
        Map<String, Integer> cost = new HashMap<>();
        cost.put("clay", 3);
        cost.put("stone", 1);
        return cost;
    }

    /**
     * 빵굽기 교환 비율을 생성합니다.
     *
     * @return 빵굽기 교환 비율을 나타내는 맵
     */
    private static Map<String, Integer> createBreadBakingExchangeRate() {
        Map<String, Integer> rate = new HashMap<>();
        rate.put("grain", 1);
        rate.put("food", 5);
        return rate;
    }

    /**
     * 플레이어가 빵을 굽는 기능을 트리거합니다.
     *
     * @param player 빵굽기 기능을 사용할 플레이어
     */
    @Override
    public void triggerBreadBaking(Player player) {
        int grain = player.getResource("grain");
        int food = grain * 5;
        player.addResource("grain", -grain);
        player.addResource("food", food);
    }
}
