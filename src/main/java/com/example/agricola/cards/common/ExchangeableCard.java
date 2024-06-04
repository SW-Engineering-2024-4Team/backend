package com.example.agricola.cards.common;

import com.example.agricola.enums.ExchangeTiming;
import com.example.agricola.models.Animal;
import com.example.agricola.models.Player;
import com.example.agricola.models.PlayerBoard;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public interface ExchangeableCard {
    boolean canExchange(ExchangeTiming timing);
    LinkedHashMap<String, Integer> getExchangeRate();

    default void executeExchange(Player player) {
        LinkedHashMap<String, Integer> exchangeRate = getExchangeRate();
        if (exchangeRate != null && exchangeRate.size() == 2) {
            String fromResource = (String) exchangeRate.keySet().toArray()[0];
            String toResource = (String) exchangeRate.keySet().toArray()[1];

            int fromResourceRate = exchangeRate.get(fromResource);
            int toResourceRate = exchangeRate.get(toResource);

            int maxExchangeAmount = player.getResource(fromResource) / fromResourceRate;
            int exchangeAmount = maxExchangeAmount;

            if (fromResource.equals("sheep")) {
                removeRandomSheepFromBoard(player, exchangeAmount);
            } else {
                player.addResource(fromResource, -exchangeAmount * fromResourceRate);
            }

            int toResourceAmount = toResourceRate * exchangeAmount;
            player.addResource(toResource, toResourceAmount);
        }
    }

    default void removeRandomSheepFromBoard(Player player, int amount) {
        PlayerBoard board = player.getPlayerBoard();
        Random rand = new Random();
        int sheepRemoved = 0;

        while (sheepRemoved < amount) {
            int x = rand.nextInt(board.getTiles().length);
            int y = rand.nextInt(board.getTiles()[0].length);

            Animal[][] animals = board.getAnimals();
            if (animals[x][y] != null && animals[x][y].getType().equals("sheep")) {
                animals[x][y] = null; // 보드에서 양 제거
                sheepRemoved++;
                notifySheepRemoval(player, x, y); // 프론트엔드에 양 제거 좌표 알림
            }
        }
    }

    default void notifySheepRemoval(Player player, int x, int y) {
        Map<String, Object> message = Map.of(
                "playerId", player.getId(),
                "action", "removeSheep",
                "position", Map.of("x", x, "y", y)
        );
        player.getGameService().notifyPlayer(player, message);
    }
}
