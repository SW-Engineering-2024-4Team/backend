package com.example.agricola.cards.factory.imp.round;


import com.example.agricola.cards.round.NonAccumulativeRoundCard;
import com.example.agricola.models.Player;

public class AddFamilyMember extends NonAccumulativeRoundCard {
    public AddFamilyMember(int id, int cycle) {
        super(id, "기본 가족 늘리기", "가족 구성원을 늘립니다. 한 후에 보조 설비 카드를 하나 사용합니다..", cycle);
    }

    @Override
    public void execute(Player player) {
        executeThen(player,
                () -> addNewborn(player),
                () -> useMinorImprovementCard(player)
        );
    }
}
