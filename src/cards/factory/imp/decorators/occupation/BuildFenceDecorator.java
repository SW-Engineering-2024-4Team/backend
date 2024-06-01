package cards.factory.imp.decorators.occupation;

import cards.common.ActionRoundCard;
import cards.decorators.UnifiedDecoratorNon;
import models.Player;

import java.util.Map;

public class BuildFenceDecorator extends UnifiedDecoratorNon {

    public BuildFenceDecorator(ActionRoundCard decoratedCard, Player appliedPlayer) {
        super(decoratedCard, appliedPlayer);
    }

    @Override
    public void buildFence(Player player) {
        super.buildFence(player);
        if (player.equals(appliedPlayer)) {
//            Map<String, Integer> requiredResources = player.getPlayerBoard().calculateRequiredWoodForFences();
//            // TODO 나무대신 흙을 낼지 말지
//            if (requiredResources.containsKey("wood") && player.getResource("clay") >= requiredResources.get("wood")) {
//                int woodNeeded = requiredResources.get("wood");
//                player.addResource("clay", -woodNeeded);
//                player.addResource("wood", woodNeeded);
//                System.out.println("다진 흙 효과: 울타리를 치기 위해 나무 대신 흙을 사용합니다.");
//            }
        }
    }
}
