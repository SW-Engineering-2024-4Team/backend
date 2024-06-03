package com.example.agricola.cards.factory.imp.decorators.occupation;


import com.example.agricola.cards.common.ActionRoundCard;
import com.example.agricola.cards.decorators.UnifiedDecoratorNon;
import com.example.agricola.models.Player;

public class BuildHouseDecorator extends UnifiedDecoratorNon {

    public BuildHouseDecorator(ActionRoundCard decoratedCard, Player appliedPlayer) {
        super(decoratedCard, appliedPlayer);
    }

    @Override
    public void buildHouse(Player player) {
        super.buildHouse(player);
        if (player.equals(appliedPlayer)) {
            player.addResource("food", 3);
            System.out.println("초벽질공 효과: 음식 3개를 가져옵니다.");
        }
    }

    @Override
    public void execute(Player player) {
        executeAndOr(player,
                () -> buildHouse(player),
                () -> buildBarn(player)
        );
    }
}