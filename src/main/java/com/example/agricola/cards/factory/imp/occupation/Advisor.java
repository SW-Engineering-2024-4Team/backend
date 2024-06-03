package com.example.agricola.cards.factory.imp.occupation;


import com.example.agricola.cards.occupation.OccupationCard;
import com.example.agricola.enums.ExchangeTiming;
import com.example.agricola.models.Animal;
import com.example.agricola.models.Player;
import com.example.agricola.service.GameService;

import java.util.HashMap;
import java.util.Map;

public class Advisor extends OccupationCard {
    public Advisor(int id) {
        super(id, "상담가", "게임 인원이 1/2/3/4명일 때, 이 카드를 내려놓으면 즉시 곡식2/흙3/돌2/양2마리를 가져옵니다..", null, null, 1, 4, ExchangeTiming.NONE);
    }

    @Override
    public void execute(Player player) {
        int playerCount = player.getGameService().getPlayers().size();
        if (canPlayWithPlayerCount(playerCount)) {
            Map<String, Integer> resources = getResourcesBasedOnPlayerCount(playerCount);
            gainResources(player, resources);
        } else {
            System.out.println("The player count does not meet the requirements to play this card.");
        }
    }

    private Map<String, Integer> getResourcesBasedOnPlayerCount(int playerCount) {
        Map<String, Integer> resources = new HashMap<>();
        switch (playerCount) {
            case 1:
                resources.put("grain", 2);
                break;
            case 2:
                resources.put("clay", 3);
                break;
            case 3:
                resources.put("stone", 2);
                break;
            case 4:
                resources.put("sheep", 2);
                break;
            default:
                break;
        }
        return resources;
    }

    @Override
    // TODO 양배치
    public void gainResources(Player player, Map<String, Integer> resources) {
        for (Map.Entry<String, Integer> entry : resources.entrySet()) {
            String resource = entry.getKey();
            int amount = entry.getValue();

            if (resource.equals("sheep")) {
                System.out.println("양 자원 시작");
                for (int i = 0; i < amount; i++) {
                    Animal newSheep = new Animal(-1, -1, resource);
                    player.addNewAnimal(newSheep);
                }
                int placedSheep = player.placeNewAnimals();
                System.out.println("placedSheep = " + placedSheep);
                player.addResource(resource, placedSheep);
            } else {
                player.addResource(resource, amount);
            }
        }
    }
}
