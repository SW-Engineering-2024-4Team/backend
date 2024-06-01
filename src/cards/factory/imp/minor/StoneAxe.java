package cards.factory.imp.minor;

import cards.common.ActionRoundCard;
import cards.minorimprovement.MinorImprovementCard;
import cards.decorators.UnifiedDecoratorNon;
import enums.ExchangeTiming;
import models.MainBoard;
import models.Player;

import java.util.*;

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
        player.payResources(createPurchaseCost());
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
        public void execute(Player player) {
            if (appliedPlayer.equals(player)) {
                executeThen(player,
                        () -> renovateRooms(player),
                        () -> buildRandomBarn(player),
                        () -> buildFence(player)
                );
            } else {
                decoratedCard.execute(player);
            }
        }

        private void executeThen(Player player, Runnable o1, Runnable o2, Runnable o3) {
            o1.run();
            o2.run();
            o3.run();
        }

        private void buildRandomBarn(Player player) {
            Set<int[]> validPositions = player.getPlayerBoard().getValidBarnPositions();
            if (validPositions.isEmpty()) {
                System.out.println("No valid positions to build a barn.");
                return;
            }

            int[] position = getRandomPosition(validPositions);
            player.getPlayerBoard().buildBarn(position[0], position[1]);
            System.out.println("Barn built at: (" + position[0] + ", " + position[1] + ")");
        }

        private int[] getRandomPosition(Set<int[]> positions) {
            int size = positions.size();
            int item = new Random().nextInt(size); // 랜덤 인덱스 선택
            int i = 0;
            for (int[] pos : positions) {
                if (i == item) {
                    return pos;
                }
                i++;
            }
            return null;
        }
    }
}
