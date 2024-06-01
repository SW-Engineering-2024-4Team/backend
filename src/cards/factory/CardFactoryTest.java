package cards.factory;

import cards.common.CommonCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CardFactoryTest {

    @Test
    public void testCardCreation() {
        List<CommonCard> actionCards = new ArrayList<>();
        List<CommonCard> roundCards = new ArrayList<>();
        List<CommonCard> minorImprovementCards = new ArrayList<>();
        List<CommonCard> occupationCards = new ArrayList<>();
        List<CommonCard> majorImprovementCards = new ArrayList<>();

        CardFactory.createCards(actionCards, roundCards, minorImprovementCards, occupationCards, majorImprovementCards);

        // 각 덱에 카드가 정상적으로 생성되었는지 확인
        assertFalse(actionCards.isEmpty(), "Action cards should not be empty");
        assertFalse(roundCards.isEmpty(), "Round cards should not be empty");
        assertFalse(minorImprovementCards.isEmpty(), "Minor improvement cards should not be empty");
        assertFalse(occupationCards.isEmpty(), "Occupation cards should not be empty");
        assertFalse(majorImprovementCards.isEmpty(), "Major improvement cards should not be empty");

        // 각 카드 덱의 첫 번째 카드가 null이 아닌지 확인하여 카드가 정상적으로 생성되었음을 확인
        assertNotNull(actionCards.get(0), "First action card should not be null");
        assertNotNull(roundCards.get(0), "First round card should not be null");
        assertNotNull(minorImprovementCards.get(0), "First minor improvement card should not be null");
        assertNotNull(occupationCards.get(0), "First occupation card should not be null");
        assertNotNull(majorImprovementCards.get(0), "First major improvement card should not be null");
    }
}
