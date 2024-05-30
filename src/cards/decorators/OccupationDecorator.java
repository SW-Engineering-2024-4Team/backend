package cards.decorators;

import cards.common.AccumulativeCard;
import cards.decorators.UnifiedDecorator;
import models.Player;

import java.util.Map;

public abstract class OccupationDecorator extends UnifiedDecorator {
    public OccupationDecorator(AccumulativeCard decoratedCard) {
        super(decoratedCard);
    }

    @Override
    public void applyAdditionalEffects(Player player) {
        // 직업 카드의 추가적인 효과를 구현합니다.
        // 예를 들어, 직업 카드를 플레이어에게 적용하는 동안 발생하는 특수한 효과를 구현할 수 있습니다.
    }
}
