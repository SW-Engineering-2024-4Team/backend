package com.example.agricola.cards.factory.imp.round;


import com.example.agricola.cards.round.NonAccumulativeRoundCard;
import com.example.agricola.models.Player;

public class BuildFence extends NonAccumulativeRoundCard {
    public BuildFence(int id, int cycle) {
        super(id, "울타리", "울타리를 칩니다.", cycle);
    }

    @Override
    public void execute(Player player) {
        buildFence(player);
    }

}
