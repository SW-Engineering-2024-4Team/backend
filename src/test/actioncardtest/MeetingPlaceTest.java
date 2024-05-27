package test.actioncardtest;

import cards.action.ExtendFarm;
import cards.action.MeetingPlace;
import controllers.GameController;
import controllers.RoomController;
import models.Player;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MeetingPlaceTest {

    @Test
    void execute() {
        RoomController rm = new RoomController();
        List<Player> players = new java.util.ArrayList<>(List.of());
        GameController gc = new GameController("1234", rm, players);
        ///GameController gc = mock(GameController.class);
        Player player = new Player("1", "Yunjae", gc);
        Player player2 = new Player("2", "Yunjae2", gc);
        Player player3 = new Player("3", "Yunjae3", gc);
        Player player4 = new Player("4", "Yunjae4", gc);
        gc.setTurnOrder(player);
        gc.setTurnOrder(player2);
        gc.setTurnOrder(player3);
        gc.setTurnOrder(player4);
        List<Player> turnOrder = gc.getTurnOrder();
        MeetingPlace mp = new MeetingPlace();

        //테스트 구체화 필요
        mp.execute(player);
    }


}