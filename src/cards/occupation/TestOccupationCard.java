package cards.occupation;

import models.Player;

import java.util.Map;

public class TestOccupationCard extends OccupationCard {
    public TestOccupationCard(int id, String name, String description, Map<String, Integer> exchangeRate, Map<String, Integer> gainResources, int minPlayer, int maxPlayer) {
        super(id, name, description, exchangeRate, gainResources, minPlayer, maxPlayer);
    }

    @Override
    public void applyEffect(Player player) {
        // 테스트용 간단한 효과 예시
        System.out.println("Applying effect for " + getName());
    }
}
