package com.example.agricola.cards.factory.imp.action;


import com.example.agricola.cards.action.NonAccumulativeRoundCard;
import com.example.agricola.models.Player;

public class PlowField extends NonAccumulativeRoundCard {

    public PlowField(int id) {
        super(id, "밭 일구기", "밭을 1개 일굽니다.");
    }

    @Override
    public void execute(Player player) {
        plowField(player);
    }
}
