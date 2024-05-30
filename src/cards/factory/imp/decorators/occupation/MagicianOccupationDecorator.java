package cards.factory.imp.decorators.occupation;

import cards.common.AccumulativeCard;
import cards.decorators.OccupationDecorator;
import models.Player;

public class MagicianOccupationDecorator extends OccupationDecorator {
    public MagicianOccupationDecorator(AccumulativeCard decoratedCard) {
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
        System.out.println("Applying additional effects for MagicianOccupationDecorator");
        // 특정 효과 적용 로직을 여기에 구현합니다.
    }
}
