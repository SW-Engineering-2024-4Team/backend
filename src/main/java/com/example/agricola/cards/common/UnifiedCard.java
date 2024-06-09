package com.example.agricola.cards.common;

import com.example.agricola.models.Player;

public interface UnifiedCard extends com.example.agricola.cards.common.CommonCard {
    void gainResource(Player player);
    default void applyEffect(Player player){

    };
}
