package cards.minorimprovement;

import cards.common.ExchangeableCard;
import cards.common.UnifiedCard;
import enums.ExchangeTiming;
import models.Player;

import java.util.Map;
import java.util.function.Predicate;

public abstract class MinorImprovementCard implements UnifiedCard, ExchangeableCard {
    private int id;
    private String name;
    private String description;
    private Map<String, Integer> exchangeRate;
    private Map<String, Integer> gainResources;
    private Map<String, Integer> cost;
    private Predicate<Player> condition;

    public MinorImprovementCard(int id, String name, String description, Map<String, Integer> exchangeRate, Map<String, Integer> gainResources, Map<String, Integer> cost, Predicate<Player> condition) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.exchangeRate = exchangeRate;
        this.gainResources = gainResources;
        this.cost = cost;
        this.condition = condition;
    }

    @Override
    public void execute(Player player) {
        if (testCondition(player)) {
            if (checkResources(player, cost)) {
                payResources(player, cost);
                applyEffect(player);
                gainResource(player);
            } else {
                // 자원이 부족하다는 메시지 표시
            }
        } else {
            // 조건을 만족하지 않는다는 메시지 표시
        }
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

    @Override
    public void gainResource(Player player) {
        if (gainResources != null) {
            for (Map.Entry<String, Integer> entry : gainResources.entrySet()) {
                player.addResource(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public abstract void applyEffect(Player player);

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

    public boolean testCondition(Player player) {
        return condition == null || condition.test(player);
    }
}
