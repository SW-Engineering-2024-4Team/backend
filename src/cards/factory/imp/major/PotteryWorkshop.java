package cards.factory.imp.major;

import java.util.HashMap;
import java.util.Map;
import enums.ExchangeTiming;
import cards.majorimprovement.MajorImprovementCard;
import models.Player;

public class PotteryWorkshop extends MajorImprovementCard {

    public PotteryWorkshop(int id) {
        super(
                id, // id
                "그릇 제작소", // name
                "수확때 흙 1개당 음식 2개로 교환합니다. 점수 계산할 때, 흙 3/5/7개 보유시 추가점수 1/2/3을 얻습니다.", // description
                createPurchaseCost(), // purchase cost
                createExchangeRate(), // 수확때 교환 비율
                null, // 빵굽기 교환 비율 없음
                2, // 기본 추가 점수 없음
                false, // 즉시 빵굽기 행동 없음
                ExchangeTiming.HARVEST // 수확 시 교환
        );
    }

    private static Map<String, Integer> createPurchaseCost() {
        Map<String, Integer> purchaseCost = new HashMap<>();
        purchaseCost.put("stone", 2); // 돌 2개
        purchaseCost.put("clay", 2); // 흙 2개
        return purchaseCost;
    }

    private static Map<String, Integer> createExchangeRate() {
        Map<String, Integer> exchangeRate = new HashMap<>();
        exchangeRate.put("clay", 1);
        exchangeRate.put("food", 2);
        return exchangeRate;
    }

    public int calculateAdditionalPoints(Player player) {
        int clayCount = player.getResource("clay");
        if (clayCount >= 7) {
            return 3;
        } else if (clayCount >= 5) {
            return 2;
        } else if (clayCount >= 3) {
            return 1;
        } else {
            return 0;
        }
    }
}
