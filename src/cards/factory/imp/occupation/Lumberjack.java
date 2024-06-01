package cards.factory.imp.occupation;

import cards.common.AccumulativeCard;
import cards.common.ActionRoundCard;
import cards.decorators.UnifiedDecorator;
import cards.occupation.OccupationCard;
import enums.ExchangeTiming;
import models.MainBoard;
import models.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Lumberjack extends OccupationCard {

    public Lumberjack(int id) {
        super(id, "Lumberjack", "나무 자원 누적칸을 사용할 시 나무를 추가로 1개 얻습니다.", null, null, 1, 4, ExchangeTiming.NONE);
    }

    @Override
    public void execute(Player player) {
        MainBoard mainBoard = player.getGameController().getMainBoard();
        List<ActionRoundCard> actionCards = mainBoard.getActionCards();
        List<ActionRoundCard> roundCards = mainBoard.getRoundCards();

        List<ActionRoundCard> newActionCards = wrapWithLumberjackDecorator(actionCards, player);
        List<ActionRoundCard> newRoundCards = wrapWithLumberjackDecorator(roundCards, player);

        // 메인보드의 원래 리스트를 업데이트
        mainBoard.setActionCards(newActionCards);
        mainBoard.setRoundCards(newRoundCards);
    }

    private List<ActionRoundCard> wrapWithLumberjackDecorator(List<ActionRoundCard> cards, Player player) {
        List<ActionRoundCard> newCards = new ArrayList<>();
        for (ActionRoundCard card : cards) {
            if (card instanceof AccumulativeCard && ((AccumulativeCard) card).getAccumulatedAmounts().containsKey("wood")) {
                UnifiedDecorator decoratedCard = new LumberjackDecorator((AccumulativeCard) card, player);
                newCards.add(decoratedCard);
            } else {
                newCards.add(card);
            }
        }
        return newCards;
    }

    private static class LumberjackDecorator extends UnifiedDecorator {
        public LumberjackDecorator(AccumulativeCard decoratedCard, Player appliedPlayer) {
            super(decoratedCard, appliedPlayer);
        }

        @Override
        public void gainResources(Player player, Map<String, Integer> resources) {
            super.gainResources(player, resources);
            if (player.equals(appliedPlayer) && resources.containsKey("wood")) {
                int additionalWood = 1;
                player.addResource("wood", additionalWood);
                System.out.println("Lumberjack effect: Gained an additional wood.");
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

    @Override
    public void gainResources(Player player, Map<String, Integer> resources) {
        if (resources.containsKey("wood")) {
            int additionalWood = 1;
            player.addResource("wood", additionalWood);
            System.out.println("Lumberjack effect: Gained an additional wood.");
        }
    }
}

