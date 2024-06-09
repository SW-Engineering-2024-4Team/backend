package controllers;

import cards.common.ActionRoundCard;
import cards.common.CommonCard;
import cards.factory.CardFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CardController는 게임에서 사용되는 카드 덱을 관리하고 제공하는 역할을 합니다.
 * 싱글턴 패턴으로 구현되어 서버 부팅 시 한 개의 객체만 존재합니다.
 */
public class CardController extends CardFactory {
    private static CardController instance;
    private List<CommonCard> actionCards;
    private List<CommonCard> roundCards;
    private List<CommonCard> minorImprovementCards;
    private List<CommonCard> occupationCards;
    private List<CommonCard> majorImprovementCards;

    private CardController() {
        this.actionCards = new ArrayList<>();
        this.roundCards = new ArrayList<>();
        this.minorImprovementCards = new ArrayList<>();
        this.occupationCards = new ArrayList<>();
        this.majorImprovementCards = new ArrayList<>();
        initializeDecks();
    }

    /**
     * `CardController`의 싱글턴 인스턴스를 반환합니다.
     *
     * @return `CardController` 인스턴스
     */
    public static CardController getInstance() {
        if (instance == null) {
            instance = new CardController();
        }
        return instance;
    }

    /**
     * 덱을 초기화합니다. 각 종류별로 카드를 팩토리에서 생성하여 추가합니다.
     */
    private void initializeDecks() {
        CardFactory.createCards(actionCards, roundCards, minorImprovementCards, occupationCards, majorImprovementCards);
    }

    /**
     * 주어진 덱 타입에 해당하는 카드를 반환합니다.
     *
     * @param deckType 덱 타입 ("actionCards", "roundCards", "minorImprovementCards", "occupationCards", "majorImprovementCards")
     * @return 해당 덱 타입의 카드 리스트
     */
    public List<CommonCard> getDeck(String deckType) {
        switch (deckType) {
            case "actionCards":
                return actionCards;
            case "roundCards":
                return roundCards;
            case "minorImprovementCards":
                return minorImprovementCards;
            case "occupationCards":
                return occupationCards;
            case "majorImprovementCards":
                return majorImprovementCards;
            default:
                return new ArrayList<>();
        }
    }

    /**
     * 주어진 덱을 셔플합니다.
     *
     * @param deck 셔플할 덱
     */
    public void shuffleDeck(List<CommonCard> deck) {
        Collections.shuffle(deck);
    }

    /**
     * 라운드 카드들을 사이클별로 셔플하여 반환합니다.
     *
     * @return 셔플된 라운드 카드 리스트
     */
    public List<List<ActionRoundCard>> getShuffledRoundCardsByCycle() {
        List<List<ActionRoundCard>> cycles = new ArrayList<>();
        int[] roundsPerCycle = {4, 3, 2, 2, 2, 1};
        int startIndex = 0;

        for (int rounds : roundsPerCycle) {
            List<ActionRoundCard> cycle = new ArrayList<>();
            for (CommonCard card : roundCards.subList(startIndex, startIndex + rounds)) {
                if (card instanceof ActionRoundCard) {
                    cycle.add((ActionRoundCard) card);
                }
            }
            Collections.shuffle(cycle);
            cycles.add(cycle);
            startIndex += rounds;
        }

        return cycles;
    }

    /**
     * 액션 라운드 카드를 반환합니다.
     *
     * @return 액션 라운드 카드 리스트
     */
    public List<ActionRoundCard> getActionRoundCards() {
        List<ActionRoundCard> actionRoundCards = new ArrayList<>();
        for (CommonCard card : actionCards) {
            if (card instanceof ActionRoundCard) {
                actionRoundCards.add((ActionRoundCard) card);
            }
        }
        return actionRoundCards;
    }
}
