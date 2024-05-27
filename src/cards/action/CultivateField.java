package cards.action;

import cards.common.ActionRoundCard;
import models.Player;

public class CultivateField implements ActionRoundCard {
    private int id = 4;
    private String name = "CultivateField";
    private String description = "Player can cultivate field ";
    private boolean revealed = true;
    private boolean occupied = false;

    @Override
    public void execute(Player player) {
        plowField(player);
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
    public void plowField(Player player) {
        ActionRoundCard.super.plowField(player);
    }
}
