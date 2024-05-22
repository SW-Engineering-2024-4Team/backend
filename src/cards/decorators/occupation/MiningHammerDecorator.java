package cards.decorators.occupation;

import cards.common.AccumulativeCard;
import cards.decorators.UnifiedDecorator;
import models.Player;
import java.util.Map;

public class MiningHammerDecorator extends UnifiedDecorator {

    public MiningHammerDecorator(AccumulativeCard decoratedCard) {
        super(decoratedCard);
    }

    @Override
    public void applyAdditionalEffects(Player player) {
        // 추가 효과 로직: 흙 자원을 추가로 1개 더 받는 기능
        Map<String, Integer> accumulatedResources = decoratedCard.getAccumulatedResources();
        if (accumulatedResources.containsKey("clay")) {
            int additionalClay = accumulatedResources.get("clay") + 1;
            accumulatedResources.put("clay", additionalClay);
            System.out.println("MiningHammerDecorator: 흙 자원을 추가로 1개 더 받았습니다.");
        }
    }
}
