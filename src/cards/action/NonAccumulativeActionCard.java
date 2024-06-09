package cards.action;

import cards.common.ActionRoundCard;
import models.Player;

import java.util.Map;
import java.util.Objects;

public class NonAccumulativeActionCard implements ActionRoundCard {
    private int id;
    public String name;
    private String description;
    private boolean revealed;
    private boolean occupied;
    private Map<String, Integer> resourcesToGain;

    public NonAccumulativeActionCard(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.revealed = false;
        this.occupied = false;
    }

    @Override
    public void execute(Player player) {
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
        return false; // 자원 누적 불가능
    }

    @Override
    public boolean isOccupied() {
        return occupied;
    }

    @Override
    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

}
