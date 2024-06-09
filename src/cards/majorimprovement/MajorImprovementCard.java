package cards.majorimprovement;

import cards.common.BakingCard;
import cards.common.CommonCard;
import cards.common.ExchangeableCard;
import enums.ExchangeTiming;
import models.Player;

import java.util.Map;
import java.util.Objects;

/**
 * 클래스 `MajorImprovementCard`는 주요 설비 카드를 구현한 것입니다.
 * 이 카드는 자원 교환, 빵굽기 및 추가 포인트 기능을 제공합니다.
 */
public class MajorImprovementCard implements CommonCard, ExchangeableCard, BakingCard {
    private int id;
    private String name;
    private String description;
    private Map<String, Integer> exchangeRate;
    private Map<String, Integer> breadBakingExchangeRate;
    private Map<String, Integer> purchaseCost;
    private int additionalPoints;
    private boolean immediateBakingAction;
    private boolean purchased;
    private ExchangeTiming exchangeTiming;

    /**
     * `MajorImprovementCard` 생성자.
     *
     * @param id                     카드의 고유 식별자
     * @param name                   카드의 이름
     * @param description            카드의 설명
     * @param purchaseCost           카드의 구매 비용
     * @param exchangeRate           자원 교환 비율
     * @param breadBakingExchangeRate 빵굽기 교환 비율
     * @param additionalPoints       추가 포인트
     * @param immediateBakingAction  즉시 빵굽기 실행 여부
     * @param exchangeTiming         자원 교환 타이밍
     */
    public MajorImprovementCard(int id, String name, String description,
                                Map<String, Integer> purchaseCost,
                                Map<String, Integer> exchangeRate,
                                Map<String, Integer> breadBakingExchangeRate,
                                int additionalPoints,
                                boolean immediateBakingAction,
                                ExchangeTiming exchangeTiming) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.purchaseCost = purchaseCost;
        this.exchangeRate = exchangeRate;
        this.breadBakingExchangeRate = breadBakingExchangeRate;
        this.additionalPoints = additionalPoints;
        this.immediateBakingAction = immediateBakingAction;
        this.purchased = false;
        this.exchangeTiming = exchangeTiming;
    }

    /**
     * 플레이어에게 카드를 실행합니다.
     *
     * @param player 카드를 실행할 플레이어
     */
    @Override
    public void execute(Player player) {
        purchase(player);
    }

    /**
     * 플레이어가 이 카드를 구매합니다.
     *
     * @param player 카드를 구매할 플레이어
     * @return 구매 성공 여부
     */
    public boolean purchase(Player player) {
        Map<String, Integer> cost = player.getDiscountedCost(this.purchaseCost);
        if (player.checkResources(cost)) {
            player.payResources(cost);
            player.addMajorImprovementCard(this);
            this.purchased = true;
            return true;
        } else {
            System.out.println("Not enough resources to purchase " + name);
            return false;
        }
    }

    /**
     * 카드가 구매되었는지 여부를 반환합니다.
     *
     * @return 구매되었으면 true, 그렇지 않으면 false
     */
    public boolean isPurchased() {
        return purchased;
    }

    /**
     * 카드의 구매 상태를 설정합니다.
     *
     * @param purchased 구매 상태
     */
    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    /**
     * 카드의 고유 식별자를 반환합니다.
     *
     * @return 카드의 고유 식별자
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * 카드의 이름을 반환합니다.
     *
     * @return 카드의 이름
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * 카드의 설명을 반환합니다.
     *
     * @return 카드의 설명
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * 카드의 구매 비용을 반환합니다.
     *
     * @return 구매 비용
     */
    public Map<String, Integer> getPurchaseCost() {
        return purchaseCost;
    }

    /**
     * 카드의 추가 포인트를 반환합니다.
     *
     * @return 추가 포인트
     */
    public int getAdditionalPoints() {
        return additionalPoints;
    }

    /**
     * 카드가 즉시 빵굽기 액션을 제공하는지 여부를 반환합니다.
     *
     * @return 즉시 빵굽기 액션을 제공하면 true, 그렇지 않으면 false
     */
    public boolean hasImmediateBakingAction() {
        return immediateBakingAction;
    }

    /**
     * 자원 교환 타이밍을 반환합니다.
     *
     * @return 자원 교환 타이밍
     */
    public ExchangeTiming getExchangeTiming() {
        return exchangeTiming;
    }

    /**
     * 주어진 타이밍에 자원을 교환할 수 있는지 여부를 확인합니다.
     *
     * @param timing 자원 교환이 발생할 시점
     * @return 자원 교환이 가능하면 true, 그렇지 않으면 false
     */
    @Override
    public boolean canExchange(ExchangeTiming timing) {
        return (timing == ExchangeTiming.ANYTIME || timing == this.exchangeTiming);
    }

    /**
     * 플레이어가 자원을 교환할 수 있게 합니다.
     *
     * @param player       자원을 교환할 플레이어
     * @param fromResource 교환할 자원의 종류
     * @param toResource   받을 자원의 종류
     * @param amount       교환할 자원의 양
     */
    @Override
    public void executeExchange(Player player, String fromResource, String toResource, int amount) {
        if (exchangeRate == null) {
            System.out.println("일반적인 교환 기능을 제공하지 않음. 빵굽기만 가능");
            return;
        }
        int exchangeAmount = exchangeRate.get(toResource) * amount / exchangeRate.get(fromResource);
        player.addResource(fromResource, -amount);
        player.addResource(toResource, exchangeAmount);
    }

    /**
     * 카드의 자원 교환 비율을 반환합니다.
     *
     * @return 자원 교환 비율을 나타내는 맵
     */
    @Override
    public Map<String, Integer> getExchangeRate() {
        return exchangeRate;
    }

    /**
     * 플레이어가 빵을 굽는 기능을 트리거합니다.
     *
     * @param player 빵굽기 기능을 사용할 플레이어
     */
    @Override
    public void triggerBreadBaking(Player player) {
        if (breadBakingExchangeRate == null) return;

        int availableGrain = player.getResource("grain");
        if (availableGrain > 0) {
            int amount = player.selectGrainForBaking(availableGrain);

            if (amount > 0) {
                int exchangeAmount = breadBakingExchangeRate.get("food") * amount / breadBakingExchangeRate.get("grain");
                player.addResource("grain", -amount);
                player.addResource("food", exchangeAmount);
            }
        }
    }

    /**
     * 카드의 구매 비용을 설정합니다.
     *
     * @param purchaseCost 구매 비용
     */
    public void setPurchaseCost(Map<String, Integer> purchaseCost) {
        this.purchaseCost = purchaseCost;
    }
}
