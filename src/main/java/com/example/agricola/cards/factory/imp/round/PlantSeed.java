package com.example.agricola.cards.factory.imp.round;


import com.example.agricola.cards.round.NonAccumulativeRoundCard;
import com.example.agricola.models.Player;

public class PlantSeed extends NonAccumulativeRoundCard {

    public PlantSeed(int id, int cycle) {
        super(id, "곡식 활용", "씨 뿌리기 그리고/또는 빵 굽기", cycle);
    }

    @Override
    public void execute(Player player) {
        // 씨 뿌리기 그리고/또는 빵굽기
        executeAndOr(player,
                () -> plantField(player),
                () -> triggerBreadBaking(player)
        );
    }
}
