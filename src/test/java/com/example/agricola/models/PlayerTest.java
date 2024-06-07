package com.example.agricola.models;

import com.example.agricola.cards.factory.imp.action.ResourceMarket;
import com.example.agricola.controller.GameController;
import com.example.agricola.controller.RoomController;
import com.example.agricola.service.GameService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void playerResourcesList() {
        RoomController rm = new RoomController();
        List<Player> players = List.of();
        GameController gc = new GameController();
        GameService gs = new GameService();
        //GameController gc = mock(GameController.class);
        Player player = new Player("1", "Yunjae", gs);
        gc = new GameController();
        Player p1 = new Player("id", "yunjae", gs);
        Player p2 = new Player("id", "yunjae2", gs);
        Player p3 = new Player("id", "yunjae3", gs);
        Player p4 = new Player("id", "yunjae4", gs);
        ResourceMarket resourceMarket = new ResourceMarket(3);
        resourceMarket.execute(p1);


        ArrayList<String> resources = (ArrayList<String>) p1.playerResourcesList();
        ArrayList<String> resources1 = (ArrayList<String>) p2.playerResourcesList();
        ArrayList<String> resources2 = (ArrayList<String>) p3.playerResourcesList();
        ArrayList<String> resources3 = (ArrayList<String>) p4.playerResourcesList();
        for (int i = 0; i < resources.size(); i++) {
            System.out.println(resources.get(i));
        }
        for (int i = 0; i < resources1.size(); i++) {
            System.out.println(resources1.get(i));
        }
        for (int i = 0; i < resources2.size(); i++) {
            System.out.println(resources2.get(i));
        }
        for (int i = 0; i < resources3.size(); i++) {
            System.out.println(resources3.get(i));
        }

    }
}