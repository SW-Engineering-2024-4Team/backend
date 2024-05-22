package cards.common;

import java.util.Map;

public interface AccumulativeCard extends ActionRoundCard {
    Map<String, Integer> getAccumulatedResources(); // 누적된 자원을 반환하는 메서드
    void clearAccumulatedResources(); // 누적된 자원을 초기화하는 메서드

    // 자원 누적
    default void accumulateResources() {
        if (isAccumulative()) {
            Map<String, Integer> resources = getAccumulatedResources();
            for (Map.Entry<String, Integer> entry : resources.entrySet()) {
                String resource = entry.getKey();
                int amount = entry.getValue();

                if (resource.equals("sheep")) {
                    // 양 자원 누적
                    resources.put(resource, resources.getOrDefault(resource, 0) + amount);
                } else {
                    // 일반 자원 누적
                    resources.put(resource, resources.getOrDefault(resource, 0) + amount);
                }
            }
        }
    }
}
