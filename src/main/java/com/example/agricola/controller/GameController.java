package com.example.agricola.controller;

import com.example.agricola.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
//        String cardName = (String) payload.get("cardName");
        int cardId = (int) payload.get("cardId");
//        int familyMemberX = (int) payload.get("familyMemberX");
//        int familyMemberY = (int) payload.get("familyMemberY");

        gameService.receivePlayerTurn(playerId, cardId);
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

    @MessageMapping("/chooseOccupationCard")
    @SendTo("/topic/occupationCardOptions")
    public List<Map<String, Object>> chooseOccupationCard(Map<String, String> payload) {
        String playerId = payload.get("playerId");
        return gameService.getOccupationCards(playerId);
    }

    @MessageMapping("/chooseMinorImprovementCard")
    @SendTo("/topic/minorImprovementCardOptions")
    public List<Map<String, Object>> chooseMinorImprovementCard(Map<String, String> payload) {
        String playerId = payload.get("playerId");
        return gameService.getMinorImprovementCards(playerId);
    }

    @MessageMapping("/purchaseMajorImprovementCard")
    @SendTo("/topic/majorImprovementCardOptions")
    public List<Map<String, Object>> purchaseMajorImprovementCard(Map<String, String> payload) {
        String playerId = payload.get("playerId");
        return gameService.getAvailableMajorImprovementCards();
    }

    @MessageMapping("/selectedCard")
    public void selectedCard(Map<String, Object> payload) {
        String playerId = (String) payload.get("playerId");
        int cardId = (int) payload.get("cardId");
        gameService.receiveSelectedCard(playerId, cardId);
    }


    @MessageMapping("/receiveSelectedPosition")
    public void receiveSelectedPosition(Map<String, Object> payload) {
        String playerId = (String) payload.get("playerId");
        int x = (int) payload.get("x");
        int y = (int) payload.get("y");

        gameService.receiveSelectedPosition(playerId, x, y);
    }

    @MessageMapping("/receiveSelectedFencePositions")
    public void receiveSelectedFencePositions(Map<String, Object> payload) {
        String playerId = (String) payload.get("playerId");
        List<Map<String, Integer>> positions = (List<Map<String, Integer>>) payload.get("positions");
        List<int[]> fencePositions = positions.stream()
                .map(pos -> new int[]{pos.get("x"), pos.get("y")})
                .collect(Collectors.toList());

        gameService.receiveSelectedFencePositions(playerId, fencePositions);
    }



}
