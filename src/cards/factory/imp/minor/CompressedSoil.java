package cards.factory.imp.minor;

import cards.common.ActionRoundCard;
import cards.factory.imp.decorators.occupation.BuildFenceDecorator;
import cards.minorimprovement.MinorImprovementCard;
import enums.ExchangeTiming;
import models.MainBoard;
import models.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompressedSoil extends MinorImprovementCard {

    public CompressedSoil(int id) {
        super(id, "다진 흙", "울타리를 칠 때 나무 대신 흙을 낼 수 있습니다.", createPurchaseCost(), null, createPurchaseCost(), null, ExchangeTiming.NONE, 1);
    }

    private static Map<String, Integer> createPurchaseCost() {
        Map<String, Integer> cost = new HashMap<>();
        cost.put("clay", 2);
        return cost;
    }

    @Override
    public void execute(Player player) {
        // 플레이어에게 흙 자원 1개를 추가로 제공합니다.
        player.addResource("clay", 1);

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
            if (hasMethod(card, "buildFence")) {
                decoratedCard = new BuildFenceDecorator(decoratedCard, player);
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
