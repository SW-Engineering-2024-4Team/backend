package com.example.agricola.cards.factory.imp.round;

import com.example.agricola.cards.round.NonAccumulativeRoundCard;
import com.example.agricola.models.Player;

public class RenovateFarms extends NonAccumulativeRoundCard {
    public RenovateFarms(int id, int cycle) {
        super(id, "농장 개조", "집을 고칩니다. 한 후에 울타리를 칩니다.", cycle);
    }

    @Override
    public void execute(Player player) {
        executeThen(player,
                () -> renovateRooms(player),
                () -> buildFence(player)
        );
    }
}
