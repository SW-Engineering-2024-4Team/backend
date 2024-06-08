//package com.example.agricola.controller;
//
//import com.example.agricola.enums.ExchangeTiming;
//import com.example.agricola.message.GameActionBoardMessage;
//import com.example.agricola.service.GameService;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.DestinationVariable;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Controller;
//
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Controller
//public class GameController {
//
//    @Autowired
//    private SimpMessagingTemplate simpMessagingTemplate;
//    @Autowired
//    private GameService gameService;
//
//    @MessageMapping("/room/{roomId}/actionCardClick")
//    @SendTo("/topic/room/{roomId}")
//    public GameActionBoardMessage handleAction(String message, @DestinationVariable String roomId) throws JsonProcessingException {
//        int[] clickedActionCards = {0, 0, 0, 0, 0, 2, 3, 4, 0, 0, 0, 0, 0, 0};
//        int[] resourceActionCards = {1,2,3,4,5,6,7,8,9,10,11,12,13,14};
//
//        System.out.println("Action performed in room " + roomId);
//        System.out.println(message);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(message);
//        int playerId = jsonNode.get("currentPlayer").asInt();
//        int cardId = jsonNode.get("cardNumber").asInt();
//
//        System.out.println("Player ID: " + playerId);
//        System.out.println("Card ID: " + cardId);
//
//        gameService.receivePlayerTurn(String.valueOf(playerId), cardId);
//        return new GameActionBoardMessage("Action performed in room " + roomId + "!", clickedActionCards, resourceActionCards);
//    }
//
//    @MessageMapping("/room/{roomId}/viewExchangeableCards")
//    public void viewExchangeableCards(Map<String, Object> payload) {
//        String playerId = (String) payload.get("playerId");
//        gameService.sendExchangeableCardsInfoToFrontEnd(playerId, ExchangeTiming.ANYTIME);
//    }
//
//    @MessageMapping("/room/{roomId}/exchangeResources")
//    public void exchangeResources(Map<String, Object> payload) {
//        String playerId = (String) payload.get("playerId");
//        int cardId = (int) payload.get("cardId");
//        gameService.handleExchangeRequest(playerId, cardId);
//    }
//
//    @MessageMapping("/room/{roomId}/placeAnimal")
//    public void placeAnimal(Map<String, Object> payload) {
//        String playerId = (String) payload.get("playerId");
//        String animalType = (String) payload.get("animalType");
//        int x = (int) payload.get("x");
//        int y = (int) payload.get("y");
//        gameService.receiveAnimalPlacement(playerId, animalType, x, y);
//    }
//
//    @MessageMapping("/room/{roomId}/playerReadyForNextPhase")
//    public void playerReadyForNextPhase(Map<String, String> payload) {
//        String playerId = payload.get("playerId");
//        gameService.playerReadyForNextPhase(playerId);
//    }
//
//    @MessageMapping("/room/{roomId}/chooseOccupationCard")
//    @SendTo("/topic/occupationCardOptions")
//    public List<Map<String, Object>> chooseOccupationCard(Map<String, String> payload) {
//        String playerId = payload.get("playerId");
//        return gameService.getOccupationCards(playerId);
//    }
//
//    @MessageMapping("/room/{roomId}/chooseMinorImprovementCard")
//    @SendTo("/topic/minorImprovementCardOptions")
//    public List<Map<String, Object>> chooseMinorImprovementCard(Map<String, String> payload) {
//        String playerId = payload.get("playerId");
//        return gameService.getMinorImprovementCards(playerId);
//    }
//
//    @MessageMapping("/room/{roomId}/purchaseMajorImprovementCard")
//    @SendTo("/topic/majorImprovementCardOptions")
//    public List<Map<String, Object>> purchaseMajorImprovementCard(Map<String, String> payload) {
//        String playerId = payload.get("playerId");
//        return gameService.getAvailableMajorImprovementCards();
//    }
//
//    @MessageMapping("/room/{roomId}/selectedCard")
//    public void selectedCard(Map<String, Object> payload) {
//        String playerId = (String) payload.get("playerId");
//        int cardId = (int) payload.get("cardId");
//        gameService.receiveSelectedCard(playerId, cardId);
//    }
//
//    @MessageMapping("/room/{roomId}/receiveSelectedPosition")
//    public void receiveSelectedPosition(Map<String, Object> payload) {
//        String playerId = (String) payload.get("playerId");
//        Integer x = null;
//        Integer y = null;
//
//        try {
//            x = (Integer) payload.get("x");
//            y = (Integer) payload.get("y");
//        } catch (ClassCastException e) {
//            System.err.println("Received coordinates are not in the correct format from player: " + playerId);
//            return;
//        }
//
//        if (x == null || y == null) {
//            System.err.println("Received null coordinates from player: " + playerId);
//            return;
//        }
//
//        gameService.receiveSelectedPosition(playerId, x, y);
//    }
//
//    @MessageMapping("/room/{roomId}/receiveSelectedFencePositions")
//    public void receiveSelectedFencePositions(Map<String, Object> payload) {
//        String playerId = (String) payload.get("playerId");
//        List<Map<String, Integer>> positions = (List<Map<String, Integer>>) payload.get("positions");
//        List<int[]> fencePositions = positions.stream()
//                .map(pos -> new int[]{pos.get("x"), pos.get("y")})
//                .collect(Collectors.toList());
//
//        gameService.receiveSelectedFencePositions(playerId, fencePositions);
//    }
//
//    @MessageMapping("/room/{roomId}/playerChoice")
//    public void receivePlayerChoice(Map<String, Object> payload) {
//        String playerId = (String) payload.get("playerId");
//        String choiceType = (String) payload.get("choiceType");
//        Object choice = payload.get("choice");
//        gameService.receivePlayerChoice(playerId, choiceType, choice);
//    }
//
//    @MessageMapping("/room/{roomId}/chooseResource")
//    public void receiveChosenResource(Map<String, Object> payload) {
//        String playerId = (String) payload.get("playerId");
//        String chosenResource = (String) payload.get("chosenResource");
//        gameService.receiveChosenResource(playerId, chosenResource);
//    }
//
//    @MessageMapping("/room/{roomId}/getExchangeableCards")
//    public void getExchangeableCards(Map<String, String> payload) {
//        String playerId = payload.get("playerId");
//        ExchangeTiming timing = ExchangeTiming.valueOf(payload.get("timing"));
//        gameService.sendExchangeableCardsInfoToFrontEnd(playerId, timing);
//    }
//}

