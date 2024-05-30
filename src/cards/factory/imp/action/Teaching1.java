package cards.factory.imp.action;

import cards.action.NonAccumulativeActionCard;
import cards.round.AccumulativeRoundCard;
import models.Player;

import java.util.HashMap;
import java.util.Map;

public class Teaching1 extends NonAccumulativeActionCard {

    public Teaching1(int id) {
        super(id, "교습1", "음식 2개를 지불하고 직업카드를 사용합니다.");
    }

    private static Map<String, Integer> neededResources() {
        Map<String, Integer> neededResources = new HashMap<>();
        neededResources.put("food", 2);

        return neededResources;
    }

    @Override
    public void execute(Player player) {
        if(!checkResources(player, neededResources())) {
            return;
        }
        payResources(player, neededResources());
        useOccupationCard(player);
    }
}
