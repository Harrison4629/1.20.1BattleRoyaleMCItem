package net.harrison.battleroyaleitem.items.rholditem;

import net.harrison.battleroyaleitem.items.AbsRHoldItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ChameleonItem extends AbsRHoldItem {
    private static final int USE_DURATION = 10;
    private static final int INVISIBILITY_DURATION = 160;
    private static final int COOLDOWN_TICKS = 140;

    public static final Map<UUID, Integer> INVISIBLE_PLAYERS = new ConcurrentHashMap<>();

    public ChameleonItem(Properties properties) {
        super(properties, USE_DURATION, COOLDOWN_TICKS);
    }

    @Override
    protected void applyItem(Player player, Level level) {
        if (!level.isClientSide) {

            player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, INVISIBILITY_DURATION, 0, false, false));

            INVISIBLE_PLAYERS.put(player.getUUID(), INVISIBILITY_DURATION);

            player.displayClientMessage(Component.translatable("item.battleroyaleitem.chameleon.use_success")
                    .withStyle(ChatFormatting.GREEN), true);
        }
    }

    @Override
    protected String getUseTooShortTranslationKey() {
        return "item.battleroyaleitem.chameleon.use_short";
    }

    @Override
    protected String getTooltipTranslationKey() {
        return "item.battleroyaleitem.chameleon.tooltip";
    }

    @Override
    protected String getUseTooltipTranslationKey() {
        return "item.battleroyaleitem.chameleon.tooltip.use";
    }

    @Override
    protected String getUseFailTranslationKey() {
        return "";
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BRUSH;
    }

    @Override
    public SoundEvent getUsingSound() {
        return SoundEvents.PHANTOM_FLAP;
    }

    @Override
    protected SoundEvent getFinishSound() {
        return SoundEvents.PHANTOM_FLAP;
    }

    @Override
    protected ParticleOptions getParticleType() {
        return ParticleTypes.SNOWFLAKE;
    }
}
