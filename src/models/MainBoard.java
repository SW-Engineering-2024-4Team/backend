package models;

import cards.common.AccumulativeCard;
import cards.common.ActionRoundCard;
import cards.common.CommonCard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainBoard {
    private List<CommonCard> actionCards;
    private List<ActionRoundCard> roundCards;
    private List<CommonCard> majorImprovementCards;

    public void initializeBoard(List<CommonCard> actionCards, List<List<CommonCard>> roundCycles, List<CommonCard> majorImprovementCards) {
        this.actionCards = actionCards;
        this.roundCards = new ArrayList<>();
        for (List<CommonCard> cycle : roundCycles) {
            this.roundCards.addAll((Collection<? extends ActionRoundCard>) cycle);
        }
        this.majorImprovementCards = majorImprovementCards;
    }

    public void revealRoundCard(int round) {
        ActionRoundCard roundCard = roundCards.get(round - 1);
        roundCard.reveal();
        // 프론트엔드에 라운드 카드 공개 메시지 전송
        // WebSocketService.sendMessageToClient("roundCardRevealed", roundCard);
    }

    public void accumulateResources() {
        for (CommonCard card : actionCards) {
            if (card instanceof AccumulativeCard) {
                ((AccumulativeCard) card).accumulateResources();
            }
        }
        for (ActionRoundCard card : roundCards) {
            if (card instanceof AccumulativeCard && card.isRevealed()) {
                ((AccumulativeCard) card).accumulateResources();
            }
        }
    }

    public List<CommonCard> getActionCards() {
        return actionCards;
    }

    public List<ActionRoundCard> getRoundCards() {
        return roundCards;
    }

    public List<CommonCard> getMajorImprovementCards() {
        return majorImprovementCards;
    }

    public List<CommonCard> getAllCards() {
        List<CommonCard> allCards = new ArrayList<>();
        allCards.addAll(actionCards);
        allCards.addAll(roundCards);
        allCards.add((CommonCard) majorImprovementCards);
        return allCards;
    }

    public void removeMajorImprovementCard(CommonCard card) {
        majorImprovementCards.remove(card);
    }

}
