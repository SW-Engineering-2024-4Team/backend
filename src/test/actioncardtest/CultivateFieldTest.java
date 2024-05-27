package test.actioncardtest;

import cards.action.CultivateField;
import cards.action.ExtendFarm;
import controllers.GameController;
import controllers.RoomController;
import models.Player;
import org.junit.jupiter.api.Test;

import java.util.List;

class CultivateFieldTest {

    @Test
    void execute() {
        RoomController rm = new RoomController();
        List<Player> players = List.of();
        GameController gc = new GameController("1234", rm, players);
        //GameController gc = mock(GameController.class);
        Player player = new Player("1", "Yunjae", gc);
        CultivateField cf = new CultivateField();

        //테스트 구체화 필요
        cf.execute(player);
    }
}