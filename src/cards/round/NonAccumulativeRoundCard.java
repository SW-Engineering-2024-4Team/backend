package cards.round;

import cards.common.ActionRoundCard;
import models.Player;

public class NonAccumulativeRoundCard implements ActionRoundCard {
    private int id;
    private String name;
    private String description;
    private int cycle;
    private boolean revealed;

    public NonAccumulativeRoundCard(int id, String name, String description, int cycle) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cycle = cycle;
        this.revealed = false;
    }

    @Override
    public void execute(Player player) {
        // 라운드 카드 실행 로직
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
        return false; // 자원 누적 불가능
    }
}
