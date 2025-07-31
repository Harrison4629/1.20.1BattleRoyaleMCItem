package net.harrison.battleroyaleitem.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbsRClickItem extends Item {
    private final int cooldownTicks;

    public AbsRClickItem(Properties pProperties, int cooldownTicks) {
        super(pProperties);
        this.cooldownTicks = cooldownTicks;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        applyItem(pPlayer, pLevel);

        if (!pPlayer.isCreative()) {
            itemStack.shrink(1);
        }

        if (!pLevel.isClientSide) {
            pPlayer.getCooldowns().addCooldown(this, cooldownTicks);
        }

        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable(getTooltipTranslationKey())
                .withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(Component.translatable(getUseTooltipTranslationKey())
                .withStyle(ChatFormatting.YELLOW));
    }

    protected abstract void applyItem(Player player, Level level);
    protected abstract String getTooltipTranslationKey();
    protected abstract String getUseTooltipTranslationKey();

    protected abstract void spawnSuccessParticles(Player player, Level level);
}
