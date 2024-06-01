package cards.factory.imp.decorators.occupation;

import cards.common.ActionRoundCard;
import cards.decorators.UnifiedDecoratorNon;
import models.Player;

public class RenovateRoomsDecorator extends UnifiedDecoratorNon {

    public RenovateRoomsDecorator(ActionRoundCard decoratedCard, Player appliedPlayer) {
        super(decoratedCard, appliedPlayer);
    }

    @Override
    public void renovateRooms(Player player) {
        super.renovateRooms(player);
        if (player.equals(appliedPlayer)) {
            player.addResource("food", 3);
            System.out.println("초벽질공 효과: 음식 3개를 가져옵니다.");
        }
    }

    @Override
    public void execute(Player player) {
        executeThen(player,
                () -> renovateRooms(player),
                () -> buildFence(player)
        );
    }
}