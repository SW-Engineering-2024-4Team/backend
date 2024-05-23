package cards.decorators;

import cards.common.AccumulativeCard;
import cards.common.UnifiedCard;
import enums.RoomType;
import models.MainBoard;
import models.Player;
import java.util.Map;

public abstract class UnifiedDecorator implements AccumulativeCard {
    protected AccumulativeCard decoratedCard;

    public UnifiedDecorator(AccumulativeCard decoratedCard) {
        this.decoratedCard = decoratedCard;
    }

    @Override
    public void execute(Player player) {
        decoratedCard.execute(player);
        applyAdditionalEffects(player);
    }

    @Override
    public int getId() {
        return decoratedCard.getId();
    }

    @Override
    public String getName() {
        return decoratedCard.getName();
    }

    @Override
    public String getDescription() {
        return decoratedCard.getDescription();
    }

    @Override
    public boolean isRevealed() {
        return decoratedCard.isRevealed();
    }

    @Override
    public void reveal() {
        decoratedCard.reveal();
    }

    @Override
    public boolean isAccumulative() {
        return decoratedCard.isAccumulative();
    }

    @Override
    public Map<String, Integer> getAccumulatedResources() {
        return decoratedCard.getAccumulatedResources();
    }

    @Override
    public void clearAccumulatedResources() {
        decoratedCard.clearAccumulatedResources();
    }

    @Override
    public void accumulateResources() {
        decoratedCard.accumulateResources();
    }

    @Override
    public void gainResources(Player player, Map<String, Integer> resources) {
        decoratedCard.gainResources(player, resources);
    }

    @Override
    public void useOccupationCard(Player player) {
        decoratedCard.useOccupationCard(player);
    }

    @Override
    public void useMinorImprovementCard(Player player) {
        decoratedCard.useMinorImprovementCard(player);
    }

    @Override
    public void purchaseMajorImprovementCard(Player player) {
        decoratedCard.purchaseMajorImprovementCard(player);
    }

    @Override
    public void triggerBreadBaking(Player player) {
        decoratedCard.triggerBreadBaking(player);
    }

    @Override
    public void buildHouse(Player player) {
        decoratedCard.buildHouse(player);
    }

    @Override
    public void plowField(Player player) {
        decoratedCard.plowField(player);
    }
    @Override
    public void buildBarn(Player player) {
        decoratedCard.buildBarn(player);
    }

    @Override
    public void buildFence(Player player) {
        decoratedCard.buildFence(player);
    }

    @Override
    public void plantField(Player player) {
        decoratedCard.plantField(player);
    }


    @Override
    public void becomeFirstPlayer(Player player) {
        decoratedCard.becomeFirstPlayer(player);
    }

    @Override
    public void addNewborn(Player player) {
        decoratedCard.addNewborn(player);
    }

    @Override
    public void renovateRooms(Player player) {
        decoratedCard.renovateRooms(player);
    }

    @Override
    public boolean checkResources(Player player, Map<String, Integer> resources) {
        return decoratedCard.checkResources(player, resources);
    }

    @Override
    public void payResources(Player player, Map<String, Integer> resources) {
        decoratedCard.payResources(player, resources);
    }

    @Override
    public void executeThen(Player player, Runnable action1, Runnable action2) {
        decoratedCard.executeThen(player, action1, action2);
    }

    @Override
    public void applyAdditionalEffects(Player player) {
        decoratedCard.applyAdditionalEffects(player);
    }
}
