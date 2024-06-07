package com.example.agricola.controller;

import com.example.agricola.dto.PlayerDTO;
import com.example.agricola.models.Player;
import com.example.agricola.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class RoomController {

    @Autowired
    private GameService gameService;

    @Autowired
    private ObjectMapper objectMapper;

    @MessageMapping("/room/{roomId}/start")
    @SendTo("/topic/game")
    public Map<String, Object> handleGameStart(@DestinationVariable String roomId, String payload) {
        try {
            // Deserialize the payload into a Map to extract the players array
            Map<String, Object> payloadMap = objectMapper.readValue(payload, Map.class);
            List<PlayerDTO> playersData = objectMapper.convertValue(payloadMap.get("players"),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, PlayerDTO.class));
            System.out.println("Received startGame message with playersData: " + playersData);

            List<Player> players = playersData.stream()
                    .map(playerDTO -> new Player(playerDTO.getId(), playerDTO.getName(), gameService))
                    .collect(Collectors.toList());

            gameService.startGame(roomId, players);

            System.out.println("Game started with players: " + players);

            gameService.playGame(roomId);

            return gameService.getGameState();
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("error", "Failed to start game");
        }
    }

    @MessageMapping("/endGame")
    @SendTo("/topic/game")
    public void handleGameEnd(Map<String, Object> payload) {
        String roomNumber = (String) payload.get("roomNumber");
        System.out.println("Game ended with ID: " + roomNumber);
        gameService.endGame();
    }
}
