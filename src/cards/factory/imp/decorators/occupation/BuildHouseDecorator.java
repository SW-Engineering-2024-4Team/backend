package cards.factory.imp.decorators.occupation;

import cards.common.ActionRoundCard;
import cards.decorators.UnifiedDecoratorNon;
import models.Player;

public class BuildHouseDecorator extends UnifiedDecoratorNon {

    public BuildHouseDecorator(ActionRoundCard decoratedCard, Player appliedPlayer) {
        super(decoratedCard, appliedPlayer);
    }

    @Override
    public void buildHouse(Player player) {
        super.buildHouse(player);
        if (player.equals(appliedPlayer)) {
            player.addResource("food", 3);
            System.out.println("초벽질공 효과: 음식 3개를 가져옵니다.");
        }
    }
}