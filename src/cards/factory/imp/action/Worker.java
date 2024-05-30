package cards.factory.imp.action;

import cards.action.NonAccumulativeActionCard;
import models.Player;

import java.util.HashMap;
import java.util.Map;

public class Worker extends NonAccumulativeActionCard {

    public Worker(int id) {
        super(id, "날품팔이", "음식 자원을 2개씩 획득합니다.");
    }

    private Map<String, Integer> createResourcesToGain() {
        Map<String, Integer> resourcesToGain = new HashMap<>();
        resourcesToGain.put("food", 2);
        return resourcesToGain;
    }

    @Override
    public void execute(Player player) {
        gainResources(player, createResourcesToGain());
    }
}
