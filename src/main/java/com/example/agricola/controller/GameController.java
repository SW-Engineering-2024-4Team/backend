package com.example.agricola.controller;

import com.example.agricola.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class GameController {

    @Autowired
    private GameService gameService;

    @MessageMapping("/gamePlay")
    @SendTo("/topic/game")
    public void playGame(String gameID) {
        gameService.playGame(gameID);
    }

    @MessageMapping("/receivePlayerTurn")
    public void receivePlayerTurn(Map<String, Object> payload) {
        String playerId = (String) payload.get("playerId");
        String cardName = (String) payload.get("cardName");
        int familyMemberX = (int) payload.get("familyMemberX");
        int familyMemberY = (int) payload.get("familyMemberY");

        gameService.receivePlayerTurn(playerId, cardName, familyMemberX, familyMemberY);
    }

    @MessageMapping("/exchangeResources")
    public void exchangeResources(Map<String, Object> payload) {
        String playerId = (String) payload.get("playerId");
        String cardName = (String) payload.get("cardName");
        String fromResource = (String) payload.get("fromResource");
        String toResource = (String) payload.get("toResource");
        int amount = (int) payload.get("amount");

        gameService.handleExchangeRequest(playerId, cardName, fromResource, toResource, amount);
    }

    @MessageMapping("/placeAnimal")
    public void placeAnimal(Map<String, Object> payload) {
        String playerId = (String) payload.get("playerId");
        String animalType = (String) payload.get("animalType");
        int x = (int) payload.get("x");
        int y = (int) payload.get("y");

        gameService.receiveAnimalPlacement(playerId, animalType, x, y);
    }

    @MessageMapping("/playerReadyForNextPhase")
    public void playerReadyForNextPhase(Map<String, String> payload) {
        String playerId = payload.get("playerId");
        gameService.playerReadyForNextPhase(playerId);
    }

}
