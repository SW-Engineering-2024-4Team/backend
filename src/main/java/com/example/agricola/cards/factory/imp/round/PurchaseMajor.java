package com.example.agricola.cards.factory.imp.round;


import com.example.agricola.cards.round.NonAccumulativeRoundCard;
import com.example.agricola.models.Player;

public class PurchaseMajor extends NonAccumulativeRoundCard {
    public PurchaseMajor(int id, int cycle) {
        super(id, "주요 설비", "주요 설비 카드를 하나 구매합니다. 또는 보조 설비 카드 하나를 사용합니다.", cycle);
    }

    @Override
    public void execute(Player player) {
        executeOr(player,
                () -> purchaseMajorImprovementCard(player),
                () -> useMinorImprovementCard(player)
        );
    }
}
