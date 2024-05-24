package cards.round;

import cards.action.AccumulativeActionCard;
import cards.common.AccumulativeCard;
import models.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AccumulativeRoundCard implements AccumulativeCard {
    private int id;
    private String name;
    private String description;
    private int cycle;
    private boolean revealed;
    private boolean occupied;
    private Map<String, Integer> accumulatedResources;

    public AccumulativeRoundCard(int id, String name, String description, int cycle) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cycle = cycle;
        this.revealed = false;
        this.occupied = true;
        this.accumulatedResources = new HashMap<>();
    }

    @Override
    public void execute(Player player) {
        // 라운드 카드 실행 로직
        System.out.println("executed");
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

    public int getCycle() {
        return cycle;
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

    @Override
    public boolean isOccupied() {
        return occupied;
    }

    @Override
    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }


//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        AccumulativeActionCard that = (AccumulativeActionCard) o;
//        return Objects.equals(name, that.name);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(name);
//    }
}
