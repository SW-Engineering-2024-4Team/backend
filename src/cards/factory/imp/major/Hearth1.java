
package cards.factory.imp.major;

import cards.common.BakingCard;
import cards.majorimprovement.MajorImprovementCard;
import enums.ExchangeTiming;
import models.Player;

import java.util.HashMap;
import java.util.Map;

public class Hearth1 extends MajorImprovementCard implements BakingCard {
    public Hearth1(int id) {
        super(id, "Hearth1",
                "아무때나 양 한 마리를 음식 2개로 교환 가능. 빵굽기: 곡식 1개를 음식 2개로 교환 가능. 추가점수 1점.",
                createPurchaseCost(),
                createAExchangeRate(),
                createBreadBakingExchangeRate(),
                1,
                false,
                ExchangeTiming.ANYTIME);
    }

    private static Map<String, Integer> createPurchaseCost() {
        Map<String, Integer> cost = new HashMap<>();
        cost.put("clay", 2);
        return cost;
    }

    private static Map<String, Integer> createAExchangeRate() {
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