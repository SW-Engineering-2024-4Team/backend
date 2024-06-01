package cards.factory.imp.occupation;

import cards.occupation.OccupationCard;
import enums.ExchangeTiming;
import java.util.HashMap;
import java.util.Map;

public class ShepherdCard extends OccupationCard {

    public ShepherdCard(int id) {
        super(id, "Shepherd", "아무때나 양 1마리를 돌 1개로 바꿉니다.", createExchangeRate(), null, 1, 4, ExchangeTiming.ANYTIME);
    }

    private static Map<String, Integer> createExchangeRate() {
        Map<String, Integer> exchangeRate = new HashMap<>();
        exchangeRate.put("sheep", 1);
        exchangeRate.put("stone", 1);
        return exchangeRate;
    }
}
