package cards.common;

import models.Player;

/**
 * 인터페이스 `BakingCard`는 `CommonCard`를 확장하여 빵굽기 기능을 제공하는 카드의 기능을 정의합니다.
 * 이 인터페이스를 구현하는 카드는 플레이어가 빵을 구울 수 있는 기능을 제공합니다.
 */
public interface BakingCard extends CommonCard {

    /**
     * 플레이어가 빵을 굽는 기능을 트리거합니다.
     *
     * @param player 빵굽기 기능을 사용할 플레이어
     */
    void triggerBreadBaking(Player player);
}
