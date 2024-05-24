package controllers;

import models.Player;

import java.util.List;

public class RoomController {
    private GameController gameController;

    public void handleGameStart(String roomNumber, List<Player> players) {
        gameController = new GameController(roomNumber, this, players);
        gameController.initializeGame();
    }

    public GameController getGameController() {
        return gameController;
    }

    public void handleGameEnd() {
        // 게임 종료 처리 로직
        System.out.println("Game has ended.");
    }
}
