package cards.majorimprovement;

import models.Player;
import java.util.Map;

public class TestMajorImprovementCard extends MajorImprovementCard {
    public TestMajorImprovementCard(int id, String name, String description,
                                    Map<String, Integer> purchaseCost,
                                    Map<String, Integer> anytimeExchangeRate,
                                    Map<String, Integer> breadBakingExchangeRate,
                                    int additionalPoints,
                                    boolean immediateBakingAction) {
        super(id, name, description, purchaseCost, anytimeExchangeRate, breadBakingExchangeRate, additionalPoints, immediateBakingAction);
    }

    @Override
    public void triggerBreadBaking(Player player) {
        int grain = player.getResource("grain");
        int food = grain * 2;
        player.addResource("grain", -grain);
        player.addResource("food", food);
    }
}
