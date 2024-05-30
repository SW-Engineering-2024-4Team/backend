package cards.factory.imp.action;

import cards.action.AccumulativeActionCard;
import models.Player;

import java.util.HashMap;
import java.util.Map;

public class Forest extends AccumulativeActionCard {

    public Forest(int id, String name, String description, Map<String, Integer> accumulatedAmounts) {
        super(id, "숲", "나무 자원 3개를 누적합니다.", accumulatedAmounts);
    }

    private static Map<String, Integer> createAccumulatedAmounts() {
        Map<String, Integer> accumulatedAmounts = new HashMap<>();
        accumulatedAmounts.put("wood", 3);
        return accumulatedAmounts;
    }

    @Override
    public void execute(Player player) {
        // 누적된 자원을 플레이어에게 부여
        gainResources(player, getAccumulatedResources());
        // 카드를 점유 상태로 설정
//        setOccupied(true);
        clearAccumulatedResources();
    }

}
