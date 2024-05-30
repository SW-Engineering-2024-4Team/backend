//package cards.factory.imp.minor;
//
//import cards.minorimprovement.MinorImprovementCard;
//import enums.ExchangeTiming;
//import models.Player;
//import enums.RoomType;
//
//import java.util.Map;
//import java.util.function.Predicate;
//
//public class KitchenRoom extends MinorImprovementCard {
//
//    public KitchenRoom(int id, ExchangeTiming exchangeTiming) {
//        super(id, name, description, null, gainResources, cost, condition, exchangeTiming, bonusPoints);
//    }
//
//    @Override
//    public void applyEffect(Player player) {
//        player.addRoundStartEffect(this);
//    }
//
//    public void onRoundStart(Player player) {
//        if (player.getPlayerBoard().getExistingRoomType().equals(RoomType.WOOD)) {
//            player.addResource("food", 1);
//        }
//    }
//}
