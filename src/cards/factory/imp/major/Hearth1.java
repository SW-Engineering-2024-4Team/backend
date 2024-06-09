
package cards.factory.imp.major;

import cards.common.BakingCard;
import cards.majorimprovement.MajorImprovementCard;
import enums.ExchangeTiming;
import models.Player;

import java.util.HashMap;
import java.util.Map;

public class Hearth1 extends MajorImprovementCard implements BakingCard {

    /**
     * `Hearth1` 생성자.
     *
     * @param id 카드의 고유 식별자
     */
    public Hearth1(int id) {
        super(id, "화로1",
                "아무때나 양 한 마리를 음식 2개로 교환 가능. 빵굽기: 곡식 1개를 음식 2개로 교환 가능. 추가점수 1점.",
                createPurchaseCost(),
                createAExchangeRate(),
                createBreadBakingExchangeRate(),
                1,
                false,
                ExchangeTiming.ANYTIME);
    }

    /**
     * 카드의 구매 비용을 생성합니다.
     *
     * @return 구매 비용을 나타내는 맵
     */
    private static Map<String, Integer> createPurchaseCost() {
        Map<String, Integer> cost = new HashMap<>();
        cost.put("clay", 2);
        return cost;
    }

    /**
     * 자원 교환 비율을 생성합니다.
     *
     * @return 자원 교환 비율을 나타내는 맵
     */
    private static Map<String, Integer> createAExchangeRate() {
        Map<String, Integer> rate = new HashMap<>();
        rate.put("sheep", 1);
        rate.put("food", 2);
        return rate;
    }

    /**
     * 빵굽기 교환 비율을 생성합니다.
     *
     * @return 빵굽기 교환 비율을 나타내는 맵
     */
    private static Map<String, Integer> createBreadBakingExchangeRate() {
        Map<String, Integer> rate = new HashMap<>();
        rate.put("grain", 1);
        rate.put("food", 2);
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
        int food = grain * 2;
        player.addResource("grain", -grain);
        player.addResource("food", food);
    }

}