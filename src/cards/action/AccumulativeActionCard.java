package cards.action;

import cards.common.AccumulativeCard;
import models.Animal;
import models.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * 클래스 `AccumulativeActionCard`는 누적 가능한 액션 카드를 구현한 것입니다.
 * 이 카드는 라운드 동안 자원을 누적하고, 플레이어가 카드를 사용할 때 누적된 자원을 획득할 수 있습니다.
 */
public class AccumulativeActionCard implements AccumulativeCard {
    private int id;
    private String name;
    private String description;
    private boolean revealed;
    private boolean occupied;
    private Map<String, Integer> accumulatedResources;
    private Map<String, Integer> accumulatedAmounts;

    /**
     * `AccumulativeActionCard` 생성자.
     *
     * @param id                카드의 고유 식별자
     * @param name              카드의 이름
     * @param description       카드의 설명
     * @param accumulatedAmounts 매 라운드마다 누적되는 자원의 양
     */
    public AccumulativeActionCard(int id, String name, String description, Map<String, Integer> accumulatedAmounts) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.revealed = false;
        this.occupied = false;
        this.accumulatedResources = new HashMap<>();
        this.accumulatedAmounts = accumulatedAmounts;
    }

    /**
     * 플레이어에게 카드를 실행합니다.
     *
     * @param player 카드를 실행할 플레이어
     */
    @Override
    public void execute(Player player) {
        // 구현해야 할 로직
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
     * 카드가 공개되었는지 여부를 확인합니다.
     *
     * @return 카드가 공개되었으면 true, 그렇지 않으면 false
     */
    @Override
    public boolean isRevealed() {
        return revealed;
    }

    /**
     * 카드를 공개합니다.
     */
    @Override
    public void reveal() {
        revealed = true;
    }

    /**
     * 카드가 누적 가능한지 여부를 확인합니다.
     *
     * @return 누적 가능하면 true, 그렇지 않으면 false
     */
    @Override
    public boolean isAccumulative() {
        return true;
    }

    /**
     * 누적된 자원의 맵을 반환합니다.
     *
     * @return 누적된 자원의 맵
     */
    @Override
    public Map<String, Integer> getAccumulatedResources() {
        return accumulatedResources;
    }

    /**
     * 매 라운드마다 누적되는 자원의 양을 반환합니다.
     *
     * @return 누적되는 자원의 양의 맵
     */
    public Map<String, Integer> getAccumulatedAmounts() {
        return accumulatedAmounts;
    }

    /**
     * 누적된 자원을 초기화합니다.
     */
    @Override
    public void clearAccumulatedResources() {
        accumulatedResources.clear();
    }

    /**
     * 자원을 누적합니다. 카드를 점유하지 않은 경우, 지정된 자원을 누적합니다.
     */
    @Override
    public void accumulateResources() {
        if (!occupied) {
            for (Map.Entry<String, Integer> entry : accumulatedAmounts.entrySet()) {
                String resource = entry.getKey();
                int amount = entry.getValue();
                accumulatedResources.put(resource, accumulatedResources.getOrDefault(resource, 0) + amount);
            }
        } else {
            resetAccumulatedResources();
            setOccupied(false);
        }
    }

    /**
     * 누적된 자원을 초기 상태로 리셋합니다.
     */
    private void resetAccumulatedResources() {
        for (Map.Entry<String, Integer> entry : accumulatedAmounts.entrySet()) {
            accumulatedResources.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 카드가 점유되었는지 여부를 확인합니다.
     *
     * @return 카드가 점유되었으면 true, 그렇지 않으면 false
     */
    @Override
    public boolean isOccupied() {
        return occupied;
    }

    /**
     * 카드의 점유 상태를 설정합니다.
     *
     * @param occupied 점유 상태
     */
    @Override
    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    /**
     * 누적된 자원의 맵을 설정합니다.
     *
     * @param resources 누적된 자원의 맵
     */
    public void setAccumulatedResources(Map<String, Integer> resources) {
        this.accumulatedResources = resources;
    }
}
