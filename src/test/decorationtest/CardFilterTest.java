package test.decorationtest;

import cards.common.ActionRoundCard;
import cards.factory.imp.action.FarmExpansion;
import cards.factory.imp.round.RenovateFarms;
import controllers.GameController;
import controllers.RoomController;
import models.MainBoard;
import models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardFilterTest {
    private GameController gameController;
    private MainBoard mainBoard;
    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player("player1", "Player 1", null);
        RoomController roomController = new RoomController();
        roomController.handleGameStart("TestRoom123", Arrays.asList(player));
        gameController = roomController.getGameController();
        mainBoard = gameController.getMainBoard();

        ActionRoundCard farmExpansion = new FarmExpansion(1);
        ActionRoundCard renovateFarms = new RenovateFarms(2, 1);

        mainBoard.setActionCards(Arrays.asList(farmExpansion, renovateFarms));
        mainBoard.setRoundCards(Arrays.asList());
    }

    @Test
    public void testGetBuildOrRenovateCards() {
        List<ActionRoundCard> filteredCards = mainBoard.getBuildOrRenovateCards();
        for (ActionRoundCard card : filteredCards) {
            System.out.println("card = " + card.getName());
        }
        assertEquals(2, filteredCards.size(), "There should be 2 cards that can build or renovate.");
    }
}
