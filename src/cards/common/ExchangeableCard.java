package cards.common;

import enums.ExchangeTiming;
import models.Player;

import java.util.Map;

/**
 * 인터페이스 `ExchangeableCard`는 `CommonCard`를 확장하여 자원 교환 기능을 제공하는 카드의 기능을 정의합니다.
 * 이 인터페이스를 구현하는 카드는 플레이어가 자원을 교환할 수 있는 기능을 제공합니다.
 */
public interface ExchangeableCard {

    /**
     * 주어진 타이밍에 자원을 교환할 수 있는지 여부를 확인합니다.
     *
     * @param timing 자원 교환이 발생할 시점
     * @return 자원 교환이 가능하면 true, 그렇지 않으면 false
     */
    boolean canExchange(ExchangeTiming timing);

    /**
     * 플레이어가 자원을 교환할 수 있게 합니다.
     *
     * @param player       자원을 교환할 플레이어
     * @param fromResource 교환할 자원의 종류
     * @param toResource   받을 자원의 종류
     * @param amount       교환할 자원의 양
     */
    void executeExchange(Player player, String fromResource, String toResource, int amount);

    /**
     * 카드의 자원 교환 비율을 반환합니다.
     *
     * @return 자원 교환 비율을 나타내는 맵
     */
    Map<String, Integer> getExchangeRate();
}
