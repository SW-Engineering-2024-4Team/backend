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

        roundCards.add(new PlantSeed(15, 1));
        roundCards.add(new BuildFence(16, 2));
        roundCards.add(new SheepMarket(17, 1));
        roundCards.add(new PurchaseMajor(15, 1));
        roundCards.add(new AddFamilyMember(19, 2));
        roundCards.add(new RenovateFarms(20, 1));





        roundCards.add(new NonAccumulativeRoundCard(51, "22 비누적 자원 카드 주기2 ","효과가 없는 라운드 카드", 2));
//        roundCards.add(new NonAccumulativeRoundCard(21, "23 비누적 자원 카드 주기2 ","효과가 없는 라운드 카드", 2));
//
        roundCards.add(new NonAccumulativeRoundCard(52, "24 비누적 자원 카드 주기3 ","효과가 없는 라운드 카드", 3));
        roundCards.add(new NonAccumulativeRoundCard(53, "25 비누적 자원 카드 주기3 ","효과가 없는 라운드 카드", 3));

        roundCards.add(new NonAccumulativeRoundCard(54, "26 비누적 자원 카드 주기4 ","효과가 없는 라운드 카드", 4));
        roundCards.add(new NonAccumulativeRoundCard(25, "27 비누적 자원 카드 주기4 ","효과가 없는 라운드 카드", 4));

        roundCards.add(new NonAccumulativeRoundCard(56, "28 비누적 자원 카드 주기5 ","효과가 없는 라운드 카드", 5));
        roundCards.add(new NonAccumulativeRoundCard(57, "29 비누적 자원 카드 주기5 ","효과가 없는 라운드 카드", 5));
//
        roundCards.add(new NonAccumulativeRoundCard(58, "29 비누적 자원 카드 주기6 ","효과가 없는 라운드 카드", 5));
//        roundCards.add(new RenovateFarms(28, 6));

        majorImprovementCards.add(new PotteryWorkshop(21));
        majorImprovementCards.add(new FurnitureWorkshop(22));
        majorImprovementCards.add(new Hearth1(23));
        majorImprovementCards.add(new Hearth2(24));
        majorImprovementCards.add(new StoneOven(25));
        majorImprovementCards.add(new ClayOven(26));

        occupationCards.add(new Advisor(27));
        occupationCards.add(new LivestockMerchant(28));
        occupationCards.add(new Lumberjack(29));
        occupationCards.add(new Magician(30));
        occupationCards.add(new MasterBuilder(31));
        occupationCards.add(new Plasterer(32));
        occupationCards.add(new Roofer(33));
        occupationCards.add(new ShepherdCard(34));

        minorImprovementCards.add(new GrainShovel(35));
        minorImprovementCards.add(new CompressedSoil(36));
        minorImprovementCards.add(new KitchenRoom(37));
        minorImprovementCards.add(new StoneAxe(38));
        minorImprovementCards.add(new WoodYard(39));
        minorImprovementCards.add(new WaterTrough(40));
        minorImprovementCards.add(new HardenedClay(41));
        minorImprovementCards.add(new ClayPit(42));







    }
}
