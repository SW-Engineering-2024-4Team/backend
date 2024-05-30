package cards.factory.imp.occupation;

import cards.occupation.OccupationCard;
import enums.ExchangeTiming;
import java.util.HashMap;
import java.util.Map;

public class ShepherdCard extends OccupationCard {

    public ShepherdCard(int id, ExchangeTiming exchangeTiming) {
        super(id, "Shepherd", "Exchange 1 sheep for 1 stone anytime.", createExchangeRate(), null, 1, 4, exchangeTiming);
    }

    private static Map<String, Integer> createExchangeRate() {
        Map<String, Integer> exchangeRate = new HashMap<>();
        exchangeRate.put("sheep", 1);
        exchangeRate.put("stone", 1);
        return exchangeRate;
    }
}
