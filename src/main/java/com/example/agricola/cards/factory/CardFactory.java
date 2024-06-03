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
//        Map<String, Integer> accumulatedAmounts = new HashMap<>();
//        accumulatedAmounts.put("food", 0);
//        accumulatedAmounts.put("wood", 0);

//        for (int i = 0; i < 7; i++) {
//            actionCards.add(new AccumulativeActionCard(i, "Accumulative Action Card " + (i + 1), "Description of Accumulative Action Card " + (i + 1), accumulatedAmounts));
//            actionCards.add(new NonAccumulativeActionCard(i + 8, "Non-Accumulative Action Card " + (i + 1), "Description of Non-Accumulative Action Card " + (i + 1)));
//        }
//
//        actionCards.add(new ClayMine(15));
//        actionCards.add(new WanderingTheater(16));
//        actionCards.add(new Bush(17));
//
//        int roundId = 17;
//        roundCards.add(new AccumulativeRoundCard(roundId++, "Accumulative Round Card A" + (0), "Description of Accumulative Round Card A" + (0), 1, accumulatedAmounts));
//        roundCards.add(new NonAccumulativeRoundCard(roundId++, "Non-Accumulative Round Card A" + (1), "Description of Non-Accumulative Round Card A" + (1), 1));
//        roundCards.add(new AccumulativeRoundCard(roundId++, "Accumulative Round Card A" + (2), "Description of Accumulative Round Card A" + (21), 1, accumulatedAmounts));
//        roundCards.add(new NonAccumulativeRoundCard(roundId++, "Non-Accumulative Round Card A" + (3), "Description of Non-Accumulative Round Card A" + (3), 1));
//
//        roundCards.add(new AccumulativeRoundCard(roundId++, "Accumulative Round Card B" + (4), "Description of Accumulative Round Card B" + (4), 2, accumulatedAmounts));
//        roundCards.add(new AccumulativeRoundCard(roundId++, "Accumulative Round Card B" + (5), "Description of Accumulative Round Card B" + (5), 2, accumulatedAmounts));
//        roundCards.add(new NonAccumulativeRoundCard(roundId++, "Non-Accumulative Round Card B" + (6), "Description of Non-Accumulative Round Card B" + (6), 2));
//
//        roundCards.add(new AccumulativeRoundCard(roundId++, "Accumulative Round Card C" + (7), "Description of Accumulative Round Card C" + (7), 3, accumulatedAmounts));
//        roundCards.add(new NonAccumulativeRoundCard(roundId++, "Non-Accumulative Round Card C" + (8), "Description of Non-Accumulative Round Card C" + (8), 3));
//
//        roundCards.add(new AccumulativeRoundCard(roundId++, "Accumulative Round Card D" + (9), "Description of Accumulative Round Card D" + (9), 4, accumulatedAmounts));
//        roundCards.add(new NonAccumulativeRoundCard(roundId++, "Non-Accumulative Round Card D" + (10), "Description of Non-Accumulative Round Card D" + (10), 4));
//
//        roundCards.add(new AccumulativeRoundCard(roundId++, "Accumulative Round Card E" + (11), "Description of Accumulative Round Card E" + (11), 5, accumulatedAmounts));
//        roundCards.add(new NonAccumulativeRoundCard(roundId++, "Non-Accumulative Round Card E" + (12), "Description of Non-Accumulative Round Card E" + (12), 5));
//
//        roundCards.add(new AccumulativeRoundCard(roundId++, "Accumulative Round Card F" + (13), "Description of Accumulative Round Card F" + (13), 6, accumulatedAmounts));

//        for (int i = 0; i < 8; i++) {
//            minorImprovementCards.add(new TestMinorImprovementCard(i, "Minor Improvement Card " + (i + 1), "Description of Minor Improvement Card " + (i + 1), new HashMap<>(), new HashMap<>(), new HashMap<>()));
//            occupationCards.add(new TestOccupationCard(i, "Test Occupation Card " + (i + 1), "Description of Test Occupation Card " + (i + 1), new HashMap<>(), new HashMap<>(), 1, 4));
//        }

//        for (int i = 0; i < 5; i++) {
//            majorImprovementCards.add(new MajorImprovementCard(i, "Major Improvement Card " + (i + 1), "Description of Major Improvement Card " + (i + 1), new HashMap<>(), new HashMap<>(), new HashMap<>(), 1, false, ExchangeTiming.ANYTIME));
//        }
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
        actionCards.add(new Teaching2(11));
        actionCards.add(new Worker(12));
        actionCards.add(new Forest(13));
        actionCards.add(new ClayMine2(14));
        actionCards.add(new ReedField(15));
        actionCards.add(new Fishing(16));


        roundCards.add(new SheepMarket(17, 1));
        roundCards.add(new BuildFence(18, 1));
        roundCards.add(new PlantSeed(19, 1));
        roundCards.add(new PurchaseMajor(20, 1));

        roundCards.add(new AddFamilyMember(21, 2));
        roundCards.add(new NonAccumulativeRoundCard(22, "22 비누적 자원 카드 주기2 ","효과가 없는 라운드 카드", 2));
        roundCards.add(new NonAccumulativeRoundCard(23, "23 비누적 자원 카드 주기2 ","효과가 없는 라운드 카드", 2));

        roundCards.add(new NonAccumulativeRoundCard(24, "24 비누적 자원 카드 주기3 ","효과가 없는 라운드 카드", 3));
        roundCards.add(new NonAccumulativeRoundCard(25, "25 비누적 자원 카드 주기3 ","효과가 없는 라운드 카드", 3));

        roundCards.add(new NonAccumulativeRoundCard(26, "26 비누적 자원 카드 주기4 ","효과가 없는 라운드 카드", 4));
        roundCards.add(new NonAccumulativeRoundCard(27, "27 비누적 자원 카드 주기4 ","효과가 없는 라운드 카드", 4));

        roundCards.add(new NonAccumulativeRoundCard(28, "28 비누적 자원 카드 주기5 ","효과가 없는 라운드 카드", 5));
        roundCards.add(new NonAccumulativeRoundCard(29, "29 비누적 자원 카드 주기5 ","효과가 없는 라운드 카드", 5));

        roundCards.add(new RenovateFarms(30, 6));

        majorImprovementCards.add(new PotteryWorkshop(31));
        majorImprovementCards.add(new Hearth1(32));
        majorImprovementCards.add(new FurnitureWorkshop(33));
        majorImprovementCards.add(new StoneOven(34));
        majorImprovementCards.add(new Hearth2(35));
        majorImprovementCards.add(new ClayOven(36));

        occupationCards.add(new Advisor(37));
        occupationCards.add(new LivestockMerchant(38));
        occupationCards.add(new Lumberjack(39));
        occupationCards.add(new Magician(40));
        occupationCards.add(new MasterBuilder(41));
        occupationCards.add(new Plasterer(42));
        occupationCards.add(new Roofer(43));
        occupationCards.add(new ShepherdCard(44));

        minorImprovementCards.add(new ClayPit(45));
        minorImprovementCards.add(new CompressedSoil(46));
        minorImprovementCards.add(new GrainShovel(47));
        minorImprovementCards.add(new HardenedClay(48));
        minorImprovementCards.add(new KitchenRoom(49));
        minorImprovementCards.add(new StoneAxe(50));
        minorImprovementCards.add(new WaterTrough(51));
        minorImprovementCards.add(new WoodYard(52));

    }
}
