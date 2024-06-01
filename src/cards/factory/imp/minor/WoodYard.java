package cards.factory.imp.minor;

import cards.minorimprovement.MinorImprovementCard;
import enums.ExchangeTiming;
import models.Player;

import java.util.HashMap;
import java.util.Map;

public class WoodYard extends MinorImprovementCard {
    public WoodYard(int id) {
        super(id,
                "목재소",
                "모든 설비의 비용에서 나무 1개씩을 적게 냅니다.",
                null,
                null,
                createPurchaseCost(),
                null,
                ExchangeTiming.NONE,
                2);
    }

    private static Map<String, Integer> createPurchaseCost() {
        Map<String, Integer> cost = new HashMap<>();
        cost.put("stone", 2);
        return cost;
    }

    @Override
    public void execute(Player player) {
        if (player.getOccupationCards().size() <= 3) {
            player.payResources(createPurchaseCost());
            player.setWoodDiscountActive(true);
            System.out.println(player.getName() + " has activated the wood discount effect.");
        }
    }
}
