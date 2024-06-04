package com.example.agricola.cards.factory;



import com.example.agricola.cards.common.CommonCard;
import com.example.agricola.cards.factory.imp.action.*;
import com.example.agricola.cards.factory.imp.major.*;
import com.example.agricola.cards.factory.imp.minor.*;
import com.example.agricola.cards.factory.imp.occupation.*;
import com.example.agricola.cards.factory.imp.round.*;
import com.example.agricola.cards.round.NonAccumulativeRoundCard;

import java.util.List;

public class CardFactory {

    public static void createCards(List<CommonCard> actionCards, List<CommonCard> roundCards, List<CommonCard> minorImprovementCards, List<CommonCard> occupationCards, List<CommonCard> majorImprovementCards) {
        actionCards.add(new Bush(1));
        actionCards.add(new Bush2(2));
        actionCards.add(new ResourceMarket(3));
        actionCards.add(new ClayMine(4));
        actionCards.add(new Teaching1(5));
        actionCards.add(new WanderingTheater(6));
        actionCards.add(new FarmExpansion(7));
        actionCards.add(new MeetingPlace(8));
        actionCards.add(new Seed(9));
        actionCards.add(new PlowField(10));
        actionCards.add(new Worker(11));
        actionCards.add(new Forest(12));
        actionCards.add(new ClayMine2(13));
        actionCards.add(new Fishing(14));
//        actionCards.add(new Teaching2(11));
//        actionCards.add(new ReedField(15));


        roundCards.add(new AddFamilyMember(19, 2));
        roundCards.add(new PlantSeed(16, 1));
        roundCards.add(new PurchaseMajor(15, 1));
        roundCards.add(new RenovateFarms(20, 1));
        roundCards.add(new SheepMarket(18, 1));
        roundCards.add(new BuildFence(17, 2));

        roundCards.add(new NonAccumulativeRoundCard(20, "22 비누적 자원 카드 주기2 ","효과가 없는 라운드 카드", 2));
//        roundCards.add(new NonAccumulativeRoundCard(21, "23 비누적 자원 카드 주기2 ","효과가 없는 라운드 카드", 2));
//
        roundCards.add(new NonAccumulativeRoundCard(22, "24 비누적 자원 카드 주기3 ","효과가 없는 라운드 카드", 3));
        roundCards.add(new NonAccumulativeRoundCard(23, "25 비누적 자원 카드 주기3 ","효과가 없는 라운드 카드", 3));

        roundCards.add(new NonAccumulativeRoundCard(24, "26 비누적 자원 카드 주기4 ","효과가 없는 라운드 카드", 4));
        roundCards.add(new NonAccumulativeRoundCard(25, "27 비누적 자원 카드 주기4 ","효과가 없는 라운드 카드", 4));

        roundCards.add(new NonAccumulativeRoundCard(26, "28 비누적 자원 카드 주기5 ","효과가 없는 라운드 카드", 5));
        roundCards.add(new NonAccumulativeRoundCard(27, "29 비누적 자원 카드 주기5 ","효과가 없는 라운드 카드", 5));
//
        roundCards.add(new NonAccumulativeRoundCard(28, "29 비누적 자원 카드 주기6 ","효과가 없는 라운드 카드", 5));
//        roundCards.add(new RenovateFarms(28, 6));

        majorImprovementCards.add(new PotteryWorkshop(29));
        majorImprovementCards.add(new Hearth1(30));
        majorImprovementCards.add(new FurnitureWorkshop(31));
        majorImprovementCards.add(new StoneOven(32));
        majorImprovementCards.add(new Hearth2(33));
        majorImprovementCards.add(new ClayOven(34));

        occupationCards.add(new Advisor(35));
        occupationCards.add(new LivestockMerchant(36));
        occupationCards.add(new Lumberjack(37));
        occupationCards.add(new Magician(38));
        occupationCards.add(new MasterBuilder(39));
        occupationCards.add(new Plasterer(40));
        occupationCards.add(new Roofer(41));
        occupationCards.add(new ShepherdCard(42));

        minorImprovementCards.add(new ClayPit(43));
        minorImprovementCards.add(new CompressedSoil(44));
        minorImprovementCards.add(new GrainShovel(45));
        minorImprovementCards.add(new HardenedClay(46));
        minorImprovementCards.add(new KitchenRoom(47));
        minorImprovementCards.add(new StoneAxe(48));
        minorImprovementCards.add(new WaterTrough(49));
        minorImprovementCards.add(new WoodYard(50));

    }
}
