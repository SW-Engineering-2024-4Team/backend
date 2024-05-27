package cards.common;

import java.util.HashMap;
import java.util.Map;

public interface AccumulativeCard extends ActionRoundCard {
    //java.lang.NullPointerException: Cannot invoke "java.util.Map.entrySet()" because "resources" is null 에러 해결
    //accumulatedResource 추가
    Map<String, Integer> accumulatedResource = new HashMap<>();
    //어느 자원이 라운드별로 축적되는지에 대한 정보
    Map<String, Integer> accumulatedResourceInfo = new HashMap<>();

    Map<String, Integer> getAccumulatedResourceInfo();

    //아래 3개의 메소드를 인터페이스 구현시 모두 사용할것
    //초기호 메서드 인터페이스 구현시 생성자클래스에서 호출
    default void initAccumulatedResources() {
        accumulatedResource.put("wood", 1);
        accumulatedResourceInfo.put("wood", 1);
    }
    // 누적된 자원을 반환하는 메서드
    default Map<String, Integer> getAccumulatedResources() {
        return accumulatedResource;
    }
    default Map<String, Integer> getAccumulatedResourcesInfo() {
        return accumulatedResourceInfo;
    }
    void clearAccumulatedResources(); // 누적된 자원을 초기화하는 메서드

    // 자원 누적 코드수정 누적이 +n이 아닌 +2n, +4n, +8n...되는경우 수정, resources 초기화시 Map<> resources = getAccumulatedResources()는 nullpoint에러발생해서 이를 해결
    default void accumulateResources() {
        if (isAccumulative()) {
            Map<String, Integer> resources = new HashMap<>();
            resources = getAccumulatedResources();
            Map<String, Integer> info = new HashMap<>();
            info = getAccumulatedResourcesInfo();
            for (Map.Entry<String, Integer> entry : resources.entrySet()) {
                String resource = entry.getKey();
                int amount = info.get(resource);
                if (resource.equals("sheep")) {
                    // 양 자원 누적
                    resources.put(resource, resources.getOrDefault(resource, 0) + amount);
                } else {
                    // 일반 자원 누적
                    resources.put(resource, resources.getOrDefault(resource, 0) + amount);
                }
            }
        }//AccumulativeCard.super.accumulateResources()사용시 에러나옴 이 코드를 복사해서 인터페이스 구현에 붙여넣기할것
         // Map<String, Integer> resources = getAccumulatedResources()를하던 위처럼하던 인터페이스는 필드값 초기화를 안해서 nullpoint 에러뜸.
    }
}
