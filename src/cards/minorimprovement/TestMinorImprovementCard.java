package cards.minorimprovement;

import models.Player;

import java.util.Map;
import java.util.function.Predicate;

public class TestMinorImprovementCard extends MinorImprovementCard {
    public TestMinorImprovementCard(int id, String name, String description, Map<String, Integer> exchangeRate, Map<String, Integer> gainResources, Map<String, Integer> cost, Predicate<Player> condition) {
        super(id, name, description, exchangeRate, gainResources, cost, condition);
    }

    @Override
    public void applyEffect(Player player) {
        // 테스트용 간단한 효과 예시
        System.out.println("Applying effect for " + getName());
    }
}
