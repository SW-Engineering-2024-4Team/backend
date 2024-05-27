package cards.decorators.minorimprovement;

import cards.common.AccumulativeCard;
import cards.decorators.UnifiedDecorator;
import models.Player;

import java.util.HashMap;
import java.util.Map;

public class MinorImprovementCardDecorator extends UnifiedDecorator {

    public MinorImprovementCardDecorator(AccumulativeCard decoratedCard) {
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
        // 보조 설비 카드에 추가 효과 적용 로직
    }
    //AccumulativeCard에 메서드추가했으므로 여기도 임의추가
    @Override
    public Map<String, Integer> getAccumulatedResourceInfo() {
        return new HashMap<>();
    }
}
