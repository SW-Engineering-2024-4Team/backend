package cards.factory.imp.round;

import cards.round.NonAccumulativeRoundCard;
import models.Player;

public class BuildFence extends NonAccumulativeRoundCard {
    public BuildFence(int id, int cycle) {
        super(id, "울타리", "울타리를 칩니다.", cycle);
    }

    @Override
    public void execute(Player player) {
        buildFence(player);
    }

}
