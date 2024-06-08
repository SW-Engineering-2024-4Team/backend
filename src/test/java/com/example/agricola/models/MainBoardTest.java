package com.example.agricola.models;

import com.example.agricola.cards.common.ActionRoundCard;
import com.example.agricola.cards.factory.imp.action.Bush;
import com.example.agricola.cards.factory.imp.action.ClayMine;
import com.example.agricola.cards.factory.imp.action.ClayMine2;
import com.example.agricola.cards.factory.imp.action.ResourceMarket;
import com.example.agricola.cards.factory.imp.round.*;
import com.example.agricola.controller.GameController;
import com.example.agricola.controller.RoomController;
import com.example.agricola.service.GameService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MainBoardTest {
    GameService gs = new GameService();
    MainBoard mainBoard = new MainBoard();
    Player player = new Player("1", "Yunjae", gs);
    Player p1 = new Player("2", "yunjae", gs);
    Player p2 = new Player("3", "yunjae2", gs);
    Player p3 = new Player("4", "yunjae3", gs);
    Player p4 = new Player("5", "yunjae4", gs);
    ResourceMarket resourceMarket = new ResourceMarket(5);

    /**
     * 카드 객체들
     */
    List<ActionRoundCard> actionCards = new ArrayList<ActionRoundCard>();
    List<ActionRoundCard> roundCards = new ArrayList<ActionRoundCard>();

    Bush bush = new Bush(1);
    Bush bush2 = new Bush(2);
    ClayMine clayMine = new ClayMine(3);
    ClayMine2 clayMine2 = new ClayMine2(4);
    AddFamilyMember addFamilyMembe = new AddFamilyMember(6, 1);
    BuildFence buildFence = new BuildFence(7, 2);
    PlantSeed plantSeed = new PlantSeed(8, 3);
    PurchaseMajor purchaseMajor = new PurchaseMajor(9, 4);
    SheepMarket sheepMarket = new SheepMarket(10, 5);


    @Test
    void mainBoardAccumulatedResourcesList() {
        sheepMarket.reveal();
        bush.accumulateResources();
        bush.accumulateResources();
        bush2.accumulateResources();
        bush2.accumulateResources();
        bush2.accumulateResources();
        clayMine.accumulateResources();
        clayMine2.accumulateResources();
        clayMine2.accumulateResources();
        clayMine2.accumulateResources();
        clayMine2.accumulateResources();
        sheepMarket.accumulateResources();
        sheepMarket.accumulateResources();
        sheepMarket.accumulateResources();
        gs.setMainBoard(mainBoard);
        actionCards.add(bush);
        actionCards.add(bush2);
        actionCards.add(clayMine);
        actionCards.add(clayMine2);
        actionCards.add(resourceMarket);
        roundCards.add(addFamilyMembe);
        roundCards.add(buildFence);
        roundCards.add(plantSeed);
        roundCards.add(purchaseMajor);
        roundCards.add(sheepMarket);
        mainBoard.setActionCards(actionCards);
        mainBoard.setRoundCards(roundCards);
        mainBoard.mainBoardAccumulatedResourcesList(mainBoard);

    }

    @Test
    void mainboardActioncardPlayerList() { //test진행시 GameService sendPlayerResourcesToFrontEnd메서드 simpMessagingTemplate.convertAndSend("/topic/room/1", message);주석처리
        gs.setMainBoard(mainBoard);
        actionCards.add(bush);
        actionCards.add(bush2);
        actionCards.add(clayMine);
        actionCards.add(clayMine2);
        actionCards.add(resourceMarket);
        mainBoard.setActionCards(actionCards);
        List<ArrayList<String>> sendToFront = mainBoard.mainboardActionCardAccumulateResourceList(mainBoard);
        player.placeFamilyMember(bush);
        p1.placeFamilyMember(clayMine);
        p2.placeFamilyMember(resourceMarket);
        p3.placeFamilyMember(clayMine2);
        List<String> sendToFront2 = mainBoard.mainboardActioncardPlayerList(mainBoard);
        System.out.print(sendToFront2);
    }
    @Test
    void mainboardRoundcardPlayerList() { //test진행시 GameService sendChoiceRequestToFrontEnd simpMessagingTemplate.convertAndSend("/topic/room/1", message);주석
        gs.setMainBoard(mainBoard);    //플레이어 선택 기다려서 당장에 테스트 불가
        roundCards.add(addFamilyMembe);
        roundCards.add(buildFence);
        roundCards.add(plantSeed);
        roundCards.add(purchaseMajor);
        roundCards.add(sheepMarket);
        mainBoard.setRoundCards(roundCards);
        List<ArrayList<String>> sendToFront1 = mainBoard.mainboardRoundCardAccumulateResourceList(mainBoard);
        player.placeFamilyMember(addFamilyMembe);
        p1.placeFamilyMember(sheepMarket);
        p2.placeFamilyMember(purchaseMajor);
        p3.placeFamilyMember(plantSeed);
        List<String> sendToFront2 = mainBoard.mainboardActioncardPlayerList(mainBoard);
        System.out.println(sendToFront2);


    }
    @Test
    void mainboardActionCardAccumulateResourceList() {
        actionCards.add(bush);
        actionCards.add(bush2);
        actionCards.add(clayMine);
        actionCards.add(clayMine2);
        actionCards.add(resourceMarket);
        mainBoard.setActionCards(actionCards);
        List<ArrayList<String>> sendToFront = mainBoard.mainboardActionCardAccumulateResourceList(mainBoard);
    }
    @Test
    void mainboardRoundCardAccumulateResourceList() {
        roundCards.add(addFamilyMembe);
        roundCards.add(buildFence);
        roundCards.add(plantSeed);
        roundCards.add(purchaseMajor);
        roundCards.add(sheepMarket);
        mainBoard.setRoundCards(roundCards);
        List<ArrayList<String>> sendToFront = mainBoard.mainboardRoundCardAccumulateResourceList(mainBoard);
    }
}