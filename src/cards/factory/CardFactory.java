package cards.factory;

import cards.action.AccumulativeActionCard;
import cards.action.NonAccumulativeActionCard;
import cards.common.CommonCard;
import cards.factory.imp.action.Bush;
import cards.factory.imp.action.ClayMine;
import cards.factory.imp.action.WanderingTheater;
import cards.majorimprovement.MajorImprovementCard;
import cards.minorimprovement.TestMinorImprovementCard;
import cards.occupation.TestOccupationCard;
import cards.round.AccumulativeRoundCard;
import cards.round.NonAccumulativeRoundCard;
import enums.ExchangeTiming;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardFactory {

    public static void createCards(List<CommonCard> actionCards, List<CommonCard> roundCards, List<CommonCard> minorImprovementCards, List<CommonCard> occupationCards, List<CommonCard> majorImprovementCards) {
        Map<String, Integer> accumulatedAmounts = new HashMap<>();
        accumulatedAmounts.put("food", 1);
        accumulatedAmounts.put("wood", 2);

        for (int i = 0; i < 7; i++) {
            actionCards.add(new AccumulativeActionCard(i, "Accumulative Action Card " + (i + 1), "Description of Accumulative Action Card " + (i + 1), accumulatedAmounts));
            actionCards.add(new NonAccumulativeActionCard(i + 8, "Non-Accumulative Action Card " + (i + 1), "Description of Non-Accumulative Action Card " + (i + 1)));
        }

        actionCards.add(new ClayMine(15));
        actionCards.add(new WanderingTheater(16));
        actionCards.add(new Bush(17));

        int roundId = 0;
        roundCards.add(new AccumulativeRoundCard(roundId++, "Accumulative Round Card A" + (0), "Description of Accumulative Round Card A" + (0), 1, accumulatedAmounts));
        roundCards.add(new NonAccumulativeRoundCard(roundId++, "Non-Accumulative Round Card A" + (1), "Description of Non-Accumulative Round Card A" + (1), 1));
        roundCards.add(new AccumulativeRoundCard(roundId++, "Accumulative Round Card A" + (2), "Description of Accumulative Round Card A" + (21), 1, accumulatedAmounts));
        roundCards.add(new NonAccumulativeRoundCard(roundId++, "Non-Accumulative Round Card A" + (3), "Description of Non-Accumulative Round Card A" + (3), 1));

        roundCards.add(new AccumulativeRoundCard(roundId++, "Accumulative Round Card B" + (4), "Description of Accumulative Round Card B" + (4), 2, accumulatedAmounts));
        roundCards.add(new AccumulativeRoundCard(roundId++, "Accumulative Round Card B" + (5), "Description of Accumulative Round Card B" + (5), 2, accumulatedAmounts));
        roundCards.add(new NonAccumulativeRoundCard(roundId++, "Non-Accumulative Round Card B" + (6), "Description of Non-Accumulative Round Card B" + (6), 2));

        roundCards.add(new AccumulativeRoundCard(roundId++, "Accumulative Round Card C" + (7), "Description of Accumulative Round Card C" + (7), 3, accumulatedAmounts));
        roundCards.add(new NonAccumulativeRoundCard(roundId++, "Non-Accumulative Round Card C" + (8), "Description of Non-Accumulative Round Card C" + (8), 3));

        roundCards.add(new AccumulativeRoundCard(roundId++, "Accumulative Round Card D" + (9), "Description of Accumulative Round Card D" + (9), 4, accumulatedAmounts));
        roundCards.add(new NonAccumulativeRoundCard(roundId++, "Non-Accumulative Round Card D" + (10), "Description of Non-Accumulative Round Card D" + (10), 4));

        roundCards.add(new AccumulativeRoundCard(roundId++, "Accumulative Round Card E" + (11), "Description of Accumulative Round Card E" + (11), 5, accumulatedAmounts));
        roundCards.add(new NonAccumulativeRoundCard(roundId++, "Non-Accumulative Round Card E" + (12), "Description of Non-Accumulative Round Card E" + (12), 5));

        roundCards.add(new AccumulativeRoundCard(roundId++, "Accumulative Round Card F" + (13), "Description of Accumulative Round Card F" + (13), 6, accumulatedAmounts));

        for (int i = 0; i < 8; i++) {
            minorImprovementCards.add(new TestMinorImprovementCard(i, "Minor Improvement Card " + (i + 1), "Description of Minor Improvement Card " + (i + 1), new HashMap<>(), new HashMap<>(), new HashMap<>()));
            occupationCards.add(new TestOccupationCard(i, "Test Occupation Card " + (i + 1), "Description of Test Occupation Card " + (i + 1), new HashMap<>(), new HashMap<>(), 1, 4));
        }

        for (int i = 0; i < 5; i++) {
            majorImprovementCards.add(new MajorImprovementCard(i, "Major Improvement Card " + (i + 1), "Description of Major Improvement Card " + (i + 1), new HashMap<>(), new HashMap<>(), new HashMap<>(), 1, false, ExchangeTiming.ANYTIME));
        }
    }
}
