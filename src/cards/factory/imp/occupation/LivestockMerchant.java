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

// TODO
public class LivestockMerchant extends OccupationCard {

    public LivestockMerchant(int id) {
        super(id, "Livestock Merchant", "음식 자원을 1개 지불하고 추가로 양을 한 마리 얻습니다.", null, null, 1, 4, ExchangeTiming.NONE);
    }

    @Override
    public void execute(Player player) {
        MainBoard mainBoard = player.getGameController().getMainBoard();
        List<ActionRoundCard> actionCards = mainBoard.getActionCards();
        List<ActionRoundCard> roundCards = mainBoard.getRoundCards();

        List<ActionRoundCard> newActionCards = wrapWithLivestockMerchantDecorator(actionCards, player);
        List<ActionRoundCard> newRoundCards = wrapWithLivestockMerchantDecorator(roundCards, player);

        // 메인보드의 원래 리스트를 업데이트
        mainBoard.setActionCards(newActionCards);
        mainBoard.setRoundCards(newRoundCards);
    }

    private List<ActionRoundCard> wrapWithLivestockMerchantDecorator(List<ActionRoundCard> cards, Player player) {
        List<ActionRoundCard> newCards = new ArrayList<>();
        for (ActionRoundCard card : cards) {
            if (card.getName().equals("양 시장")) {
                UnifiedDecorator decoratedCard = new LivestockMerchantDecorator((AccumulativeCard) card, player);
                newCards.add(decoratedCard);
            } else {
                newCards.add(card);
            }
        }
        return newCards;
    }

    private static class LivestockMerchantDecorator extends UnifiedDecorator {
        public LivestockMerchantDecorator(AccumulativeCard decoratedCard, Player appliedPlayer) {
            super(decoratedCard, appliedPlayer);
        }

        @Override
        public void gainResources(Player player, Map<String, Integer> resources) {
            super.gainResources(player, resources);
            if (player.equals(appliedPlayer) && player.getResource("food") >= 1) {
                player.addResource("food", -1); // 음식 자원 1개 지불
                player.addResource("sheep", 1); // 추가 양 자원 1개 획득
                System.out.println("Livestock Merchant effect: Gained an additional sheep.");
            }
        }

        @Override
        public void execute(Player player) {
            super.execute(player);
//            if (player.equals(appliedPlayer) && player.getResource("food") >= 1) {
//                super.execute(player);
//                gainResources(player, getAccumulatedResources());
//                clearAccumulatedResources();
//            } else {
//                decoratedCard.execute(player);
//            }
            decoratedCard.execute(player);
//            if (player.equals(appliedPlayer) && player.getResource("food") >= 1) {
//                gainResources(player, getAccumulatedResources());
//            }
        }
    }
}
