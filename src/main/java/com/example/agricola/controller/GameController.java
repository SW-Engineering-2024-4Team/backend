package com.example.agricola.controller;

import com.example.agricola.enums.ExchangeTiming;
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
        int cardId = (int) payload.get("cardId");

        gameService.receivePlayerTurn(playerId, cardId);
    }

    @MessageMapping("/viewExchangeableCards")
    public void viewExchangeableCards(Map<String, Object> payload) {
        String playerId = (String) payload.get("playerId");
        gameService.sendExchangeableCardsInfoToFrontEnd(playerId, ExchangeTiming.ANYTIME);
    }


    //    @MessageMapping("/exchangeResources")
//    public void exchangeResources(Map<String, Object> payload) {
//        String playerId = (String) payload.get("playerId");
//        String cardName = (String) payload.get("cardName");
//        String fromResource = (String) payload.get("fromResource");
//        String toResource = (String) payload.get("toResource");
//        int amount = (int) payload.get("amount");
//
//        gameService.handleExchangeRequest(playerId, cardName, fromResource, toResource, amount);
//    }
@MessageMapping("/exchangeResources")
public void exchangeResources(Map<String, Object> payload) {
    String playerId = (String) payload.get("playerId");
    int cardId = (int) payload.get("cardId");

    gameService.handleExchangeRequest(playerId, cardId);
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
        Integer x = null;
        Integer y = null;

        try {
            x = (Integer) payload.get("x");
            y = (Integer) payload.get("y");
        } catch (ClassCastException e) {
            System.err.println("Received coordinates are not in the correct format from player: " + playerId);
            return;
        }

        if (x == null || y == null) {
            System.err.println("Received null coordinates from player: " + playerId);
            return; // 또는 에러 핸들링 로직 추가
        }

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

    @MessageMapping("/playerChoice")
    public void receivePlayerChoice(Map<String, Object> payload) {
        String playerId = (String) payload.get("playerId");
        String choiceType = (String) payload.get("choiceType");
        Object choice = payload.get("choice"); // choice를 Object로 받아서 동적으로 처리
        gameService.receivePlayerChoice(playerId, choiceType, choice);
    }


    @MessageMapping("/chooseResource")
    public void receiveChosenResource(Map<String, Object> payload) {
        String playerId = (String) payload.get("playerId");
        String chosenResource = (String) payload.get("chosenResource");
        gameService.receiveChosenResource(playerId, chosenResource);
    }

    @MessageMapping("/getExchangeableCards")
    public void getExchangeableCards(Map<String, String> payload) {
        String playerId = payload.get("playerId");
        ExchangeTiming timing = ExchangeTiming.valueOf(payload.get("timing"));
        gameService.sendExchangeableCardsInfoToFrontEnd(playerId, timing);
    }





}
