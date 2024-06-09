package cards.common;

import models.FamilyMember;
import models.Player;

import java.util.List;
import java.util.Map;

/**
 * 인터페이스 `CommonCard`는 게임 내에서 공통적으로 사용되는 카드의 기본적인 기능을 정의합니다.
 * 모든 카드는 이 인터페이스를 구현하며, 플레이어가 수행할 수 있는 다양한 액션을 execute안에서 수행합니다.
 */
public interface CommonCard {

    int getId();
    String getName();
    String getDescription();

    /**
     * 지정된 플레이어에 대해 카드를 실행합니다.
     *
     * @param player 카드를 실행할 플레이어
     */
    void execute(Player player);

    /**
     * 세 가지 경우의 수를 실행하는 유틸리티 메서드입니다.
     * 1. 액션 1 이후 액션 2
     * 2. 액션 1만
     * 3. 액션 2만
     *
     * @param player  액션을 실행할 플레이어
     * @param actions 실행할 액션 목록
     */
    default void executeAndOr(Player player, Runnable... actions) {
        for (Runnable action : actions) {
            action.run();
        }
    }

    /**
     * 두 개의 액션 중 하나를 선택하여 실행하는 유틸리티 메서드입니다.
     * 플레이어의 선택에 따라 첫 번째 액션 또는 두 번째 액션이 실행됩니다.
     *
     * @param player  액션을 실행할 플레이어
     * @param action1 첫 번째 액션
     * @param action2 두 번째 액션
     */
    default void executeOr(Player player, Runnable action1, Runnable action2) {
        boolean choice = player.chooseOption(); // 플레이어가 선택한 옵션
        if (choice) {
            action1.run();
        } else {
            action2.run();
        }
    }

    /**
     * 첫 번째 액션을 실행한 후, 선택에 따라 두 번째 액션을 실행하는 유틸리티 메서드입니다.
     *
     * @param player  액션을 실행할 플레이어
     * @param action1 첫 번째 액션
     * @param action2 두 번째 액션
     */
    default void executeThen(Player player, Runnable action1, Runnable action2) {
        action1.run();
        action2.run();
    }



    // 자원 체크 및 지불 메서드 추가
    default boolean checkResources(Player player, Map<String, Integer> resources) {
        for (Map.Entry<String, Integer> entry : resources.entrySet()) {
            if (player.getResource(entry.getKey()) < entry.getValue()) {
                return false; // 자원이 부족하면 false 반환
            }
        }
        return true; // 자원이 충분하면 true 반환
    }

    default void payResources(Player player, Map<String, Integer> resources) {
        for (Map.Entry<String, Integer> entry : resources.entrySet()) {
            player.addResource(entry.getKey(), -entry.getValue());
        }
    }
}

