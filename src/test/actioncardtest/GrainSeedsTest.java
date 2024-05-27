package test.actioncardtest;

import cards.action.ExtendFarm;
import cards.action.GrainSeeds;
import controllers.GameController;
import controllers.RoomController;
import models.Player;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GrainSeedsTest {

    @Test
    void execute() {
        RoomController rm = new RoomController();
        List<Player> players = List.of();
        GameController gc = new GameController("1234", rm, players);
        //GameController gc = mock(GameController.class);
        Player player = new Player("1", "Yunjae", gc);
        GrainSeeds gs = new GrainSeeds();

        //테스트 완료.
        gs.execute(player);
        assertEquals(1, player.getResource("grain"));
    }
}