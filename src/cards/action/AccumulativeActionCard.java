package cards.action;

import cards.common.AccumulativeCard;
import models.Animal;
import models.Player;

import java.util.HashMap;
import java.util.Map;

public class AccumulativeActionCard implements AccumulativeCard {
    private int id;
    private String name;
    private String description;
    private boolean revealed;
    private boolean occupied;
    private Map<String, Integer> accumulatedResources;
    private Map<String, Integer> accumulatedAmounts;

    public AccumulativeActionCard(int id, String name, String description, Map<String, Integer> accumulatedAmounts) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.revealed = false;
        this.occupied = false;
        this.accumulatedResources = new HashMap<>();
        this.accumulatedAmounts = accumulatedAmounts;
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
        return true;
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
        if (!occupied) {
            for (Map.Entry<String, Integer> entry : accumulatedAmounts.entrySet()) {
                String resource = entry.getKey();
                int amount = entry.getValue();
                accumulatedResources.put(resource, accumulatedResources.getOrDefault(resource, 0) + amount);
            }
        } else {
            resetAccumulatedResources();
            setOccupied(false);
        }
    }

    private void resetAccumulatedResources() {
        for (Map.Entry<String, Integer> entry : accumulatedAmounts.entrySet()) {
            accumulatedResources.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public boolean isOccupied() {
        return occupied;
    }

    @Override
    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public void setAccumulatedResources(Map<String, Integer> resources) {
        this.accumulatedResources = resources;
    }

}
