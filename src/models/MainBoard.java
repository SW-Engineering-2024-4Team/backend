package models;

import cards.common.AccumulativeCard;
import cards.common.ActionRoundCard;
import cards.common.CommonCard;
import cards.majorimprovement.MajorImprovementCard;

// MainBoard.java

import java.util.*;

public class MainBoard {
    private List<ActionRoundCard> actionCards;
    private List<ActionRoundCard> roundCards;
    private List<CommonCard> majorImprovementCards;

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
        // 프론트엔드에 라운드 카드 공개 메시지 전송
        // WebSocketService.sendMessageToClient("roundCardRevealed", roundCard);
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
                ((AccumulativeCard) card).accumulateResources();
            }
        }
        for (ActionRoundCard card : roundCards) {
            if (card instanceof AccumulativeCard && card.isRevealed()) {
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

    public void removeMajorImprovementCard(CommonCard card) {
        System.out.println("Removing card: " + card.getName());
        majorImprovementCards.remove(card);
    }

//    public void placeFamilyMember(ActionRoundCard card, FamilyMember familyMember) {
//        if (canPlaceFamilyMember(card)) {
//            occupyingFamilyMembers.put(card, familyMember);
//        } else {
//            throw new IllegalStateException("Card is already occupied.");
//        }
//    }

    public boolean canPlaceFamilyMember(ActionRoundCard card) {
        return !card.isOccupied();
    }
    public void placeFamilyMember(ActionRoundCard card) {
        if (canPlaceFamilyMember(card)) {
            card.setOccupied(true);
        } else {
            throw new IllegalStateException("Card is already occupied.");
        }
    }

    public boolean isCardOccupied(ActionRoundCard card) {
        return card.isOccupied();
    }

    public void resetFamilyMembersOnCards() {
        for (ActionRoundCard card : actionCards) {
            card.setOccupied(false);
        }
        for (ActionRoundCard card : roundCards) {
            card.setOccupied(false);
        }
    }



}

