package com.example.agricola.models;

import com.example.agricola.cards.common.AccumulativeCard;
import com.example.agricola.cards.common.ActionRoundCard;
import com.example.agricola.cards.common.CommonCard;
import com.example.agricola.cards.majorimprovement.MajorImprovementCard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainBoard {
    private List<ActionRoundCard> actionCards;
    private List<ActionRoundCard> roundCards;
    private List<CommonCard> majorImprovementCards;

    public void setActionCards(List<ActionRoundCard> actionCards) {
        this.actionCards = actionCards;
    }

    public void setRoundCards(List<ActionRoundCard> roundCards) {
        this.roundCards = roundCards;
    }

    public void setMajorImprovementCards(List<CommonCard> majorImprovementCards) {
        this.majorImprovementCards = majorImprovementCards;
    }

    public MainBoard() {
        this.actionCards = new ArrayList<>();
        this.roundCards = new ArrayList<>();
        this.majorImprovementCards = new ArrayList<>();
    }

    public void initializeBoard(List<ActionRoundCard> actionCards, List<List<ActionRoundCard>> roundCycles, List<CommonCard> majorImprovementCards) {
        this.actionCards = actionCards;
        this.roundCards = new ArrayList<>();
        for (List<ActionRoundCard> cycle : roundCycles) {
            this.roundCards.addAll(cycle);
        }
        this.majorImprovementCards = majorImprovementCards;
    }

    public void revealRoundCard(int round) {
        ActionRoundCard roundCard = roundCards.get(round - 1);
        roundCard.reveal();
        System.out.println("Round " + round + " card revealed: " + roundCard.getName());
    }

    public List<ActionRoundCard> getRevealedRoundCards() {
        List<ActionRoundCard> revealedRoundCards = new ArrayList<>();
        for (ActionRoundCard card : roundCards) {
            if (card.isRevealed()) {
                revealedRoundCards.add(card);
            }
        }
        return revealedRoundCards;
    }

    public void accumulateResources() {
        for (ActionRoundCard card : actionCards) {
            if (card instanceof AccumulativeCard) {
                System.out.println("Accumulating resources for card: " + card.getName());
                ((AccumulativeCard) card).accumulateResources();
            }
        }
        for (ActionRoundCard card : roundCards) {
            if (card instanceof AccumulativeCard && card.isRevealed()) {
                System.out.println("Accumulating resources for revealed card: " + card.getName());
                ((AccumulativeCard) card).accumulateResources();
            }
        }
    }

    public List<ActionRoundCard> getActionCards() {
        return actionCards;
    }

    public List<ActionRoundCard> getRoundCards() {
        return roundCards;
    }

    public List<CommonCard> getMajorImprovementCards() {
        return majorImprovementCards;
    }

    public List<CommonCard> getAvailableMajorImprovementCards() {
        List<CommonCard> availableCards = new ArrayList<>();
        for (CommonCard card : majorImprovementCards) {
            if (card instanceof MajorImprovementCard && !((MajorImprovementCard) card).isPurchased()) {
                availableCards.add(card);
            }
        }
        return availableCards;
    }

    public List<CommonCard> getAllCards() {
        List<CommonCard> allCards = new ArrayList<>();
        allCards.addAll(actionCards);
        allCards.addAll(roundCards);
        allCards.addAll(majorImprovementCards);
        return allCards;
    }

    public ActionRoundCard getCardByName(String name) {
        for (ActionRoundCard card : getActionCards()) {
            if (card.getName().equals(name)) {
                return card;
            }
        }
        for (ActionRoundCard card : getRoundCards()) {
            if (card.getName().equals(name)) {
                return card;
            }
        }
        return null;
    }

    public void removeMajorImprovementCard(CommonCard card) {
        System.out.println("Removing major improvement card: " + card.getName());
        majorImprovementCards.remove(card);
    }

    public boolean canPlaceFamilyMember(ActionRoundCard card) {
        return !card.isOccupied();
    }

    public void placeFamilyMember(ActionRoundCard card) {
        if (canPlaceFamilyMember(card)) {
            card.setOccupied(true);
        } else {
            throw new IllegalStateException("Card is already occupied: " + card.getName());
        }
    }

    public boolean isCardOccupied(ActionRoundCard card) {
        return card.isOccupied();
    }

    public void resetFamilyMembersOnCards() {
        System.out.println("Resetting family members on action cards...");
        for (ActionRoundCard card : actionCards) {
            System.out.println("Action card: " + card.getName() + ", occupied: " + card.isOccupied());
            card.setOccupied(false);
            System.out.println("Action card: " + card.getName() + ", reset to occupied: " + card.isOccupied());
        }
        System.out.println("Resetting family members on round cards...");
        for (ActionRoundCard card : roundCards) {
            System.out.println("Round card: " + card.getName() + ", occupied: " + card.isOccupied());
            card.setOccupied(false);
            System.out.println("Round card: " + card.getName() + ", reset to occupied: " + card.isOccupied());
        }
    }

    public void addCard(CommonCard card, String type) {
        if (type.equals("action") && card instanceof ActionRoundCard) {
            actionCards.add((ActionRoundCard) card);
        } else if (type.equals("round") && card instanceof ActionRoundCard) {
            roundCards.add((ActionRoundCard) card);
        } else if (type.equals("majorImprovement") && card instanceof MajorImprovementCard) {
            majorImprovementCards.add(card);
        } else {
            throw new IllegalArgumentException("Invalid card type or card type mismatch.");
        }
    }

    public void printCardLists() {
        System.out.println("Action Cards:");
        for (ActionRoundCard card : actionCards) {
            System.out.println("- " + card.getName() + " (hashCode: " + card.hashCode() + ")");
        }

        System.out.println("Round Cards:");
        for (ActionRoundCard card : roundCards) {
            System.out.println("- " + card.getName() + " (hashCode: " + card.hashCode() + ")");
        }
    }

    public List<ActionRoundCard> getBuildOrRenovateCards() {
        return actionCards.stream()
                .filter(ActionRoundCard::executesBuildOrRenovate)
                .collect(Collectors.toList());
    }

    public void updateCardsWithDecorated(List<ActionRoundCard> originalCards, List<ActionRoundCard> decoratedCards) {
        for (int i = 0; i < actionCards.size(); i++) {
            if (originalCards.contains(actionCards.get(i))) {
                actionCards.set(i, decoratedCards.get(originalCards.indexOf(actionCards.get(i))));
            }
        }
        for (int i = 0; i < roundCards.size(); i++) {
            if (originalCards.contains(roundCards.get(i))) {
                roundCards.set(i, decoratedCards.get(originalCards.indexOf(roundCards.get(i))));
            }
        }
    }

    public ActionRoundCard getCardById(int cardID) {
        for (ActionRoundCard card : getActionCards()) {
            if (card.getId() == cardID) {
                return card;
            }
        }
        for (ActionRoundCard card : getRoundCards()) {
            if (card.getId() == (cardID)) {
                return card;
            }
        }
        return null;
    }
}
