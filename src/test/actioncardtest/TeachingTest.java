package test.actioncardtest;

import cards.action.ExtendFarm;
import cards.action.Teaching;
import controllers.GameController;
import controllers.RoomController;
import models.Player;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TeachingTest {

    @Test
    void execute() {
        RoomController rm = new RoomController();
        List<Player> players = List.of();
        GameController gc = new GameController("1234", rm, players);
        //GameController gc = mock(GameController.class);
        Player player = new Player("1", "Yunjae", gc);
        Teaching t = new Teaching();
        player.addResource("food", 3);

        //테스트 구체화 필요
        t.execute(player);
        assertEquals(2, player.getResource("food"));
    }
}