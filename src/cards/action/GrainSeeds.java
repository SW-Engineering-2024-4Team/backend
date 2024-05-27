package cards.action;

import cards.common.ActionRoundCard;
import models.Player;

import java.util.HashMap;
import java.util.Map;

public class GrainSeeds implements ActionRoundCard {
    private int id = 3;
    private String name = "GrainSeeds";
    private String description = "Player can gain Grainseeds ";
    private boolean revealed = true;
    private boolean occupied = false;

    @Override
    public void execute(Player player) {
        Map<String, Integer> resource = new HashMap<>();
        resource.put("grain", 1);
        gainResources(player,resource);
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
    /*Action Card는 무조건 true*/
    public boolean isRevealed() {
        return true;
    }

    @Override
    public void reveal() {
        ;
    }

    @Override
    public boolean isAccumulative() {
        return false;
    }

    @Override
    public boolean isOccupied() {
        return occupied;
    }

    @Override
    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    @Override
    public void gainResources(Player player, Map<String, Integer> resources) {
        ActionRoundCard.super.gainResources(player, resources);
    }
}
