package cards.factory.imp.minor;

import cards.common.ActionRoundCard;
import cards.common.ExchangeableCard;
import cards.common.UnifiedCard;
import cards.decorators.UnifiedDecorator;
import cards.decorators.UnifiedDecoratorNon;
import cards.minorimprovement.MinorImprovementCard;
import enums.ExchangeTiming;
import models.MainBoard;
import models.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class GrainShovel extends MinorImprovementCard {

    public GrainShovel(int id) {
        super(id, "곡식용 삽", "곡식 종자 행동 카드를 이용할 때, 곡식 1개를 추가로 가져옵니다.", null, null, Map.of("wood", 1), null, ExchangeTiming.NONE, 0);
    }

    @Override
    public void execute(Player player) {
        if (checkResources(player, this.cost)) {
            payResources(player, this.cost);
        } else {
            System.out.println("자원이 부족합니다.");
            return;
        }

        MainBoard mainBoard = player.getGameController().getMainBoard();
        List<ActionRoundCard> actionCards = mainBoard.getActionCards();
        List<ActionRoundCard> roundCards = mainBoard.getRoundCards();

        List<ActionRoundCard> newActionCards = wrapWithGrainShovelDecorator(actionCards, player);
        List<ActionRoundCard> newRoundCards = wrapWithGrainShovelDecorator(roundCards, player);

        // 메인보드의 원래 리스트를 업데이트
        mainBoard.setActionCards(newActionCards);
        mainBoard.setRoundCards(newRoundCards);
    }

    private List<ActionRoundCard> wrapWithGrainShovelDecorator(List<ActionRoundCard> cards, Player player) {
        List<ActionRoundCard> newCards = new ArrayList<>();
        for (ActionRoundCard card : cards) {
            if (card.getName().equals("곡식 종자")) {
                UnifiedDecoratorNon decoratedCard = new GrainShovelDecorator(card, player);
                newCards.add(decoratedCard);
            } else {
                newCards.add(card);
            }
        }
        return newCards;
    }

    private static class GrainShovelDecorator extends UnifiedDecoratorNon {
        public GrainShovelDecorator(ActionRoundCard decoratedCard, Player appliedPlayer) {
            super(decoratedCard, appliedPlayer);
        }

        @Override
        public void gainResources(Player player, Map<String, Integer> resources) {
            super.gainResources(player, resources);
            if (player.equals(appliedPlayer)) {
                int additionalGrain = 1;
                player.addResource("grain", additionalGrain);
                System.out.println("Grain Shovel effect: Gained an additional grain.");
            }
        }

        @Override
        public void execute(Player player) {
            Map<String, Integer> resourcesToGain = createResourcesToGain();
            super.execute(player);
            if (player.equals(appliedPlayer)) {
                gainResources(player, resourcesToGain);
            } else {
                decoratedCard.execute(player);
            }
        }

        private Map<String, Integer> createResourcesToGain() {
            Map<String, Integer> resourcesToGain = new HashMap<>();
            resourcesToGain.put("grain", 1); // 곡식 1개 획득
            return resourcesToGain;
        }
    }
}
