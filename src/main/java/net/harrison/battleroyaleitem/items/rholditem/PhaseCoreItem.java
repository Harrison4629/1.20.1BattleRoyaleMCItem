package net.harrison.battleroyaleitem.items.rholditem;

import net.harrison.battleroyaleitem.capabilities.phasecore.PhaseCoreProvider;
import net.harrison.battleroyaleitem.items.AbsRHoldItem;
import net.harrison.battleroyaleitem.util.ParticleSummon;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class PhaseCoreItem extends AbsRHoldItem {
    private static final int USE_DURATION = 10;
    private static final int COOLDOWN_TICKS = 100;


    public PhaseCoreItem(Properties properties) {
        super(properties, USE_DURATION, COOLDOWN_TICKS);
    }

    @Override
    protected void applyItem(Player player, Level level) {
        if (!level.isClientSide) {

            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDERMAN_TELEPORT,
                    SoundSource.PLAYERS,1.0F, 1.0F);

            player.getCapability(PhaseCoreProvider.PHASE_CORE_CAPABILITY).ifPresent(phaseCore -> {
                phaseCore.activePhaseCore(player.getPosition(1.0F), player.getViewVector(1.0F));
            });

            player.displayClientMessage(
                    Component.translatable("item.battleroyaleitem.phase_core.use_success"),
                    true);

        }
    }

    @Override
    protected String getUseTooShortTranslationKey() {
        return "item.battleroyaleitem.phase_core.use_short";
    }

    @Override
    protected String getTooltipTranslationKey() {
        return "item.battleroyaleitem.phase_core.tooltip";
    }

    @Override
    protected String getUseTooltipTranslationKey() {
        return "item.battleroyaleitem.phase_core.tooltip.use";
    }

    @Override
    protected String getUseFailTranslationKey() {
        return "";
    }

    @Override
    protected SoundEvent getFinishSound() {
        return SoundEvents.ENDERMAN_TELEPORT;
    }

    @Override
    public SoundEvent getUsingSound() {
        return SoundEvents.PORTAL_AMBIENT;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.CROSSBOW;
    }


    @Override
    protected void spawnParticles(Player player, Level level) {
        ParticleSummon.spawnParticleCircle(level, player.getPosition(1.0F), 1, ParticleTypes.PORTAL, 30);
    }
}
