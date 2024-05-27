package test.actioncardtest;

import cards.action.DayLaborer;
import cards.action.ExtendFarm;
import controllers.GameController;
import controllers.RoomController;
import models.Player;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DayLaborerTest {

    @Test
    void execute() {
        RoomController rm = new RoomController();
        List<Player> players = List.of();
        GameController gc = new GameController("1234", rm, players);
        //GameController gc = mock(GameController.class);
        Player player = new Player("1", "Yunjae", gc);
        DayLaborer dl = new DayLaborer();

        //테스트 완료
        dl.execute(player);
        assertEquals(2, player.getResource("food"));
    }
}