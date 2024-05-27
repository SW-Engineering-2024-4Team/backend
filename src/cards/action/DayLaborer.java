package cards.action;

import cards.common.ActionRoundCard;
import models.Player;

public class DayLaborer implements ActionRoundCard {
    private int id = 6;
    private String name = "DayLaborer";
    private String description = "Player can get 2 food.";
    private boolean revealed = true;
    private boolean occupied = false;

    @Override
    public void execute(Player player) {
        player.addResource("food", 2);
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
}
