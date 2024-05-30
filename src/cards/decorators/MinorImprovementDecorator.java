package cards.decorators;

import cards.common.AccumulativeCard;
import cards.decorators.UnifiedDecorator;
import cards.minorimprovement.MinorImprovementCard;
import models.Player;

public class MinorImprovementDecorator extends UnifiedDecorator {
    public MinorImprovementDecorator(AccumulativeCard decoratedCard) {
        super(decoratedCard);
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
    public void applyAdditionalEffects(Player player) {
        System.out.println("Applying additional effects for MinorImprovementDecorator");
        // 특정 효과 적용 로직을 여기에 구현합니다.
    }
}
