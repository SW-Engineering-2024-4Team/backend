package cards.minorimprovement;

import models.Player;
import cards.common.UnifiedCard;
import cards.common.ExchangeableCard;
import enums.ExchangeTiming;

import java.util.Map;

public class TestMinorImprovementCard implements UnifiedCard, ExchangeableCard {
    private int id;
    private String name;
    private String description;
    private Map<String, Integer> exchangeRate;
    private Map<String, Integer> gainResources;
    private Map<String, Integer> cost;

    public TestMinorImprovementCard(int id, String name, String description, Map<String, Integer> exchangeRate, Map<String, Integer> gainResources, Map<String, Integer> cost) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.exchangeRate = exchangeRate;
        this.gainResources = gainResources;
        this.cost = cost;
    }

    @Override
    public void execute(Player player) {
        if (checkResources(player, cost)) {
            payResources(player, cost);
            applyEffect(player);
            gainResource(player);
        } else {
            System.out.println("Not enough resources to use " + getName());
        }
    }

    @Override
    public void gainResource(Player player) {
        if (gainResources != null) {
            for (Map.Entry<String, Integer> entry : gainResources.entrySet()) {
                player.addResource(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public void applyEffect(Player player) {
        System.out.println("Applying effect for " + getName());
    }

    @Override
    public boolean canExchange(ExchangeTiming timing) {
        return exchangeRate != null;
    }

    @Override
    public void executeExchange(Player player, String fromResource, String toResource, int amount) {
        if (exchangeRate != null) {
            int exchangeAmount = exchangeRate.get(toResource) * amount / exchangeRate.get(fromResource);
            player.addResource(fromResource, -amount);
            player.addResource(toResource, exchangeAmount);
        }
    }

    @Override
    public Map<String, Integer> getExchangeRate() {
        return exchangeRate;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

}
