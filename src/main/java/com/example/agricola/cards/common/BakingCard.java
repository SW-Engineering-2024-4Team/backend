package com.example.agricola.cards.common;

import com.example.agricola.models.Player;

public interface BakingCard extends CommonCard {
    void triggerBreadBaking(Player player);
    boolean hasBreadBakingExchangeRate();
}
