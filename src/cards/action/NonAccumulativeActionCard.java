package cards.action;

import cards.common.ActionRoundCard;
import models.Player;

import java.util.Objects;

public class NonAccumulativeActionCard implements ActionRoundCard {
    private int id;
    public String name;
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
        System.out.println("executed");
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

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        AccumulativeActionCard that = (AccumulativeActionCard) o;
//        return Objects.equals(name, that.name);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(name);
//    }
}
