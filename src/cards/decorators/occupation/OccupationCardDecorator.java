package cards.decorators.occupation;

import cards.common.AccumulativeCard;
import cards.decorators.UnifiedDecorator;
import models.Player;

public class OccupationCardDecorator extends UnifiedDecorator {

    public OccupationCardDecorator(AccumulativeCard decoratedCard) {
        super(decoratedCard);
    }

    @Override
    public void applyAdditionalEffects(Player player) {
        // 직업 카드에 추가 효과 적용 로직
    }
}
