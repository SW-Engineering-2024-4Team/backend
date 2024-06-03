package com.example.agricola.cards.factory.imp.occupation;


import com.example.agricola.cards.occupation.OccupationCard;
import com.example.agricola.enums.ExchangeTiming;
import com.example.agricola.models.Player;

public class Roofer extends OccupationCard {
    public Roofer(int id) {
        super(id, "지붕 다지는 사람", "이 카드를 내려놓으면 즉시 음식 1개를 내고 방 수만큼 돌 1개씩을 가져옵니다.", null, null, 1, 4, ExchangeTiming.NONE);
    }

    @Override
    public void execute(Player player) {
        int roomCount = getRoomCount(player);
        if (roomCount > 0 && player.getResource("food") >= 1) {
            player.addResource("food", -1); // 음식 1개를 내고
            player.addResource("stone", roomCount); // 방 수만큼 돌을 가져옴
        } else {
            System.out.println("조건을 만족하지 못하여 카드를 사용할 수 없습니다.");
        }
    }

    private int getRoomCount(Player player) {
        return player.getPlayerBoard().getRoomCount();
    }
}
