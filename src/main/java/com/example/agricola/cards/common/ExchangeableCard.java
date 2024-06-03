package com.example.agricola.cards.common;

import com.example.agricola.enums.ExchangeTiming;
import com.example.agricola.models.Player;


import java.util.Map;

public interface ExchangeableCard {
    boolean canExchange(ExchangeTiming timing);
    void executeExchange(Player player, String fromResource, String toResource, int amount);
    Map<String, Integer> getExchangeRate();

}
