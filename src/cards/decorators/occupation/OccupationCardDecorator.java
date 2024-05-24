package cards.decorators.occupation;

import cards.common.AccumulativeCard;
import cards.decorators.UnifiedDecorator;
import models.Player;

public class OccupationCardDecorator extends UnifiedDecorator {

    public OccupationCardDecorator(AccumulativeCard decoratedCard) {
        super(decoratedCard);
    }

    @Override
    public boolean isOccupied() {
        return decoratedCard.isOccupied();
    }

    @Override
    public void setOccupied(boolean occupied) {
        decoratedCard.setOccupied(occupied);
    }

    @Override
    public void applyAdditionalEffects(Player player) {
        // 직업 카드에 추가 효과 적용 로직
    }
}
