package cards.action;

import cards.common.AccumulativeCard;
import models.Player;

import java.util.HashMap;
import java.util.Map;

public class Fishing implements AccumulativeCard {
    private int id = 8;
    private String name = "Fishing";
    private String description = "Player can get accumulated food ";
    private boolean revealed = true;
    private boolean occupied = false;
    private boolean accumulated = true;
    private Map<String, Integer> accumulatedResource = new HashMap<>();
    private Map<String, Integer> accumulatedResourceInfo = new HashMap<>();

    public Fishing() {
        initAccumulatedResources();
        initAccumulatedResourceInfo();
    }

    @Override
    //라운드 새로시작시 자원축적을 하는 로직 구현해야함
    //이함수는 플레이어가 카드에 들어왔을때 호출됨.
    //따라서 쌓인 자원을 주기만하는 역할
    //라운드 증가때마다 축적되는 메서드를 호출할수 있어야함.
    public void execute(Player player) {
        if (occupied) {
            Map<String, Integer> givingResources = getAccumulatedResources();
            for (String key : givingResources.keySet()) {
                player.addResource(key, givingResources.get(key));
            }
            clearAccumulatedResources();
        }
        else {
            accumulateResources();
        }
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    /*Action Card는 무조건 true*/
    public boolean isRevealed() {
        return true;
    }

    @Override
    public void reveal() {
        ;
    }

    @Override
    public boolean isAccumulative() {
        return accumulated;
    }

    @Override
    public boolean isOccupied() {
        return occupied;
    }

    @Override
    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    @Override
    public void initAccumulatedResources() {
        accumulatedResource.put("food", 1);
    }

    public void initAccumulatedResourceInfo() {
        accumulatedResourceInfo.put("food", 1);
    }

    @Override
    public Map<String, Integer> getAccumulatedResources() {
        return accumulatedResource;
    }

    @Override
    public Map<String, Integer> getAccumulatedResourceInfo() {
        return accumulatedResourceInfo;
    }

    //쌓인자원을 가져갔을때 0으로 초기화하는 메서드
    @Override
    public void clearAccumulatedResources() {
        accumulatedResource.put("food", 0);

    }

    //행동칸에 자원이 쌓이는 메서드
    @Override
    public void accumulateResources() {
        if (isAccumulative()) {
            Map<String, Integer> resources = getAccumulatedResources();
            Map<String, Integer> info = getAccumulatedResourceInfo();
            //resources = getAccumulatedResources();
            //info = getAccumulatedResourceInfo();
            if (resources != null) {
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
            }
        }

    }

}
