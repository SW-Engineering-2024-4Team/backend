//package cards.factory.imp.occupation;
//
//import cards.common.CommonCard;
//import cards.occupation.OccupationCard;
//import cards.majorimprovement.MajorImprovementCard;
//import enums.ExchangeTiming;
//import models.Player;
//import models.MainBoard;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class MasterBuilder extends OccupationCard {
//    public MasterBuilder(int id) {
//        super(id, "숙련 벽돌공", "카드를 낼 떄 기준으로 초기 집에 추가로 지은 방의 수만큼 주요 설비의 비용중 돌을 적게 냅니다.", null, null, 1, 4, ExchangeTiming.NONE);
//    }
//
//    @Override
//    public void execute(Player player) {
//        MainBoard mainBoard = player.getGameController().getMainBoard();
//        List<CommonCard> commonCards = mainBoard.getAvailableMajorImprovementCards();
//        int additionalRooms = player.getPlayerBoard().getAdditionalRoomsCount();
//
//        if (additionalRooms > 0) {
//            for (CommonCard commonCard : commonCards) {
//                if (commonCard instanceof MajorImprovementCard) {
//                    MajorImprovementCard card = (MajorImprovementCard) commonCard;
//                    Map<String, Integer> cost = new HashMap<>(card.getPurchaseCost());
//                    if (cost.containsKey("stone")) {
//                        cost.put("stone", Math.max(0, cost.get("stone") - additionalRooms));
//                        card.setPurchaseCost(cost);
//                    }
//                }
//            }
//        }
//    }
//}
package cards.factory.imp.occupation;

import cards.common.CommonCard;
import cards.occupation.OccupationCard;
import cards.majorimprovement.MajorImprovementCard;
import enums.ExchangeTiming;
import models.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterBuilder extends OccupationCard {
    public MasterBuilder(int id) {
        super(id, "숙련 벽돌공", "주요 설비를 지을 때마다 초기 집에 추가로 지은 방의 수만큼 돌을 적게 냅니다.", null, null, 1, 4, ExchangeTiming.NONE);
    }

    @Override
    public void execute(Player player) {
        int additionalRooms = player.getPlayerBoard().getAdditionalRoomsCount();
        player.setStoneDiscount(additionalRooms);
    }
}
