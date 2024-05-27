package cards.action;

import cards.common.ActionRoundCard;
import models.Player;

public class MeetingPlace implements ActionRoundCard {
    private int id = 2;
    private String name = "Meeting Place";
    private String description = "Player can change the first, Additionally, players can also use minorimprovement card";
    private boolean revealed = true;
    private boolean occupied = false;
    @Override
    public void execute(Player player) {
       becomeFirstPlayer(player);
       useMinorImprovementCard(player);

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
    public void becomeFirstPlayer(Player player) {
        ActionRoundCard.super.becomeFirstPlayer(player);
    }

    @Override
    public void useMinorImprovementCard(Player player) {
        ActionRoundCard.super.useMinorImprovementCard(player);
    }
}
