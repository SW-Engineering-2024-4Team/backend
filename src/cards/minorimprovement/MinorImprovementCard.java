package cards.minorimprovement;

import cards.common.ExchangeableCard;
import cards.common.UnifiedCard;
import enums.ExchangeTiming;
import models.Player;

import java.util.Map;
import java.util.function.Predicate;

/**
 * 클래스 `MinorImprovementCard`는 보조설비 카드를 구현한 것입니다.
 * 이 카드는 자원 획득, 자원 교환 및 추가 보너스 포인트 기능을 제공합니다.
 */
public class MinorImprovementCard implements UnifiedCard, ExchangeableCard {
    private int id;
    private String name;
    private String description;
    private Map<String, Integer> exchangeRate;
    private Map<String, Integer> gainResources;
    protected Map<String, Integer> cost;
    private Predicate<Player> condition;
    private ExchangeTiming exchangeTiming;
    private int bonusPoints;

    /**
     * `MinorImprovementCard` 생성자.
     *
     * @param id                카드의 고유 식별자
     * @param name              카드의 이름
     * @param description       카드의 설명
     * @param exchangeRate      자원 교환 비율
     * @param gainResources     획득할 자원의 목록과 양
     * @param cost              카드의 구매 비용
     * @param condition         카드 사용 조건
     * @param exchangeTiming    자원 교환 타이밍
     * @param bonusPoints       보너스 포인트
     */
    public MinorImprovementCard(int id, String name, String description, Map<String, Integer> exchangeRate, Map<String, Integer> gainResources, Map<String, Integer> cost, Predicate<Player> condition, ExchangeTiming exchangeTiming, int bonusPoints) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.exchangeRate = exchangeRate;
        this.gainResources = gainResources;
        this.cost = cost;
        this.condition = condition;
        this.exchangeTiming = exchangeTiming;
        this.bonusPoints = bonusPoints;
    }

    /**
     * 플레이어에게 카드를 실행합니다.
     *
     * @param player 카드를 실행할 플레이어
     */
    @Override
    public void execute(Player player) {
        if (testCondition(player)) {
            if (checkResources(player, cost)) {
                payResources(player, cost);
                applyEffect(player);
                gainResource(player);
            } else {
                // 자원이 부족하다는 메시지 표시
            }
        } else {
            // 조건을 만족하지 않는다는 메시지 표시
        }
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
     * 플레이어에게 자원을 획득하게 합니다.
     *
     * @param player 자원을 획득할 플레이어
     */
    @Override
    public void gainResource(Player player) {
        if (gainResources != null) {
            for (Map.Entry<String, Integer> entry : gainResources.entrySet()) {
                player.addResource(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 플레이어에게 효과를 적용합니다.
     * 이 메서드는 필요에 따라 오버라이드하여 사용할 수 있습니다.
     *
     * @param player 효과를 적용할 플레이어
     */
    @Override
    public void applyEffect(Player player) {
        // 구현 필요: 카드의 효과를 적용하는 로직
    }

    /**
     * 주어진 타이밍에 자원을 교환할 수 있는지 여부를 확인합니다.
     *
     * @param timing 자원 교환이 발생할 시점
     * @return 자원 교환이 가능하면 true, 그렇지 않으면 false
     */
    @Override
    public boolean canExchange(ExchangeTiming timing) {
        return exchangeRate != null && this.exchangeTiming == timing;
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
        if (canExchange(exchangeTiming)) {
            int exchangeAmount = exchangeRate.get(toResource) * amount / exchangeRate.get(fromResource);
            player.addResource(fromResource, -amount);
            player.addResource(toResource, exchangeAmount);
        }
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
     * 플레이어가 이 카드를 사용할 수 있는지 조건을 테스트합니다.
     *
     * @param player 조건을 테스트할 플레이어
     * @return 조건을 만족하면 true, 그렇지 않으면 false
     */
    public boolean testCondition(Player player) {
        return condition == null || condition.test(player);
    }

    /**
     * 카드의 보너스 포인트를 반환합니다.
     *
     * @return 보너스 포인트
     */
    public int getBonusPoints() {
        return bonusPoints;
    }

    /**
     * 카드의 구매 비용을 반환합니다.
     *
     * @return 구매 비용을 나타내는 맵
     */
    public Map<String, Integer> getPurchaseCost() {
        return cost;
    }

    /**
     * 카드의 구매 비용을 설정합니다.
     *
     * @param cost 구매 비용을 나타내는 맵
     */
    public void setPurchaseCost(Map<String, Integer> cost) {
        this.cost = cost;
    }
}
