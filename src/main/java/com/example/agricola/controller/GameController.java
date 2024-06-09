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

//    @MessageMapping("/gamePlay")
//    @SendTo("/topic/game")
//    public void playGame(String gameID) {
//        gameService.playGame(gameID);
//    }

    // 1. 플레이어 턴에서 플레이어의 카드 선택을 받음
    @MessageMapping("/receivePlayerTurn")
    public void receivePlayerTurn(Map<String, Object> payload) {
        String playerId = (String) payload.get("playerId");
        int cardId = (int) payload.get("cardId");

        gameService.receivePlayerTurn(playerId, cardId);
    }

    // 플레이어 턴에서 발동중인 카드에서 교환 가능한 카드들의 목록을 요청할 떄
    @MessageMapping("/viewExchangeableCards")
    public void viewExchangeableCards(Map<String, Object> payload) {
        String playerId = (String) payload.get("playerId");
        gameService.sendExchangeableCardsInfoToFrontEnd(playerId, ExchangeTiming.ANYTIME);
    }



    // 카드 목록에서 골랐을 때 해당 카드로 교환 기능을 수행하는 카드
@MessageMapping("/exchangeResources")
public void exchangeResources(Map<String, Object> payload) {
    String playerId = (String) payload.get("playerId");
    int cardId = (int) payload.get("cardId");

    gameService.handleExchangeRequest(playerId, cardId);
}


//    @MessageMapping("/placeAnimal")
//    public void placeAnimal(Map<String, Object> payload) {
//        String playerId = (String) payload.get("playerId");
//        String animalType = (String) payload.get("animalType");
//        int x = (int) payload.get("x");
//        int y = (int) payload.get("y");
//
//        gameService.receiveAnimalPlacement(playerId, animalType, x, y);
//    }

//    @MessageMapping("/playerReadyForNextPhase")
//    public void playerReadyForNextPhase(Map<String, String> payload) {
//        String playerId = payload.get("playerId");
//        gameService.playerReadyForNextPhase(playerId);
//    }


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

    // 액션카드, 라운드카드의 기능 중 카드를 고르는 옵션이 있는 경우
    // 예: 교습, 주요설비, ...
    // 고른 카드를 일로 보내면 됨
    @MessageMapping("/selectedCard")
    public void selectedCard(Map<String, Object> payload) {
        String playerId = (String) payload.get("playerId");
        int cardId = (int) payload.get("cardId");
        gameService.receiveSelectedCard(playerId, cardId);
    }


    // 추가요청1. 좌표 요청
    // 집 짓기, 밭 일구기, 외양간 짓기시 좌표를 한 개 받는 부분
    // app.js기준으로 가능한 위치를 먼저 보내줌 그 중에서 선택해야 함
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



    // 추가요청2. 펜스 좌표 요청
    // 프론트가 백한테 울타리를 지을 좌표를 보내는 곳
    // 좌표 하나만 보낼꺼면 위에 selectedposition으로 대체가능, 다중 좌표시 일로
    @MessageMapping("/receiveSelectedFencePositions")
    public void receiveSelectedFencePositions(Map<String, Object> payload) {
        String playerId = (String) payload.get("playerId");
        List<Map<String, Integer>> positions = (List<Map<String, Integer>>) payload.get("positions");
        List<int[]> fencePositions = positions.stream()
                .map(pos -> new int[]{pos.get("x"), pos.get("y")})
                .collect(Collectors.toList());

        gameService.receiveSelectedFencePositions(playerId, fencePositions);
    }

    // 추가요청3. 플레이어 옵션 선택
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
