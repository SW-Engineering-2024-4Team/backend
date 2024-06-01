package cards.factory.imp.minor;

import cards.common.ActionRoundCard;
import cards.minorimprovement.MinorImprovementCard;
import cards.decorators.UnifiedDecoratorNon;
import cards.factory.imp.decorators.occupation.RenovateRoomsDecorator;
import enums.ExchangeTiming;
import models.MainBoard;
import models.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoneAxe extends MinorImprovementCard {

    public StoneAxe(int id) {
        super(id, "채굴 망치", "집을 고칠 때 외양간을 무료로 지을 수 있습니다.", createPurchaseCost(), null, createPurchaseCost(), null, ExchangeTiming.NONE, 1);
    }

    private static Map<String, Integer> createPurchaseCost() {
        Map<String, Integer> cost = new HashMap<>();
        cost.put("wood", 1);
        return cost;
    }

    @Override
    public void execute(Player player) {
        // 플레이어에게 음식 자원 1개를 추가로 제공합니다.
        player.addResource("food", 1);

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
            if (hasMethod(card, "renovateRooms")) {
                decoratedCard = new RenovateRoomsDecorator(decoratedCard, player);
            }
            if (hasMethod(card, "buildBarn")) {
                decoratedCard = new BuildBarnDecorator(decoratedCard, player);
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

    private static class BuildBarnDecorator extends UnifiedDecoratorNon {

        public BuildBarnDecorator(ActionRoundCard decoratedCard, Player appliedPlayer) {
            super(decoratedCard, appliedPlayer);
        }

        @Override
        public void buildBarn(Player player) {
            super.buildBarn(player);
            if (player.equals(appliedPlayer)) {
                System.out.println("채굴 망치 효과로 인해 외양간이 무료로 지어집니다.");
            }
        }
    }
}
