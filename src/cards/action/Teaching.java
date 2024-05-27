package cards.action;

import cards.common.ActionRoundCard;
import models.Player;

import java.util.HashMap;
import java.util.Map;

public class Teaching implements ActionRoundCard {
    private int id = 5;
    private String name = "Teaching";
    private String description = "Player can use jobcard when player pay 1 food";
    private boolean revealed = true;
    private boolean occupied = false;

    @Override
    public void execute(Player player) {
        Map<String, Integer> cost = new HashMap<>();
        cost.put("food", 1);
        if (player.checkResources(cost)) {
            player.addResource("food", -1);
        }
        useOccupationCard(player);
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
    public void useOccupationCard(Player player) {
        ActionRoundCard.super.useOccupationCard(player);
    }
}
