package com.example.agricola.controller;

import com.example.agricola.enums.ExchangeTiming;
import com.example.agricola.message.GameActionBoardMessage;
import com.example.agricola.service.GameService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
        // 0: 사람없음, 1~4: 플레이어 -> 14개 카드
        int[] clickedActionCards = {0, 0, 0, 0, 0, 2, 3, 4, 0, 0, 0, 0, 0, 0};

        // 자원누적이 필요한 카드: 1,2,4,6,11,12,13 번
        int[] resourceActionCards = {1,2,3,4,5,6,7,8,9,10,11,12,13,14};

        System.out.println("Action performed in room " + roomId);
        System.out.println(message);


        // Parse the JSON message
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(message);
        int playerId = jsonNode.get("currentPlayer").asInt();
        int cardId = jsonNode.get("cardNumber").asInt();

        // Log the extracted values
        System.out.println("Player ID: " + playerId);
        System.out.println("Card ID: " + cardId);

        //simpMessagingTemplate.convertAndSend("/topic/room/{roomId}", message);
        gameService.receivePlayerTurn(String.valueOf(playerId), cardId);
        return new GameActionBoardMessage("Action performed in room " + roomId + "!", clickedActionCards, resourceActionCards);
    }



    @MessageMapping("/room/{roomId}/viewExchangeableCards")
    public void viewExchangeableCards(String message) {
        try {
            // Parse the JSON message
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(message);
            String playerId = jsonNode.get("playerId").asText(); // asText() 메서드로 String으로 변환
            gameService.sendExchangeableCardsInfoToFrontEnd(playerId, ExchangeTiming.ANYTIME);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // JSON 파싱에 실패한 경우 처리
        }
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
    @MessageMapping("/room/{roomId}/exchangeResources")
    public void exchangeResources(String message) {
        try {
            // Parse the JSON message
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(message);

            // Extract playerId and cardId from the payload
            String playerId = jsonNode.get("playerId").asText(); // asText() 메서드로 String으로 변환
            int cardId = jsonNode.get("cardId").asInt();

            // Call the service method to handle the exchange request
            gameService.handleExchangeRequest(playerId, cardId);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // JSON 파싱에 실패한 경우 처리
        }
    }


    @MessageMapping("/room/{roomId}/placeAnimal")
    public void placeAnimal(String message) {
        try {
            // Parse the JSON message
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(message);

            // Extract playerId, animalType, x, and y from the payload
            String playerId = jsonNode.get("playerId").asText(); // asText() 메서드로 String으로 변환
            String animalType = jsonNode.get("animalType").asText();
            int x = jsonNode.get("x").asInt();
            int y = jsonNode.get("y").asInt();

            // Call the service method to handle the animal placement
            gameService.receiveAnimalPlacement(playerId, animalType, x, y);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // JSON 파싱에 실패한 경우 처리
        }
    }

    @MessageMapping("/room/{roomId}/playerReadyForNextPhase")
    public void playerReadyForNextPhase(String message) {
        try {
            // Parse the JSON message
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(message);

            // Extract playerId from the payload
            String playerId = jsonNode.get("playerId").asText(); // asText() 메서드로 String으로 변환

            // Call the service method to handle player readiness for the next phase
            gameService.playerReadyForNextPhase(playerId);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // JSON 파싱에 실패한 경우 처리
        }
    }

    @MessageMapping("/room/{roomId}/chooseOccupationCard")
    @SendTo("/topic/occupationCardOptions")
    public List<Map<String, Object>> chooseOccupationCard(String message) {
        try {
            // Parse the JSON message
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(message);

            // Extract playerId from the message
            String playerId = jsonNode.get("playerId").asText(); // asText() 메서드로 String으로 변환

            // Call the service method to get occupation cards for the player
            return gameService.getOccupationCards(playerId);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // JSON 파싱에 실패한 경우 처리
            return Collections.emptyList();
        }
    }

    @MessageMapping("/room/{roomId}/chooseMinorImprovementCard")
    @SendTo("/topic/minorImprovementCardOptions")
    public List<Map<String, Object>> chooseMinorImprovementCard(String message) {
        try {
            // Parse the JSON message
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(message);

            // Extract playerId from the message
            String playerId = jsonNode.get("playerId").asText(); // asText() 메서드로 String으로 변환

            // Call the service method to get minor improvement cards for the player
            return gameService.getMinorImprovementCards(playerId);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // JSON 파싱에 실패한 경우 처리
            return Collections.emptyList();
        }
    }

    @MessageMapping("/room/{roomId}/purchaseMajorImprovementCard")
    @SendTo("/topic/majorImprovementCardOptions")
    public List<Map<String, Object>> purchaseMajorImprovementCard(String message) {
        try {
            // Parse the JSON message
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(message);

            // Extract playerId from the message
            String playerId = jsonNode.get("playerId").asText(); // asText() 메서드로 String으로 변환

            // Call the service method to get available major improvement cards
            return gameService.getAvailableMajorImprovementCards(playerId);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // JSON 파싱에 실패한 경우 처리
            return Collections.emptyList();
        }
    }

    @MessageMapping("/room/{roomId}/selectedCard")
    public void selectedCard(String message) {
        try {
            // Parse the JSON message
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(message);

            // Extract playerId and cardId from the message
            String playerId = jsonNode.get("playerId").asText(); // asText() 메서드로 String으로 변환
            int cardId = jsonNode.get("cardId").asInt();

            // Call the service method to receive the selected card
            gameService.receiveSelectedCard(playerId, cardId);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // JSON 파싱에 실패한 경우 처리
        }
    }


    @MessageMapping("/room/{roomId}/receiveSelectedPosition")
    public void receiveSelectedPosition(String message) {
        try {
            // Parse the JSON message
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(message);

            // Extract playerId from the message
            String playerId = jsonNode.get("playerId").asText(); // asText() 메서드로 String으로 변환

            // Extract x and y coordinates from the message
            Integer x = jsonNode.get("x").asInt();
            Integer y = jsonNode.get("y").asInt();

            // Check if coordinates are null
            if (x == null || y == null) {
                System.err.println("Received null coordinates from player: " + playerId);
                return; // 또는 에러 핸들링 로직 추가
            }

            // Call the service method to receive the selected position
            gameService.receiveSelectedPosition(playerId, x, y);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // JSON 파싱에 실패한 경우 처리
        }
    }



    @MessageMapping("/room/{roomId}/receiveSelectedFencePositions")
    public void receiveSelectedFencePositions(String message) {
        try {
            // Parse the JSON message
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(message);

            // Extract playerId from the message
            String playerId = jsonNode.get("playerId").asText(); // asText() 메서드로 String으로 변환

            // Extract positions from the message
            List<Map<String, Integer>> positions = objectMapper.convertValue(jsonNode.get("positions"), new TypeReference<List<Map<String, Integer>>>() {});

            // Convert positions to fencePositions
            List<int[]> fencePositions = positions.stream()
                    .map(pos -> new int[]{pos.get("x"), pos.get("y")})
                    .collect(Collectors.toList());

            // Call the service method to receive the selected fence positions
            gameService.receiveSelectedFencePositions(playerId, fencePositions);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // JSON 파싱에 실패한 경우 처리
        }
    }

    //옵션 받기
    @MessageMapping("/room/{roomId}/playerChoice")
    public void receivePlayerChoice(String message) {
        try {
            // Parse the JSON message
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(message);

            // Extract playerId, choiceType, and choice from the message
            String playerId = jsonNode.get("playerId").asText(); // asText() 메서드로 String으로 변환
            String choiceType = jsonNode.get("choiceType").asText();
            JsonNode choiceNode = jsonNode.get("choice");
            Object choice = objectMapper.treeToValue(choiceNode, Object.class);

            // Call the service method to receive the player's choice
            gameService.receivePlayerChoice(playerId, choiceType, choice);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // JSON 파싱에 실패한 경우 처리
        }
    }


    @MessageMapping("/room/{roomId}/chooseResource")
    public void receiveChosenResource(String message) {
        try {
            // Parse the JSON message
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(message);

            // Extract playerId and chosenResource from the message
            String playerId = jsonNode.get("playerId").asText(); // asText() 메서드로 String으로 변환
            String chosenResource = jsonNode.get("chosenResource").asText();

            // Call the service method to receive the chosen resource
            gameService.receiveChosenResource(playerId, chosenResource);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // JSON 파싱에 실패한 경우 처리
        }
    }

    @MessageMapping("/room/{roomId}/getExchangeableCards")
    public void getExchangeableCards(String message) {
        try {
            // Parse the JSON message
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(message);

            // Extract playerId and timing from the message
            String playerId = jsonNode.get("playerId").asText(); // asText() 메서드로 String으로 변환
            ExchangeTiming timing = ExchangeTiming.valueOf(jsonNode.get("timing").asText());

            // Call the service method to send exchangeable cards information to the frontend
            gameService.sendExchangeableCardsInfoToFrontEnd(playerId, timing);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // JSON 파싱에 실패한 경우 처리
        }
    }

}
