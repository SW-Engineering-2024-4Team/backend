//package cards.occupation;
//
//import cards.common.ActionRoundCard;
//import cards.common.AccumulativeCard;
//import cards.common.CommonCard;
//import cards.common.ExchangeableCard;
//import cards.decorators.occupation.MiningHammerDecorator;
//import enums.ExchangeTiming;
//import models.Player;
//
//import java.util.List;
//import java.util.Map;
//
//public class MiningHammer extends OccupationCard implements ExchangeableCard {
//    private Map<String, Integer> exchangeRate;
//
//    public MiningHammer(int id, String name, String description, Map<String, Integer> exchangeRate) {
//        super(id, name, description);
//        this.exchangeRate = exchangeRate;
//    }
//
//    @Override
//    public void gainResource(Player player) {
//        // 자원 획득 로직
//        // 예시로 목재를 2개 얻는 로직
//        player.addResource("wood", 2);
//    }
//
//    @Override
//    public void applyEffect(Player player) {
//        // 데코레이터 기능 적용
//        List<CommonCard> actionCards = player.getGameController().getMainBoard().getActionCards();
//        for (int i = 0; i < actionCards.size(); i++) {
//            ActionRoundCard actionCard = (ActionRoundCard) actionCards.get(i);
//            if (actionCard instanceof AccumulativeCard) {
//                actionCards.set(i, new MiningHammerDecorator((AccumulativeCard) actionCard));
//            }
//        }
//    }
//
//    @Override
//    public boolean canExchange(ExchangeTiming timing) {
//        // 교환 가능 여부 확인 로직
//        return timing == ExchangeTiming.ANYTIME; // 예시로 언제든지 교환 가능
//    }
//
//    @Override
//    public void executeExchange(Player player, String fromResource, String toResource, int amount) {
//        // 자원 교환 실행 로직
//        int currentAmount = player.getResource(fromResource);
//        if (currentAmount >= amount) {
//            player.addResource(fromResource, -amount);
//            player.addResource(toResource, amount * exchangeRate.get(toResource) / exchangeRate.get(fromResource));
//        }
//    }
//
//    @Override
//    public Map<String, Integer> getExchangeRate() {
//        return exchangeRate;
//    }
//}
