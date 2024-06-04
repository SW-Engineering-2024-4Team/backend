package com.example.agricola.cards.factory.imp.minor;


import com.example.agricola.cards.common.ActionRoundCard;
import com.example.agricola.cards.factory.imp.decorators.minorimprovement.BuildFenceDecorator;
import com.example.agricola.cards.minorimprovement.MinorImprovementCard;
import com.example.agricola.enums.ExchangeTiming;
import com.example.agricola.models.MainBoard;
import com.example.agricola.models.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompressedSoil extends MinorImprovementCard {

    public CompressedSoil(int id) {
        super(id, "다진 흙", "흙 1개를 가져옵니다. 울타리를 칠 때 나무 대신 흙을 낼 수 있습니다.", null, null, createPurchaseCost(), null, ExchangeTiming.NONE, 1);
    }

    private static Map<String, Integer> createPurchaseCost() {
        Map<String, Integer> cost = new HashMap<>();
        cost.put("clay", 0);
        return cost;
    }

    @Override
    public void execute(Player player) {
        // 플레이어에게 흙 자원 1개를 추가로 제공합니다.
        player.addResource("clay", 1);

        // CompressedSoil 효과를 활성화합니다.
        player.setCompressedSoilActive(true);

        // 필요한 액션 카드 데코레이터 적용
        applyBuildFenceDecorator(player);
    }

    private void applyBuildFenceDecorator(Player player) {
        MainBoard mainBoard = player.getGameService().getMainBoard();
        List<ActionRoundCard> actionCards = mainBoard.getActionCards();
        List<ActionRoundCard> roundCards = mainBoard.getRoundCards();

        // 액션 카드에 데코레이터 적용
        List<ActionRoundCard> newActionCards = wrapWithDecorators(actionCards, player);
        List<ActionRoundCard> newRoundCards = wrapWithDecorators(roundCards, player);

        // 메인보드의 원래 리스트를 업데이트
        mainBoard.setActionCards(newActionCards);
        mainBoard.setRoundCards(newRoundCards);

        player.getGameService().sendDecoratedCardInfo(player, newActionCards, newRoundCards);
    }

    private List<ActionRoundCard> wrapWithDecorators(List<ActionRoundCard> cards, Player player) {
        List<ActionRoundCard> newCards = new ArrayList<>();
        for (ActionRoundCard card : cards) {
            if (hasBuildFenceMethod(card)) {
                card = new BuildFenceDecorator(card, player);
            }
            newCards.add(card);
        }
        return newCards;
    }

    private boolean hasBuildFenceMethod(ActionRoundCard card) {
        try {
            card.getClass().getMethod("buildFence", Player.class);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
}
