package cards.action;

import cards.common.AccumulativeCard;
import models.Player;

import java.util.HashMap;
import java.util.Map;

public class AccumulativeActionCard implements AccumulativeCard {
    private int id;
    private String name;
    private String description;
    private boolean revealed;
    private Map<String, Integer> accumulatedResources;

    public AccumulativeActionCard(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.revealed = false;
        this.accumulatedResources = new HashMap<>();
    }

    @Override
    public void execute(Player player) {
        // 액션 카드 실행 로직
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isRevealed() {
        return revealed;
    }

    @Override
    public void reveal() {
        revealed = true;
    }

    @Override
    public boolean isAccumulative() {
        return true; // 자원 누적 가능
    }

    @Override
    public Map<String, Integer> getAccumulatedResources() {
        return accumulatedResources;
    }

    @Override
    public void clearAccumulatedResources() {
        accumulatedResources.clear();
    }
}
