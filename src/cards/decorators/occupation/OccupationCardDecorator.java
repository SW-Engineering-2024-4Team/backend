package cards.decorators.occupation;

import cards.common.AccumulativeCard;
import cards.decorators.UnifiedDecorator;
import models.Player;

import java.util.HashMap;
import java.util.Map;

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

    //임의 추가 메서드 AccumulativeCard implement수정에따라 메서드 추가.
    @Override
    public Map<String, Integer> getAccumulatedResourceInfo() {
        return new HashMap<>();
    }
}
