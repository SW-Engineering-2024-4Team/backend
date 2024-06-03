package com.example.agricola.cards.factory.imp.occupation;
import com.example.agricola.cards.occupation.OccupationCard;
import com.example.agricola.enums.ExchangeTiming;
import com.example.agricola.models.Player;

public class MasterBuilder extends OccupationCard {
    public MasterBuilder(int id) {
        super(id, "숙련 벽돌공", "주요설비의 비용을 추가로 지은 방의 수 만큼 돌을 적게 냅니다.", null, null, 1, 4, ExchangeTiming.NONE);
    }

    @Override
    public void execute(Player player) {
        int additionalRooms = player.getPlayerBoard().getAdditionalRoomsCount();
        player.setStoneDiscount(additionalRooms);
    }
}
