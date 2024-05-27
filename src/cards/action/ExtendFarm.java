package cards.action;

import cards.common.ActionRoundCard;
import cards.common.CommonCard;
import models.Player;

public class ExtendFarm implements CommonCard, ActionRoundCard {
    private int id = 1;
    private String name = "ExtendFarm";
    private String description = "Player can build or upgrade house. Additionally, players can also build a barn. ";
    private boolean revealed = true;
    private boolean occupied = false;

    @Override
    public void execute(Player player) {
        while(buildHouse(player) == 1);
        buildBarn(player);
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
    public int buildHouse(Player player) {
        return ActionRoundCard.super.buildHouse(player);
    }

}
