package cards.decorators.minorimprovement;

import cards.common.AccumulativeCard;
import cards.decorators.UnifiedDecorator;
import models.Player;

public class MinorImprovementCardDecorator extends UnifiedDecorator {

    public MinorImprovementCardDecorator(AccumulativeCard decoratedCard) {
        super(decoratedCard);
    }

    @Override
    public void applyAdditionalEffects(Player player) {
        // 보조 설비 카드에 추가 효과 적용 로직
    }
}
