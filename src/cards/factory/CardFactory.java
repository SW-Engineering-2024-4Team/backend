package cards.factory;

import cards.action.AccumulativeActionCard;
import cards.action.NonAccumulativeActionCard;
import cards.minorimprovement.TestMinorImprovementCard;
import cards.occupation.TestOccupationCard;
import cards.round.AccumulativeRoundCard;
import cards.round.NonAccumulativeRoundCard;
import cards.minorimprovement.MinorImprovementCard;
import cards.occupation.OccupationCard;
import cards.majorimprovement.MajorImprovementCard;
import cards.common.CommonCard;

import java.util.HashMap;
import java.util.List;

public class CardFactory {
    public static void createCards(List<CommonCard> actionCards, List<CommonCard> roundCards, List<CommonCard> minorImprovementCards, List<CommonCard> occupationCards, List<CommonCard> majorImprovementCards) {
        for (int i = 0; i < 8; i++) {
            actionCards.add(new AccumulativeActionCard(i, "Accumulative Action Card " + (i + 1), "Description of Accumulative Action Card " + (i + 1)));
            actionCards.add(new NonAccumulativeActionCard(i + 8, "Non-Accumulative Action Card " + (i + 1), "Description of Non-Accumulative Action Card " + (i + 1)));
        }

        int roundId = 0;
        roundCards.add(new AccumulativeRoundCard(roundId++, "Accumulative Round Card A" + (0), "Description of Accumulative Round Card A" + (0), 1));
        roundCards.add(new NonAccumulativeRoundCard(roundId++, "Non-Accumulative Round Card A" + (1), "Description of Non-Accumulative Round Card A" + (1), 1));
        roundCards.add(new AccumulativeRoundCard(roundId++, "Accumulative Round Card A" + (2), "Description of Accumulative Round Card A" + (21), 1));
        roundCards.add(new NonAccumulativeRoundCard(roundId++, "Non-Accumulative Round Card A" + (3), "Description of Non-Accumulative Round Card A" + (3), 1));

        roundCards.add(new AccumulativeRoundCard(roundId++, "Accumulative Round Card B" + (4), "Description of Accumulative Round Card B" + (4), 2));
        roundCards.add(new AccumulativeRoundCard(roundId++, "Accumulative Round Card B" + (5), "Description of Accumulative Round Card B" + (5), 2));
        roundCards.add(new NonAccumulativeRoundCard(roundId++, "Non-Accumulative Round Card B" + (6), "Description of Non-Accumulative Round Card B" + (6), 2));

        roundCards.add(new AccumulativeRoundCard(roundId++, "Accumulative Round Card C" + (7), "Description of Accumulative Round Card C" + (7), 3));
        roundCards.add(new NonAccumulativeRoundCard(roundId++, "Non-Accumulative Round Card C" + (8), "Description of Non-Accumulative Round Card C" + (8), 3));

        roundCards.add(new AccumulativeRoundCard(roundId++, "Accumulative Round Card D" + (9), "Description of Accumulative Round Card D" + (9), 4));
        roundCards.add(new NonAccumulativeRoundCard(roundId++, "Non-Accumulative Round Card D" + (10), "Description of Non-Accumulative Round Card D" + (10), 4));

        roundCards.add(new AccumulativeRoundCard(roundId++, "Accumulative Round Card E" + (11), "Description of Accumulative Round Card E" + (11), 5));
        roundCards.add(new NonAccumulativeRoundCard(roundId++, "Non-Accumulative Round Card E" + (12), "Description of Non-Accumulative Round Card E" + (12), 5));

        roundCards.add(new AccumulativeRoundCard(roundId++, "Accumulative Round Card F" + (13), "Description of Accumulative Round Card F" + (13), 6));

        for (int i = 0; i < 8; i++) {
            minorImprovementCards.add(new TestMinorImprovementCard(i, "Minor Improvement Card " + (i + 1), "Description of Minor Improvement Card " + (i + 1), new HashMap<>(), new HashMap<>(), new HashMap<>()));
            occupationCards.add(new TestOccupationCard(i, "Occupation Card " + (i + 1), "Description of Occupation Card " + (i + 1), new HashMap<>(), new HashMap<>(), 1, 4));
        }


//        for (int i = 0; i < 6; i++) {
//            majorImprovementCards.add(new MajorImprovementCard(i, "Major Improvement Card " + (i + 1), "Description of Major Improvement Card " + (i + 1), new HashMap<>(), new HashMap<>(), new HashMap<>(), 1, false));
//        }
        for (int i = 0; i < 5; i++) {
            majorImprovementCards.add(new MajorImprovementCard(i, "Major Improvement Card " + (i + 1), "Description of Major Improvement Card " + (i + 1), new HashMap<>(), new HashMap<>(), new HashMap<>(), 1, false));
        }
    }
}
