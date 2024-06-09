package cards.common;

import java.util.Map;

/**
 * 인터페이스 `AccumulativeCard`는 `ActionRoundCard`를 확장하여 누적 자원 기능이 존재하는 액션카드, 라운드 카드의 기능을 정의합니다.
 * 이 인터페이스를 구현하는 카드는 게임 내에서 자원을 누적하고 이를 관리할 수 있습니다.
 */
public interface AccumulativeCard extends ActionRoundCard {

    /**
     * 누적된 자원의 목록을 반환합니다.
     *
     * @return 누적된 자원의 이름과 수량을 포함하는 맵
     */
    Map<String, Integer> getAccumulatedResources();

    /**
     * 누적된 자원의 수량을 반환합니다.
     *
     * @return 누적된 자원의 수량을 포함하는 맵
     */
    Map<String, Integer> getAccumulatedAmounts();

    /**
     * 누적된 자원을 초기화합니다.
     */
    void clearAccumulatedResources();

    /**
     * 자원을 누적합니다. 이 메서드는 각 라운드마다 호출되어 자원을 누적하는 역할을 합니다.
     */
    void accumulateResources();
}
