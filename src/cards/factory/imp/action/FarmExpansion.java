package cards.factory.imp.action;

import cards.action.NonAccumulativeActionCard;
import models.Player;

public class FarmExpansion extends NonAccumulativeActionCard {

    public FarmExpansion(int id) {
        super(id, "농장 확장", "방 만들기 그리고/또는 외양간 짓기");
    }

    @Override
    public void execute(Player player) {
        // 방 만들기 그리고/또는 외양간 짓기
        executeAndOr(player,
                () -> buildHouse(player),
                () -> buildBarn(player)
        );
    }
}
