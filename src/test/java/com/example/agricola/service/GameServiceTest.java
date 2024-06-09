package com.example.agricola.service;

import com.example.agricola.cards.common.ActionRoundCard;
import com.example.agricola.cards.factory.imp.action.*;
import com.example.agricola.cards.factory.imp.round.*;
import com.example.agricola.controller.GameController;
import com.example.agricola.controller.RoomController;
import com.example.agricola.models.MainBoard;
import com.example.agricola.models.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    @Test
    void convertToJson() {
        RoomController rm = new RoomController();
        List<Player> players = List.of();
        MainBoard mainBoard = new MainBoard();
        GameService gs = new GameService();
        //GameController gc = mock(GameController.class);
        Player p1 = new Player("1", "yunjae", gs);
        Player p2 = new Player("2", "yunjae2", gs);
        Player p3 = new Player("3", "yunjae3", gs);
        Player p4 = new Player("4", "yunjae4", gs);
        gs.setMainBoard(mainBoard);
        /**
         * 카드 객체들
         */
        List<ActionRoundCard> actionCards = new ArrayList<ActionRoundCard>();
        List<ActionRoundCard> roundCards = new ArrayList<ActionRoundCard>();
        //카드객체 생성
        Bush bush = new Bush(1);
        Bush bush2 = new Bush(2);
        ClayMine clayMine = new ClayMine(3);
        ClayMine2 clayMine2 = new ClayMine2(4);
        FarmExpansion farmExpansion = new FarmExpansion(5);
        Fishing fishing = new Fishing(6);
        Forest forest = new Forest(7);
        MeetingPlace meetingPlace= new MeetingPlace(8);
        PlowField plowField = new PlowField(9);
        ReedField reedField = new ReedField(10);
        ResourceMarket resourceMarket = new ResourceMarket(11);
        Seed seed = new Seed(12);
        Teaching1 teaching1 = new Teaching1(13);
        Teaching2 teaching2= new Teaching2(14);
        WanderingTheater wanderingTheater = new WanderingTheater(15);
        Worker worker = new Worker(16);
        AddFamilyMember addFamilyMember = new AddFamilyMember(17, 1);
        BuildFence buildFence = new BuildFence(18, 2);
        PlantSeed plantSeed = new PlantSeed(19, 3);
        PurchaseMajor purchaseMajor = new PurchaseMajor(20, 4);
        RenovateFarms renovateFarms = new RenovateFarms(21, 5);
        SheepMarket sheepMarket = new SheepMarket(22, 6);
        //카드추가
        actionCards.add(bush);
        actionCards.add(bush2);
        actionCards.add(clayMine);
        actionCards.add(clayMine2);
        actionCards.add(farmExpansion);
        actionCards.add(fishing);
        actionCards.add(forest);;
        actionCards.add(meetingPlace);
        actionCards.add(plowField);
        actionCards.add(reedField);
        actionCards.add(resourceMarket);
        actionCards.add(seed);
        actionCards.add(teaching1);
        actionCards.add(teaching2);
        actionCards.add(wanderingTheater);
        actionCards.add(worker);

        roundCards.add(addFamilyMember);
        roundCards.add(buildFence);
        roundCards.add(plantSeed);
        roundCards.add(purchaseMajor);
        roundCards.add(renovateFarms);
        roundCards.add(sheepMarket);

        //gs.updatePlayerPositions;

    }
}