package cards.common;

import enums.ExchangeTiming;
import models.Player;

import java.util.Map;

public interface ExchangeableCard {
    boolean canExchange(ExchangeTiming timing);
    void executeExchange(Player player, String fromResource, String toResource, int amount);
    Map<String, Integer> getExchangeRate();
}
