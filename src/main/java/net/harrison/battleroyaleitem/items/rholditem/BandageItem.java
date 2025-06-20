package net.harrison.battleroyaleitem.items.rholditem;

import net.harrison.battleroyaleitem.items.AbsRHoldItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class BandageItem extends AbsRHoldItem {
    private static final int USE_DURATION = 20;
    private static final int HEALING_AMOUNT = 4;
    private static final int COOLDOWN_TICKS = 40;

    public BandageItem(Properties properties) {
        super(properties, USE_DURATION, COOLDOWN_TICKS);
    }

    @Override
    protected void applyItem(Player player, Level level) {
        if (!level.isClientSide) {
            player.heal(HEALING_AMOUNT);

            player.displayClientMessage(Component.translatable("item.battleroyaleitem.bandage.use_success")
                    .withStyle(ChatFormatting.GREEN), true);
        }
    }

    @Override
    protected boolean conditionsMet(Player player, Level level) {
        return player.getHealth() < player.getMaxHealth();
    }

    @Override
    protected String getUseTooShortTranslationKey() {
        return "item.battleroyaleitem.bandage.use_short";
    }

    @Override
    protected String getTooltipTranslationKey() {
        return "item.battleroyaleitem.bandage.tooltip";
    }

    @Override
    protected String getUseTooltipTranslationKey() {
        return "item.battleroyaleitem.bandage.tooltip.use";
    }

    @Override
    protected String getUseFailTranslationKey() {
        return "item.battleroyaleitem.bandage.use_failure";
    }

}
