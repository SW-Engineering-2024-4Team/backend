package cards.factory.imp.action;

import cards.action.AccumulativeActionCard;
import models.Player;

import java.util.HashMap;
import java.util.Map;

public class ReedField extends AccumulativeActionCard {

    public ReedField(int id) {
        super(id, "갈대밭", "나무 1개를 누적합니다..", createAccumulatedAmounts());
    }

    private static Map<String, Integer> createAccumulatedAmounts() {
        Map<String, Integer> accumulatedAmounts = new HashMap<>();
        accumulatedAmounts.put("wood", 1); // 음식 1개를 누적
        return accumulatedAmounts;
    }

    @Override
    public void execute(Player player) {
        // 누적된 자원을 플레이어에게 부여
        gainResources(player, getAccumulatedResources());
        // 카드를 점유 상태로 설정
        setOccupied(true);
        clearAccumulatedResources();
    }
}