package com.example.agricola.cards.factory.imp.occupation;


import com.example.agricola.cards.common.AccumulativeCard;
import com.example.agricola.cards.common.ActionRoundCard;
import com.example.agricola.cards.decorators.UnifiedDecorator;
import com.example.agricola.cards.occupation.OccupationCard;
import com.example.agricola.enums.ExchangeTiming;
import com.example.agricola.models.MainBoard;
import com.example.agricola.models.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Magician extends OccupationCard {

    public Magician(int id) {
        super(id, "마술사", "유랑 극단 칸을 이용할 시 추가 자원을 얻습니다(곡식1, 나무1).", null, null, 1, 4, ExchangeTiming.NONE);
    }

    @Override
    public void execute(Player player) {
        MainBoard mainBoard = player.getGameService().getMainBoard();
        List<ActionRoundCard> actionCards = mainBoard.getActionCards();
        List<ActionRoundCard> roundCards = mainBoard.getRoundCards();

        List<ActionRoundCard> newActionCards = wrapWithMagicianDecorator(actionCards, player);
        List<ActionRoundCard> newRoundCards = wrapWithMagicianDecorator(roundCards, player);

        // 메인보드의 원래 리스트를 업데이트
        mainBoard.setActionCards(newActionCards);
        mainBoard.setRoundCards(newRoundCards);

        // 데코레이터 효과를 받는 카드들의 정보를 프론트엔드로 전송
        player.getGameService().sendDecoratedCardInfo(player, newActionCards, newRoundCards);

    }

    private List<ActionRoundCard> wrapWithMagicianDecorator(List<ActionRoundCard> cards, Player player) {
        List<ActionRoundCard> newCards = new ArrayList<>();
        for (ActionRoundCard card : cards) {
            if (card.getName().equals("유랑극단")) {
                UnifiedDecorator decoratedCard = new MagicianDecorator((AccumulativeCard) card, player);
                newCards.add(decoratedCard);
            } else {
                newCards.add(card);
            }
        }
        return newCards;
    }

    private static class MagicianDecorator extends UnifiedDecorator {
        public MagicianDecorator(AccumulativeCard decoratedCard, Player appliedPlayer) {
            super(decoratedCard, appliedPlayer);
        }

        @Override
        public void setOccupiedPlayerId(String id) {

        }

        @Override
        public void gainResources(Player player, Map<String, Integer> resources) {
            super.gainResources(player, resources);
            if (player.equals(appliedPlayer)) {
                int additionalWood = 1;
                int additionalGrain = 1;
                player.addResource("wood", additionalWood);
                player.addResource("grain", additionalGrain);
                System.out.println("Magician effect: Gained an additional wood and grain.");
            }
        }

        @Override
        public void execute(Player player) {
            super.execute(player);
            if (player.equals(appliedPlayer)) {
                gainResources(player, getAccumulatedResources());
                clearAccumulatedResources();
            }
            decoratedCard.execute(player);
        }
    }
}
