package com.example.agricola.cards.factory.imp.action;


import com.example.agricola.cards.action.NonAccumulativeRoundCard;
import com.example.agricola.models.Player;

public class MeetingPlace extends NonAccumulativeRoundCard {

    public MeetingPlace(int id) {
        super(id, "회합 장소", "선 플레이어 되기 그리고/또는 보조 설비 카드 사용하기");
    }

    @Override
    public void execute(Player player) {
        // 방 만들기 그리고/또는 외양간 짓기
        executeAndOr(player,
                () -> becomeFirstPlayer(player),
                () -> useMinorImprovementCard(player)
        );
    }
}
