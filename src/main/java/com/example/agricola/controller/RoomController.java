//package com.example.agricola.controller;
//
//import com.example.agricola.models.Player;
//import com.example.agricola.service.GameService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.stereotype.Controller;
//
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Controller
//public class RoomController {
//
//    @Autowired
//    private GameService gameService;
//
//    @MessageMapping("/startGame")
//    @SendTo("/topic/game")
//    public Map<String, Object> handleGameStart(Map<String, Object> payload) {
//        String roomNumber = (String) payload.get("roomNumber");
//        List<Map<String, Object>> playersData = (List<Map<String, Object>>) payload.get("players");
//
//        List<Player> players = playersData.stream()
//                .map(data -> new Player((String) data.get("id"), (String) data.get("name"), gameService))
//                .collect(Collectors.toList());
//
//        // 게임 시작
//        gameService.startGame(roomNumber, players);
//
//        // 초기화된 게임 상태 반환
//        return Map.of(
//                "status", "Game started",
//                "gameState", gameService.getGameState()
//        );
//    }
//
//    @MessageMapping("/endGame")
//    @SendTo("/topic/game")
//    public void handleGameEnd() {
//        gameService.endGame();
//    }
//}

package com.example.agricola.controller;

import com.example.agricola.models.Player;
import com.example.agricola.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @MessageMapping("/startGame")
    @SendTo("/topic/game")
    public Map<String, Object> handleGameStart(Map<String, Object> payload) {
        System.out.println("Received startGame message");
        String roomNumber = (String) payload.get("roomNumber");
        List<Map<String, Object>> playersData = (List<Map<String, Object>>) payload.get("players");

        List<Player> players = playersData.stream()
                .map(data -> new Player((String) data.get("id"), (String) data.get("name"), gameService))
                .collect(Collectors.toList());

        gameService.startGame(roomNumber, players);

        System.out.println("Game started with players: " + players);

        return gameService.getGameState();
    }

    @MessageMapping("/endGame")
    @SendTo("/topic/game")
    public void handleGameEnd(Map<String, Object> payload) {
        String roomNumber = (String) payload.get("roomNumber");
        System.out.println("Game ended with ID: " + roomNumber);
        gameService.endGame();
    }

}
