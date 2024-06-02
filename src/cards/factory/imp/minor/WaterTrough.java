
package cards.factory.imp.minor;

import cards.minorimprovement.MinorImprovementCard;
import enums.ExchangeTiming;
import models.Player;
import models.FenceArea;

import java.util.Map;
import java.util.HashMap;

public class WaterTrough extends MinorImprovementCard {
    private Player appliedPlayer; // 물통 효과가 적용될 플레이어

    public WaterTrough(int id) {
        super(id, "물통",
                "우리(외양간이 있든 없든) 하나당 가축을 2마리씩 더 키울 수 있습니다.",
                null, null,
                createPurchaseCost(), null,
                ExchangeTiming.NONE, 1);
//        this.appliedPlayer = appliedPlayer;
    }


    private static Map<String, Integer> createPurchaseCost() {
        Map<String, Integer> cost = new HashMap<>();
        cost.put("clay", 1);
        return cost;
    }

    @Override
    public void applyEffect(Player player) {
        player.getPlayerBoard().applyWaterTroughEffect(player);
    }
}
