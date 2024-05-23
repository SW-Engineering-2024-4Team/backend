package com.example.agricola.controller;

import com.example.agricola.domain.Player;
import org.springframework.messaging.handler.annotation.DestinationVariable; // 해당 매개변수가 websocket 메시지의 목적지에 대한 변수임을 나타냄
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class GameController {
    private List<Player> players;
    /* Back이 Server, Front가 Cleint */


    // Client는 /room/sendGameStartMessage로 메세지를 보냄
    @MessageMapping("/room/sendGameStartMessage")
    //메세지 안에 정보가 들어올때 메세지로 부른 메서드에서도 정보를 처리 가능할까?

    // Client는 session.subscribe("/topic/receiveMessage") 이런식으로 구독을함. Server는 SendTo가써져있는 메서드의
    //모든 동작들의 결과(리턴값 포함)을 SendTo에 써져있는 구독으로 보내는거임.
    @SendTo("topic/gameStart")
    public void gameStart(@DestinationVariable String roomId, SimpMessageHeaderAccessor headerAccessor) {
        //플레이어 목록 초기화
        String sessionId = headerAccessor.getSessionId();
        GameController gc = new GameController(); // Player객체 만들때
        Player player = Player(sessionId, gc, roomId); //플레이어 객체 생성
        player.initializeResource();


        // Here, add any logic you need to initialize the game state.
        //System.out.println("Game started in room " + roomId);
        //return new GameInitialMessage("Game has started in room " + roomId + "!");
    }
}
