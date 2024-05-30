package cards.action;

import cards.action.AccumulativeActionCard;
import models.Player;

import java.util.HashMap;
import java.util.Map;

public class TestAccumulativeActionCard extends AccumulativeActionCard {

    public TestAccumulativeActionCard(int id) {
        super(id, "테스트누적카드", "음식 자원 1개, 나무 자원 2개를 누적합니다.", createAccumulatedAmounts());
    }

    private static Map<String, Integer> createAccumulatedAmounts() {
        Map<String, Integer> accumulatedAmounts = new HashMap<>();
        accumulatedAmounts.put("wood", 2); // 나무 1개
        accumulatedAmounts.put("food", 1); // 음식 1개
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
