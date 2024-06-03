package com.example.agricola.cards.occupation;

import com.example.agricola.cards.common.ExchangeableCard;
import com.example.agricola.cards.common.UnifiedCard;
import com.example.agricola.enums.ExchangeTiming;
import com.example.agricola.models.Player;

import java.util.Map;

public abstract class OccupationCard implements UnifiedCard, ExchangeableCard {
    private int id;
    private String name;
    private String description;
    private Map<String, Integer> exchangeRate;
    private Map<String, Integer> gainResources;
    private int minPlayer; // 최소 플레이어 수
    private int maxPlayer; // 최대 플레이어 수
    private ExchangeTiming exchangeTiming;

    public OccupationCard(int id, String name, String description, Map<String, Integer> exchangeRate, Map<String, Integer> gainResources, int minPlayer, int maxPlayer, ExchangeTiming exchangeTiming) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.exchangeRate = exchangeRate;
        this.gainResources = gainResources;
        this.minPlayer = minPlayer;
        this.maxPlayer = maxPlayer;
        this.exchangeTiming = exchangeTiming;
    }

    @Override
    public void execute(Player player) {
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
    public boolean canExchange(ExchangeTiming timing) {
        return exchangeRate != null;
    }

    @Override
    // TODO 양 자원이면 플레이어 보드에서 양을 제거해줘야 함
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

    // 필요한 경우에만 오버라이드하도록 추상 메서드로 선언
    public void gainResources(Player player, Map<String, Integer> resources) {
        throw new UnsupportedOperationException("This occupation card does not support gaining resources.");
    }
}
