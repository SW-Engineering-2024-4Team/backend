package cards.common;

import enums.ExchangeTiming;
import models.Player;

public interface UnifiedCard extends CommonCard {
    void gainResource(Player player);
    void applyEffect(Player player);
}
