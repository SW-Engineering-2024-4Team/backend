package test.actionroundcardtest;

import cards.common.AccumulativeCard;
import cards.common.ActionRoundCard;
import models.Player;

import java.util.HashMap;
import java.util.Map;

public class TestAccumulativeActionRoundCard implements ActionRoundCard, AccumulativeCard {
    private int id;
    private String name;
    private String description;
    private boolean revealed;
    private boolean occupied;
    private Map<String, Integer> accumulatedResources;

    public TestAccumulativeActionRoundCard(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.revealed = false;
        this.occupied = false;
        this.accumulatedResources = new HashMap<>();
    }

    @Override
    public void execute(Player player) {
        // 실행 로직: 자원을 획득
        gainResources(player, accumulatedResources);
        // 자원 획득 후 누적된 자원을 초기화
        clearAccumulatedResources();
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
        return true;
    }

    @Override
    public boolean isOccupied() {
        return occupied;
    }

    @Override
    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    //AccumulativeCard interface 메서드추가했으므로 여기도 임의추가
    @Override
    public Map<String, Integer> getAccumulatedResourceInfo() {
        return new HashMap<>();
    }

    @Override
    public Map<String, Integer> getAccumulatedResources() {
        return accumulatedResources;
    }

    @Override
    public void clearAccumulatedResources() {
        accumulatedResources.clear();
    }

    @Override
    public void accumulateResources() {
        // 자원을 누적
        accumulatedResources.put("wood", accumulatedResources.getOrDefault("wood", 0) + 1);
    }
}
