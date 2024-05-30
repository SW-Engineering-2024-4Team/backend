package cards.factory.imp.major;

import java.util.HashMap;
import java.util.Map;
import enums.ExchangeTiming;
import cards.majorimprovement.MajorImprovementCard;
import models.Player;

public class FurnitureWorkshop extends MajorImprovementCard {

    public FurnitureWorkshop(int id) {
        super(
                id, // id
                "가구 제작소", // name
                "수확때 나무 1개당 음식 2개로 교환할 수 있습니다. 점수 계산할 때 나무 3/5/7개당 추가점수 1/2/3을 획득합니다.", // description
                createPurchaseCost(), // purchase cost
                createExchangeRate(), // 수확때 교환 비율
                null, // 빵굽기 교환 비율 없음
                2, // 추가 점수 2
                false, // 즉시 빵굽기 행동 없음
                ExchangeTiming.HARVEST // 수확 시 교환
        );
    }

    private static Map<String, Integer> createPurchaseCost() {
        Map<String, Integer> purchaseCost = new HashMap<>();
        purchaseCost.put("wood", 2); // 나무 2개
        purchaseCost.put("stone", 2); // 돌 2개
        return purchaseCost;
    }

    private static Map<String, Integer> createExchangeRate() {
        Map<String, Integer> exchangeRate = new HashMap<>();
        exchangeRate.put("wood", 1);
        exchangeRate.put("food", 2);
        return exchangeRate;
    }

    public int calculateAdditionalPoints(Player player) {
        int woodCount = player.getResource("wood");
        if (woodCount >= 7) {
            return 3;
        } else if (woodCount >= 5) {
            return 2;
        } else if (woodCount >= 3) {
            return 1;
        } else {
            return 0;
        }
    }
}
