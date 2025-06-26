package net.harrison.battleroyaleitem.items.rholditem.bioRadar;

import net.harrison.basicdevtool.init.ModMessages;
import net.harrison.basicdevtool.networking.s2cpacket.PlaySoundToClientS2CPacket;
import net.harrison.battleroyaleitem.items.AbsRHoldItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.RenderUtils;

import java.util.List;
import java.util.function.Consumer;

public class BioRadarItem extends AbsRHoldItem implements GeoItem {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    public static final int USE_DURATION = 60;
    public static final int COOLDOWN_TICKS = 200;
    public static final int RANGE = 80;
    public static final int GLOW_DURATION = 150;

    public BioRadarItem(Properties properties) {
        super(properties, USE_DURATION, COOLDOWN_TICKS);
    }

    @Override
    protected boolean conditionsMet(Player player, Level level) {
        return player.getHealth() > 6.0f;
    }

    @Override
    protected void applyItem(Player player, Level level) {

        int scanned = 0;
        player.setHealth(player.getHealth() - 6.0F);

        AABB searchArea = new AABB(
                player.getX() - RANGE, player.getY() - RANGE, player.getZ() - RANGE,
                player.getX() + RANGE, player.getY() + RANGE, player.getZ() + RANGE
        );

        List<Player> otherPlayers = level.getEntitiesOfClass(Player.class, searchArea);

        for (Player otherPlayer : otherPlayers) {
            if (otherPlayer != player) {

                double distance = player.position().vectorTo(otherPlayer.position()).length();

                if (distance <= RANGE) {

                    otherPlayer.addEffect(new MobEffectInstance(MobEffects.GLOWING, GLOW_DURATION, 0, false, false));
                    ModMessages.sendToPlayer(new PlaySoundToClientS2CPacket(SoundEvents.ELDER_GUARDIAN_CURSE, 1.0F, 1.0F),
                            (ServerPlayer) otherPlayer);
                    otherPlayer.displayClientMessage(Component.translatable("item.battleroyaleitem.bio_radar.detected")
                            .withStyle(ChatFormatting.RED), true);

                    scanned++;
                }
            }
        }

        player.displayClientMessage(Component.translatable("item.battleroyaleitem.bio_radar.use_success", scanned)
                .withStyle(ChatFormatting.YELLOW), true);
    }

    @Override
    protected ParticleOptions getParticleType() {
        return ParticleTypes.SOUL;
    }

    @Override
    protected SoundEvent getFinishSound() {
        return SoundEvents.ENCHANTMENT_TABLE_USE;
    }

    @Override
    public SoundEvent getUsingSound() {
        return SoundEvents.BEACON_AMBIENT;
    }

    @Override
    protected String getUseTooShortTranslationKey() {
        return "item.battleroyaleitem.bio_radar.use_short";
    }

    @Override
    protected String getTooltipTranslationKey() {
        return "item.battleroyaleitem.bio_radar.tooltip";
    }

    @Override
    protected String getUseTooltipTranslationKey() {
        return "item.battleroyaleitem.bio_radar.tooltip.use";
    }

    @Override
    protected String getUseFailTranslationKey() {
        return "item.battleroyaleitem.bio_radar.use_fail";
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    private PlayState predicate(AnimationState animationState) {
        animationState.getController().setAnimation(RawAnimation.begin().then("animation.bio_radar.detect",
                Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public double getTick(Object itemStack) {
        return RenderUtils.getCurrentTick();
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private BioRadarItemRenderer renderer;
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    renderer = new BioRadarItemRenderer();
                }
                return this.renderer;
            }
        });
    }
}
