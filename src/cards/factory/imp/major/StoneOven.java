package cards.factory.imp.major;

import java.util.HashMap;
import java.util.Map;
import enums.ExchangeTiming;
import cards.majorimprovement.MajorImprovementCard;
import models.Player;

public class StoneOven extends MajorImprovementCard {

    public StoneOven(int id) {
        super(
                id, // id
                "돌가마", // name
                "빵굽기시 곡식 2개로 음식 4개의 교환비로 바꿀 수 있습니다. 이 주요 설비를 구매할 때 즉시 빵굽기 행동을 합니다.", // description
                createPurchaseCost(), // purchase cost
                null, // exchange rate 없음
                createBreadBakingExchangeRate(), // bread baking exchange rate
                3, // additional points
                true, // immediate baking action
                ExchangeTiming.NONE // exchange timing
        );
    }

    private static Map<String, Integer> createPurchaseCost() {
        Map<String, Integer> purchaseCost = new HashMap<>();
        purchaseCost.put("clay", 1); // 예시로 진흙 3개로 설정
        purchaseCost.put("stone", 3); // 예시로 돌 1개로 설정
        return purchaseCost;
    }

    private static Map<String, Integer> createBreadBakingExchangeRate() {
        Map<String, Integer> breadBakingExchangeRate = new HashMap<>();
        breadBakingExchangeRate.put("grain", 2);
        breadBakingExchangeRate.put("food", 4);
        return breadBakingExchangeRate;
    }

    @Override
    public void triggerBreadBaking(Player player) {
        int grain = player.getResource("grain");
        int food = grain * 2;
        player.addResource("grain", -grain);
        player.addResource("food", food);
    }
}