package com.example.agricola.controller;

import com.example.agricola.enums.ExchangeTiming;
import com.example.agricola.message.GameActionBoardMessage;
import com.example.agricola.service.GameService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class GameController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private GameService gameService;

    @MessageMapping("/room/{roomId}/actionCardClick")
    @SendTo("/topic/room/{roomId}")
    public GameActionBoardMessage handleAction(String message, @DestinationVariable String roomId) throws JsonProcessingException {
        int[] clickedActionCards = {0, 0, 0, 0, 0, 2, 3, 4, 0, 0, 0, 0, 0, 0};
        int[] resourceActionCards = {1,2,3,4,5,6,7,8,9,10,11,12,13,14};

        System.out.println("Action performed in room " + roomId);
        System.out.println(message);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(message);
        int playerId = jsonNode.get("currentPlayer").asInt();
        int cardId = jsonNode.get("cardNumber").asInt();

        System.out.println("Player ID: " + playerId);
        System.out.println("Card ID: " + cardId);

        gameService.receivePlayerTurn(String.valueOf(playerId), cardId);
        return new GameActionBoardMessage("Action performed in room " + roomId + "!", clickedActionCards, resourceActionCards);
    }

    @MessageMapping("/room/{roomId}/viewExchangeableCards")
    public void viewExchangeableCards(Map<String, Object> payload) {
        String playerId = (String) payload.get("playerId");
        gameService.sendExchangeableCardsInfoToFrontEnd(playerId, ExchangeTiming.ANYTIME);
    }

    @MessageMapping("/room/{roomId}/exchangeResources")
    public void exchangeResources(Map<String, Object> payload) {
        String playerId = (String) payload.get("playerId");
        int cardId = (int) payload.get("cardId");
        gameService.handleExchangeRequest(playerId, cardId);
    }

    @MessageMapping("/room/{roomId}/placeAnimal")
    public void placeAnimal(Map<String, Object> payload) {
        String playerId = (String) payload.get("playerId");
        String animalType = (String) payload.get("animalType");
        int x = (int) payload.get("x");
        int y = (int) payload.get("y");
        gameService.receiveAnimalPlacement(playerId, animalType, x, y);
    }

    @MessageMapping("/room/{roomId}/playerReadyForNextPhase")
    public void playerReadyForNextPhase(Map<String, String> payload) {
        String playerId = payload.get("playerId");
        gameService.playerReadyForNextPhase(playerId);
    }

    @MessageMapping("/room/{roomId}/chooseOccupationCard")
    @SendTo("/topic/occupationCardOptions")
    public List<Map<String, Object>> chooseOccupationCard(Map<String, String> payload) {
        String playerId = payload.get("playerId");
        return gameService.getOccupationCards(playerId);
    }

    @MessageMapping("/room/{roomId}/chooseMinorImprovementCard")
    @SendTo("/topic/minorImprovementCardOptions")
    public List<Map<String, Object>> chooseMinorImprovementCard(Map<String, String> payload) {
        String playerId = payload.get("playerId");
        return gameService.getMinorImprovementCards(playerId);
    }

    @MessageMapping("/room/{roomId}/purchaseMajorImprovementCard")
    @SendTo("/topic/majorImprovementCardOptions")
    public List<Map<String, Object>> purchaseMajorImprovementCard(Map<String, String> payload) {
        String playerId = payload.get("playerId");
        return gameService.getAvailableMajorImprovementCards();
    }

