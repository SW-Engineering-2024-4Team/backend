package test.actioncardtest;

import cards.action.DirtMiningSite;
import cards.action.Fishing;
import controllers.GameController;
import controllers.RoomController;
import models.Player;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FishingTest {

    @Test
    void execute() {
        RoomController rm = new RoomController();
        List<Player> players = List.of();
        GameController gc = new GameController("1234", rm, players);
        //GameController gc = mock(GameController.class);
        Player player = new Player("1", "Yunjae", gc);
        Fishing fs = new Fishing();

        //라운드 증가를 판단하고 자원이 카드에 축적되는 테스트 추가적으로 필요
        fs.execute(player);
        fs.execute(player);
        fs.setOccupied(true);
        fs.execute(player);
        assertEquals(3,player.getResource("food"));
        fs.setOccupied(false);
        fs.execute(player);
    }
}