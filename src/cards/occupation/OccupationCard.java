package cards.occupation;

import cards.common.ExchangeableCard;
import cards.common.UnifiedCard;
import enums.ExchangeTiming;
import models.Player;

import java.util.Map;

public abstract class OccupationCard implements UnifiedCard, ExchangeableCard {
    private int id;
    private String name;
    private String description;
    private Map<String, Integer> exchangeRate;
    private Map<String, Integer> gainResources;
    private int minPlayer; // 최소 플레이어 수
    private int maxPlayer; // 최대 플레이어 수

    public OccupationCard(int id, String name, String description, Map<String, Integer> exchangeRate, Map<String, Integer> gainResources, int minPlayer, int maxPlayer) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.exchangeRate = exchangeRate;
        this.gainResources = gainResources;
        this.minPlayer = minPlayer;
        this.maxPlayer = maxPlayer;
    }

    @Override
    public void execute(Player player) {
        // 직업 카드 실행 로직
        applyEffect(player);
        gainResource(player);
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

    // 최소 플레이어 수 반환
    public int getMinPlayer() {
        return minPlayer;
    }

    // 최대 플레이어 수 반환
    public int getMaxPlayer() {
        return maxPlayer;
    }

    // 현재 플레이어 수가 이 카드를 사용할 수 있는지 여부 확인
    public boolean canPlayWithPlayerCount(int playerCount) {
        return playerCount >= minPlayer && playerCount <= maxPlayer;
    }
}
