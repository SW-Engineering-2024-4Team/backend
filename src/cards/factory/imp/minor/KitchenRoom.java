package cards.factory.imp.minor;

import cards.minorimprovement.MinorImprovementCard;
import enums.ExchangeTiming;
import enums.RoomType;
import models.Player;

import java.util.HashMap;
import java.util.Map;

public class KitchenRoom extends MinorImprovementCard {

    public KitchenRoom(int id) {
        super(id,
                "부엌방",
                "나무집에 살고 있다면 음식 3개를 가져옵니다.",
                null,
                null,
                createPurchaseCost(),
                KitchenRoom::condition,
                ExchangeTiming.NONE,
                0);
    }

    private static Map<String, Integer> createPurchaseCost() {
        Map<String, Integer> cost = new HashMap<>();
        cost.put("wood", 1);
        cost.put("clay", 1);
        return cost;
    }

    private static boolean condition(Player player) {
        return player.getPlayerBoard().getExistingRoomType() == RoomType.WOOD;
    }


    public void applyEffect(Player player) {
        if (condition(player)) {
            player.addResource("food", 3);
            System.out.println("부엌방 효과: 나무집에 살아서 음식 3개를 가져옵니다.");
        } else {
            System.out.println("부엌방 효과: 나무집에 살고 있지 않아 효과가 적용되지 않습니다.");
        }
    }
}
