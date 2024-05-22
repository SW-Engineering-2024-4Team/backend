
package cards.majorimprovement;

import cards.common.BakingCard;
import models.Player;

import java.util.HashMap;
import java.util.Map;

public class Hearth extends MajorImprovementCard implements BakingCard {
    public Hearth(int id) {
        super(id, "Hearth", "아무때나 양 한 마리를 음식 2개로 교환 가능. 빵굽기: 곡식 1개를 음식 2개로 교환 가능. 추가점수 1점.",
                createPurchaseCost(), createAnytimeExchangeRate(), createBreadBakingExchangeRate(), 1, false);
    }

    private static Map<String, Integer> createPurchaseCost() {
        Map<String, Integer> cost = new HashMap<>();
        cost.put("clay", 3);
        return cost;
    }

    private static Map<String, Integer> createAnytimeExchangeRate() {
        Map<String, Integer> rate = new HashMap<>();
        rate.put("sheep", 1);
        rate.put("food", 2);
        return rate;
    }

    private static Map<String, Integer> createBreadBakingExchangeRate() {
        Map<String, Integer> rate = new HashMap<>();
        rate.put("grain", 1);
        rate.put("food", 2);
        return rate;
    }

    @Override
    public void triggerBreadBaking(Player player) {
        int grain = player.getResource("grain");
        int food = grain * 2;
        player.addResource("grain", -grain);
        player.addResource("food", food);
    }
}