//    @MessageMapping("/room/{roomId}/selectedCard")
//    public void selectedCard(String message, @DestinationVariable String roomId) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        Map<String, Object> payload = objectMapper.readValue(message, Map.class);
//
//        String playerId = (String) payload.get("playerId");
//        int cardId = (int) payload.get("cardId");
//        gameService.receiveSelectedCard(playerId, cardId);
//    }
//@MessageMapping("/room/{roomId}/selectedCard")
//public void selectedCard(String message, @DestinationVariable String roomId) throws JsonProcessingException {
//    ObjectMapper objectMapper = new ObjectMapper();
//    Map<String, Object> payload = objectMapper.readValue(message, Map.class);
//
//    String playerId = (String) payload.get("playerId");
//    int cardId = (int) payload.get("cardId");
//
//    // Add logging for debugging
//    System.out.println("Received card selection message for room: " + roomId + ", Player ID: " + playerId + ", Card ID: " + cardId);
//
//    gameService.receiveSelectedCard(playerId, cardId);
//}
@MessageMapping("/room/{roomId}/selectedCard")
public void selectedCard(String message, @DestinationVariable String roomId) throws JsonProcessingException {
    try {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> payload = objectMapper.readValue(message, Map.class);

        String playerId = (String) payload.get("playerId");
        int cardId = (int) payload.get("cardId");

        // 디버깅을 위한 추가 로그
        System.out.println("Received card selection message for room: " + roomId + ", Player ID: " + playerId + ", Card ID: " + cardId);

//        gameService.receiveSelectedCard(playerId, cardId);
        gameService.receivePlayerTurn(playerId, cardId);
    } catch (JsonProcessingException e) {
        System.err.println("Error parsing JSON message: " + e.getMessage());
    }
}



    @MessageMapping("/room/1/receiveSelectedPosition")
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
            return;
        }

        gameService.receiveSelectedPosition(playerId, x, y);
    }

    @MessageMapping("/room/{roomId}/receiveSelectedFencePositions")
    public void receiveSelectedFencePositions(Map<String, Object> payload) {
        String playerId = (String) payload.get("playerId");
        List<Map<String, Integer>> positions = (List<Map<String, Integer>>) payload.get("positions");
        List<int[]> fencePositions = positions.stream()
                .map(pos -> new int[]{pos.get("x"), pos.get("y")})
                .collect(Collectors.toList());

        gameService.receiveSelectedFencePositions(playerId, fencePositions);
    }

    @MessageMapping("/room/{roomId}/playerChoice")
    public void receivePlayerChoice(Map<String, Object> payload) {
        String playerId = (String) payload.get("playerId");
        String choiceType = (String) payload.get("choiceType");
        Object choice = payload.get("choice");
        gameService.receivePlayerChoice(playerId, choiceType, choice);
    }

    @MessageMapping("/room/{roomId}/chooseResource")
    public void receiveChosenResource(Map<String, Object> payload) {
        String playerId = (String) payload.get("playerId");
        String chosenResource = (String) payload.get("chosenResource");
        gameService.receiveChosenResource(playerId, chosenResource);
    }

    @MessageMapping("/room/{roomId}/getExchangeableCards")
    public void getExchangeableCards(Map<String, String> payload) {
        String playerId = payload.get("playerId");
        ExchangeTiming timing = ExchangeTiming.valueOf(payload.get("timing"));
        gameService.sendExchangeableCardsInfoToFrontEnd(playerId, timing);
    }
}
