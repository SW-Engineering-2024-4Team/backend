package cards.factory.imp.minor;

import cards.common.ActionRoundCard;
import cards.decorators.UnifiedDecoratorNon;
import cards.factory.imp.action.Worker;
import cards.minorimprovement.MinorImprovementCard;
import cards.occupation.OccupationCard;
import enums.ExchangeTiming;
import models.MainBoard;
import models.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClayPit extends MinorImprovementCard {

    public ClayPit(int id) {
        super(id, "Clay Pit", "날품팔이 행동 카드를 이용할 때, 흙 3개를 추가로 가져옵니다.", null, null, Map.of("food", 1), player -> player.getActiveCards().stream().filter(card -> card instanceof OccupationCard).count() <= 3, ExchangeTiming.NONE, 0);
    }

    @Override
    public void execute(Player player) {
        if (checkResources(player, this.cost) && testCondition(player)) {
            payResources(player, this.cost);
        } else {
            System.out.println("자원이 부족하거나 조건을 만족하지 않습니다.");
            return;
        }

        MainBoard mainBoard = player.getGameController().getMainBoard();
        List<ActionRoundCard> actionCards = mainBoard.getActionCards();
        List<ActionRoundCard> roundCards = mainBoard.getRoundCards();

        List<ActionRoundCard> newActionCards = wrapWithClayPitDecorator(actionCards, player);
        List<ActionRoundCard> newRoundCards = wrapWithClayPitDecorator(roundCards, player);

        // 메인보드의 원래 리스트를 업데이트
        mainBoard.setActionCards(newActionCards);
        mainBoard.setRoundCards(newRoundCards);
    }

    private List<ActionRoundCard> wrapWithClayPitDecorator(List<ActionRoundCard> cards, Player player) {
        List<ActionRoundCard> newCards = new ArrayList<>();
        for (ActionRoundCard card : cards) {
            if (card.getName().equals("날품팔이")) {
                UnifiedDecoratorNon decoratedCard = new ClayPitDecorator(card, player);
                newCards.add(decoratedCard);
            } else {
                newCards.add(card);
            }
        }
        return newCards;
    }

    private static class ClayPitDecorator extends UnifiedDecoratorNon {
        public ClayPitDecorator(ActionRoundCard decoratedCard, Player appliedPlayer) {
            super(decoratedCard, appliedPlayer);
        }

        @Override
        public void gainResources(Player player, Map<String, Integer> resources) {
            super.gainResources(player, resources);
            if (player.equals(appliedPlayer)) {
                int additionalClay = 3;
                player.addResource("clay", additionalClay);
                System.out.println("Clay Pit effect: Gained an additional 3 clay.");
            }
        }

        @Override
        public void execute(Player player) {
            Map<String, Integer> resourcesToGain = getResourcesToGain();
            super.execute(player);
            if (player.equals(appliedPlayer)) {
                gainResources(player, resourcesToGain);
            } else {
                decoratedCard.execute(player);
            }
        }

        private Map<String, Integer> getResourcesToGain() {
            if (decoratedCard instanceof Worker) { // 'SomeActionCard'는 실제로 "날품팔이"를 나타내는 클래스 이름으로 바꾸세요.
                return ((Worker) decoratedCard).createResourcesToGain();
            }
            return null;
        }
    }
}
