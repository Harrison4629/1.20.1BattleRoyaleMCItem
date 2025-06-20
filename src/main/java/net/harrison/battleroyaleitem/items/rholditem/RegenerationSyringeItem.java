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
import net.minecraft.world.level.Level;

public class RegenerationSyringeItem extends AbsRHoldItem {
    private static final int USE_DURATION = 100;
    private static final int REGEN_DURATION = 400;
    private static final int COOLDOWN_TICKS = 200;

    public RegenerationSyringeItem(Properties properties) {
        super(properties, USE_DURATION, COOLDOWN_TICKS);
    }

    @Override
    protected void applyItem(Player player, Level level) {
        if (!level.isClientSide) {

            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, REGEN_DURATION, 1, false, false));

            player.displayClientMessage(Component.translatable("item.battleroyaleitem.regeneration_syringe.use_success")
                    .withStyle(ChatFormatting.YELLOW), true);
        }
    }

    @Override
    protected String getUseTooShortTranslationKey() {
        return "item.battleroyaleitem.regeneration_syringe.use_short";
    }

    @Override
    protected String getTooltipTranslationKey() {
        return "item.battleroyaleitem.regeneration_syringe.tooltip";
    }

    @Override
    protected String getUseTooltipTranslationKey() {
        return "item.battleroyaleitem.regeneration_syringe.tooltip.use";
    }

    @Override
    protected String getUseFailTranslationKey() {
        return "";
    }

    @Override
    protected SoundEvent getFinishSound() {
        return SoundEvents.PLAYER_HURT_SWEET_BERRY_BUSH;
    }

    @Override
    public SoundEvent getUsingSound() {
        return SoundEvents.DISPENSER_DISPENSE;
    }

    @Override
    public float getVolume() {
        return 0.3F;
    }

    @Override
    protected ParticleOptions getParticleType() {
        return ParticleTypes.EFFECT;
    }
}
