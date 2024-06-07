package com.example.agricola.models;

import com.example.agricola.cards.action.AccumulativeActionCard;
import com.example.agricola.cards.action.NonAccumulativeActionCard;
import com.example.agricola.cards.common.AccumulativeCard;
import com.example.agricola.cards.common.ActionRoundCard;
import com.example.agricola.cards.common.CommonCard;
import com.example.agricola.cards.common.ExchangeableCard;
import com.example.agricola.cards.majorimprovement.MajorImprovementCard;
import com.example.agricola.enums.ExchangeTiming;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    /**
     * 메인보드위의 카드 ID정보를 ArrayList<String>으로 보내줌.
     * 라운드 카드가 보일시 "id" 라운드 카드가 아직 보여지지 않는 카드일시 "x"+"id"
     * @param mainBoard 현재 진행되고 있는 게임의 Mainboard객체
     * @return ex) ["1", "2", "3", "4", "12", "52", "x9", "x5" ...]
     * 최종수정 2024.6.6
     */
    public ArrayList<String> sendMainBoardCardInfo(MainBoard mainBoard) {
        //액션카드
        ArrayList <String> sendToFront = new ArrayList<String>();
        List<ActionRoundCard> actionCards = mainBoard.getActionCards();
        for (ActionRoundCard card : actionCards) {
            if (card.isRevealed()) {
                sendToFront.add("x" + String.valueOf(card.getId()));
            }
            else {
                sendToFront.add(String.valueOf(card.getId()));
            }
        }
        //라운드카드
        List<ActionRoundCard> roundCards = mainBoard.getRoundCards();
        for (ActionRoundCard card : roundCards) {
            String.valueOf(card.getId());
            if (card.isRevealed()) {
                 sendToFront.add("x" + String.valueOf(card.getId()));
            }
            else {
                sendToFront.add(String.valueOf(card.getId()));
            }
        }
        return sendToFront;
        //카드마다 플레이어가 놓을수있는지없는지 전달.
    }

    /**
     * 메인보드 위 액션카드위의 플레이어 리스트를 보냄
     * @param mainBoard 현재 진행되고 있는 게임의 Mainboard객체
     * @return ex) ["1", "2", "null", ...]
     * test필요
     * 최종수정 2024.6.6
     */
    public List<String> mainboardActioncardPlayerList(MainBoard mainBoard) {
        List<ActionRoundCard> actionRoundCards = mainBoard.getActionCards();
        List<String> sendToFront = new ArrayList<>();
        for (ActionRoundCard card : actionRoundCards) {
            if (card.isOccupied()) {
                sendToFront.add(card.getOccupiedPlayerId());
            } else {
                sendToFront.add("null");
            }
        }
        return sendToFront;
    }
    /**
     * 메인보드 위 라운드카드위의 플레이어 리스트를 보냄
     * @param mainBoard 현재 진행되고 있는 게임의 Mainboard객체
     * @return ex) ["1", "2", "null", ...]
     * test필요
     * 최종수정 2024.6.6
     */
    public List<String> mainboardRoundcardPlayerList(MainBoard mainBoard) {
        List<ActionRoundCard> roundCards = mainBoard.getRoundCards();
        List<String> sendToFront = new ArrayList<>();
        for (ActionRoundCard card : roundCards) {
            if (card.isOccupied()) {
                sendToFront.add(card.getOccupiedPlayerId());
            } else {
                sendToFront.add("null");
            }
        }
        return sendToFront;
    }

    /**
     * 메인보드 위 액션 카드위의 누적 자원 리스트 를 보냄
     * ActionRoundCard에 getPlayerId()구현필요
     * @param mainBoard 현재 진행되고 있는 게임의 Mainboard객체
     * @return ex) [["clay:1, wood:2"] , ["wood:2"] , ["null"] , ...]
     * test필요
     * 최종수정 2024.6.6
     */

    public List<ArrayList<String>> mainboardActionCardAccumulateResourceList(MainBoard mainBoard) {
        List<ActionRoundCard> actionCards = mainBoard.getActionCards();
        List<ArrayList<String>> sendToFront = new ArrayList<>();
        for (ActionRoundCard card : actionCards) {
            ArrayList<String> resource = new ArrayList<>();
            //자원 축적 카드일때
            if (card instanceof AccumulativeActionCard) {
                Map<String, Integer> accumResource = ((AccumulativeActionCard) card).getAccumulatedResources();
                for (Map.Entry<String, Integer> entry : accumResource.entrySet()) {
                    resource.add(entry.getKey() + ":" + entry.getValue());
                }
            }
            //자원 축적 카드가 아닐때
            else { //자원축적칸이 아닌 자원축적x칸일때
                if (card instanceof NonAccumulativeActionCard) {
                    //자원 축적칸이아닌 일반 자원칸
                    if (((NonAccumulativeActionCard) card).getHasResources()) {
                        Map<String, Integer> resources= ((NonAccumulativeActionCard) card).createResourcesToGain();
                        for (Map.Entry<String, Integer> entry : resources.entrySet()) {
                            resource.add(entry.getKey() + ":" + entry.getValue());
                        }
                    }
                    else {
                        resource.add("null");
                    }
                }
                else {
                    System.out.println("Invalid card type or card type mismatch.");
                }
            }
            sendToFront.add(resource);
        }
        return sendToFront;
    }

    /**
     * 메인보드 위 라운드 카드 위의 누적 자원 리스트 를 보냄
     * ActionRoundCard에 getPlayerId()구현필요
     * @param mainBoard 현재 진행되고 있는 게임의 Mainboard객체
     * @return ex) [["clay:1, wood:2"] , ["wood:2"] , ["null"] , ...]
     * test필요
     * 최종수정 2024.6.6
     */

    public List<ArrayList<String>> mainboardRoundCardAccumulateResourceList(MainBoard mainBoard) {
        List<ActionRoundCard> roundCards = mainBoard.getRoundCards();
        List<ArrayList<String>> sendToFront = new ArrayList<>();
        for (ActionRoundCard card : roundCards) {
            ArrayList<String> resource = new ArrayList<>();
            //자원 축적 카드일때
            if (card instanceof AccumulativeActionCard) {
                Map<String, Integer> accumResource = ((AccumulativeActionCard) card).getAccumulatedResources();
                for (Map.Entry<String, Integer> entry : accumResource.entrySet()) {
                    resource.add(entry.getKey() + ":" + entry.getValue());
                }
            }
            //자원 축적 카드가 아닐때
            else { //자원축적칸이 아닌 자원축적x칸일때
                if (card instanceof NonAccumulativeActionCard) {
                    //자원 축적칸이아닌 일반 자원칸
                    if (((NonAccumulativeActionCard) card).getHasResources()) {
                        Map<String, Integer> resources= ((NonAccumulativeActionCard) card).createResourcesToGain();
                        for (Map.Entry<String, Integer> entry : resources.entrySet()) {
                            resource.add(entry.getKey() + ":" + entry.getValue());
                        }
                    }
                    else {
                        resource.add("null");
                    }
                }
                else {
                    System.out.println("Invalid card type or card type mismatch.");
                }
            }
            sendToFront.add(resource);
        }
        return sendToFront;
    }
    /**
     * 구매할 수 있는 주 설비 카드 목록을 ArrayList<String>으로 보내줌.
     * 구매가능한경우 "id", 불가능한경우 "x" + "id"
     * @param mainBoard 현재 진행되고 있는 게임의 Mainboard객체
     * @return ex) ["1", "x7", "3".... ]
     * test필요
     * 최종수정 2024.6.6
     */
    public ArrayList<String> sendMainBoardMajorImprovementInfo(MainBoard mainBoard) {
        ArrayList <String> sendToFront = new ArrayList<>();
        List<CommonCard> majorCards = mainBoard.getMajorImprovementCards();
        for (CommonCard majorCard : majorCards) {
            if (majorCard instanceof MajorImprovementCard && !((MajorImprovementCard) majorCard).isPurchased()) {
                sendToFront.add(String.valueOf(majorCard.getId()));
            }
            else {
                ((MajorImprovementCard) majorCard).isPurchased();
                sendToFront.add("x"+ String.valueOf(majorCard.getId()));
            }
        }
        return sendToFront;
    }

    /**
     *메인보드의 교환 가능한 카드 목록(주설비)을 ArrayList<String>으로 보내줌.
     *교환 가능 카드인경우 "id", 불가능한경우 "x" + "id"
     * @param mainBoard 현재 진행되고 있는 게임의 Mainboard객체
     * @return ex) ["1", "x7", "3".... ]
     * test필요
     * 최종수정 2024.6.6
     */
    public ArrayList<String> sendMainBoardExchangableCardInfo(MainBoard mainBoard, Player player, ExchangeTiming timing) {
        ArrayList <String> sendToFront = new ArrayList<>();
        List<CommonCard> majorCards = mainBoard.getMajorImprovementCards();
        ArrayList<CommonCard> majorImprovementCards = (ArrayList<CommonCard>) player.getMajorImprovementCards();
        for (CommonCard card : majorImprovementCards) {
            int id = card.getId();
            //다운캐스팅 가능할때
            if (card instanceof ExchangeableCard) {
                ExchangeableCard exchangeableCard = (ExchangeableCard) card;
                //현재 교환가능한 카드 일시
                if (exchangeableCard.canExchange(timing)) {
                    sendToFront.add("id");
                }
                else {
                    sendToFront.add("x" + "id");
                }
            }
        }
        return sendToFront;
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
