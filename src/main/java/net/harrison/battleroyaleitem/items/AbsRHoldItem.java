package net.harrison.battleroyaleitem.items;

import net.harrison.soundmanager.init.ModMessages;
import net.harrison.soundmanager.networking.s2cpacket.PlaySoundToClientS2CPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbsRHoldItem extends Item {
    private final int useDuration;
    private final int cooldownTicks;

    public AbsRHoldItem(Properties properties, int useDuration, int cooldownTicks) {
        super(properties);
        this.useDuration = useDuration;
        this.cooldownTicks = cooldownTicks;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemStack);
    }


    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (entity instanceof Player player) {
            int timeUsed = getUseDuration(stack) - timeLeft;

            if (timeUsed < useDuration) {
                if (!level.isClientSide) {
                    player.displayClientMessage(Component.translatable(getUseTooShortTranslationKey())
                            .withStyle(ChatFormatting.RED), true);
                }
            }
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player) {
            if (conditionsMet(player, level)) {
                if (!level.isClientSide) {
                    applyItem(player, level);
                    ModMessages.sendToPlayer(new PlaySoundToClientS2CPacket(getFinishSound(), getVolume(),
                            getPitch()), (ServerPlayer) player);

                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                    player.getCooldowns().addCooldown(this, cooldownTicks);
                }
                if (level.isClientSide) {
                    spawnParticles(player, level);
                }
            } else {
                if (!level.isClientSide) {
                    applyItemFailed(player, level);
                    ModMessages.sendToPlayer(new PlaySoundToClientS2CPacket(SoundEvents.VILLAGER_NO, 1.0F,
                            1.0F), (ServerPlayer) player);
                    player.getCooldowns().addCooldown(this, cooldownTicks);
                }
                if (level.isClientSide) {
                    spawnFailedParticles(player, level);
                }
            }
        }
        return stack;
    }





    protected boolean conditionsMet(Player player, Level level) {
        return true;
    }

    protected abstract void applyItem(Player player, Level level);

    protected void applyItemFailed(Player player, Level level) {
        player.displayClientMessage(Component.translatable(getUseFailTranslationKey())
                .withStyle(ChatFormatting.RED), true);
    }

    protected abstract String getUseTooShortTranslationKey();
    protected abstract String getTooltipTranslationKey();
    protected abstract String getUseTooltipTranslationKey();
    protected abstract String getUseFailTranslationKey();


    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable(getTooltipTranslationKey())
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable(getUseTooltipTranslationKey())
                .withStyle(ChatFormatting.YELLOW));
    }

    public float getVolume() {
        return 0.5F;
    }

    public float getPitch() {
        return 1F;
    }

    protected SoundEvent getFinishSound() {
        return SoundEvents.EXPERIENCE_ORB_PICKUP;
    }

    protected ParticleOptions getParticleType() {
        return ParticleTypes.HEART;
    }

    public SoundEvent getUsingSound() {
        return SoundEvents.WOOL_PLACE;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return useDuration;
    }

    protected void spawnParticles(Player player, Level level) {
        Vec3 position = player.position();
        for (int i = 0; i <16; i++) {
            double xOffset = (level.random.nextDouble() - 0.5) * 1.5;
            double yOffset = level.random.nextDouble() * 2.0;
            double zOffset = (level.random.nextDouble() - 0.5) * 1.5;

            level.addParticle(
                    getParticleType(),
                    position.x + xOffset,
                    position.y + yOffset,
                    position.z + zOffset,
                    0, 0.1, 0
            );
        }
    }

    protected void spawnFailedParticles(Player player, Level level) {
        Vec3 position = player.position();
        for (int i = 0; i <16; i++) {
            double xOffset = (level.random.nextDouble() - 0.5) * 1.5;
            double yOffset = level.random.nextDouble() * 2.0;
            double zOffset = (level.random.nextDouble() - 0.5) * 1.5;

            level.addParticle(
                    ParticleTypes.ANGRY_VILLAGER,
                    position.x + xOffset,
                    position.y + yOffset,
                    position.z + zOffset,
                    0, 0.1, 0
            );
        }
    }
}
