package com.example.agricola.controller;

import com.example.agricola.cards.common.ActionRoundCard;
import com.example.agricola.cards.common.CommonCard;
import com.example.agricola.cards.factory.CardFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardController {
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

    public static CardController getInstance() {
        if (instance == null) {
            instance = new CardController();
        }
        return instance;
    }

    private void initializeDecks() {
        CardFactory.createCards(actionCards, roundCards, minorImprovementCards, occupationCards, majorImprovementCards);
    }

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

    public void shuffleDeck(List<CommonCard> deck) {
        Collections.shuffle(deck);
    }

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
//            Collections.shuffle(cycle);
            cycles.add(cycle);
            startIndex += rounds;
        }

        return cycles;
    }

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
