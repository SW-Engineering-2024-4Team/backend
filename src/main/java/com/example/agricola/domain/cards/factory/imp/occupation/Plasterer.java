package com.example.agricola.domain.cards.factory.imp.occupation;

import cards.common.ActionRoundCard;
import cards.factory.imp.decorators.occupation.BuildHouseDecorator;
import cards.factory.imp.decorators.occupation.RenovateRoomsDecorator;
import cards.occupation.OccupationCard;
import enums.ExchangeTiming;
import models.MainBoard;
import models.Player;

import java.util.ArrayList;
import java.util.List;

public class Plasterer extends OccupationCard {

    public Plasterer(int id) {
        super(id, "초벽질공", "방을 만들거나 집을 고칠 때, 음식 3개를 가져옵니다.", null, null, 1, 4, ExchangeTiming.NONE);
    }

    @Override
    public void execute(Player player) {
        MainBoard mainBoard = player.getGameController().getMainBoard();
        List<ActionRoundCard> actionCards = mainBoard.getActionCards();
        List<ActionRoundCard> roundCards = mainBoard.getRoundCards();

        List<ActionRoundCard> newActionCards = wrapWithDecorators(actionCards, player);
        List<ActionRoundCard> newRoundCards = wrapWithDecorators(roundCards, player);

        // 메인보드의 원래 리스트를 업데이트
        mainBoard.setActionCards(newActionCards);
        mainBoard.setRoundCards(newRoundCards);
    }

    private List<ActionRoundCard> wrapWithDecorators(List<ActionRoundCard> cards, Player player) {
        List<ActionRoundCard> newCards = new ArrayList<>();
        for (ActionRoundCard card : cards) {
            ActionRoundCard decoratedCard = card;
            if (hasMethod(card, "buildHouse")) {
                decoratedCard = new BuildHouseDecorator(decoratedCard, player);
            }
            if (hasMethod(card, "renovateRooms")) {
                decoratedCard = new RenovateRoomsDecorator(decoratedCard, player);
            }
            newCards.add(decoratedCard);
        }
        return newCards;
    }

    private boolean hasMethod(ActionRoundCard card, String methodName) {
        try {
            card.getClass().getMethod(methodName, Player.class);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
}
