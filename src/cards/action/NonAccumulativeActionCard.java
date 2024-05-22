package cards.action;

import cards.common.ActionRoundCard;
import models.Player;

public class NonAccumulativeActionCard implements ActionRoundCard {
    private int id;
    private String name;
    private String description;
    private boolean revealed;

    public NonAccumulativeActionCard(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.revealed = false;
    }

    @Override
    public void execute(Player player) {
        // 액션 카드 실행 로직
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
}
