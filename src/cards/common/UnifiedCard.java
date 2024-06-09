package cards.common;

import enums.ExchangeTiming;
import models.Player;
/**
 * UnifiedCard는 CommonCard를 확장하여 직업 카드와 보조설비 카드의 공통 기능을 정의합니다.
 * 이 인터페이스는 자원 획득 및 데코레이션과 관련된 메서드를 제공합니다.
 */
public interface UnifiedCard extends CommonCard {

    /**
     * 플레이어에게 자원을 획득하게 합니다.
     *
     * @param player 자원을 획득할 플레이어
     */
    void gainResource(Player player);

    /**
     * 플레이어에게 효과를 적용합니다.
     * 이 메서드는 기본적으로 아무런 동작도 하지 않으며, 필요에 따라 오버라이드하여 사용할 수 있습니다.
     *
     * @param player 효과를 적용할 플레이어
     */
    default void applyEffect(Player player) {
        // 효과 적용 로직을 구현합니다.
    }
}
