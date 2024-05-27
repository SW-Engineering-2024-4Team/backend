package test.actioncardtest;

import cards.action.DirtMiningSite;
import controllers.GameController;
import controllers.RoomController;
import models.Player;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DirtMiningSiteTest {

    @Test
    void execute() {
        RoomController rm = new RoomController();
        List<Player> players = List.of();
        GameController gc = new GameController("1234", rm, players);
        //GameController gc = mock(GameController.class);
        Player player = new Player("1", "Yunjae", gc);
        DirtMiningSite dms = new DirtMiningSite();


        dms.execute(player);
        dms.execute(player);
        dms.setOccupied(true);
        dms.execute(player);
        assertEquals(3,player.getResource("clay"));
        dms.setOccupied(false);
        dms.execute(player);
    }
}