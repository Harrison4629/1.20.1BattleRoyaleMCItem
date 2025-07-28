package net.harrison.battleroyaleitem.events;

import net.harrison.battleroyaleitem.Battleroyaleitem;
import net.harrison.battleroyaleitem.events.costomEvents.ArmorPlateDamageEvent;
import net.harrison.battleroyaleitem.init.ModEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Battleroyaleitem.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChameleonEvent {

    @SubscribeEvent
    public static void onEntityHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        if (!event.getEntity().level().isClientSide) {
            if (entity.hasEffect(ModEffects.CHAMELEON_EFFECT.get())) {
                entity.removeEffect(ModEffects.CHAMELEON_EFFECT.get());
                entity.removeEffect(MobEffects.INVISIBILITY);
                entity.level().playSound(null, entity.getOnPos(), SoundEvents.CAT_HISS, SoundSource.NEUTRAL, 1.0F, 1.2F);

                if (entity instanceof ServerPlayer player) {
                    player.displayClientMessage(Component.translatable("item.battleroyaleitem.chameleon.effect_ended_by_hurt").withStyle(ChatFormatting.RED), true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onArmorPlateDamage(ArmorPlateDamageEvent event) {

        LivingEntity entity = event.getEntity();

        if (!event.getEntity().level().isClientSide) {
            if (entity.hasEffect(ModEffects.CHAMELEON_EFFECT.get())) {
                entity.removeEffect(ModEffects.CHAMELEON_EFFECT.get());
                entity.removeEffect(MobEffects.INVISIBILITY);
                entity.level().playSound(null, entity.getOnPos(), SoundEvents.CAT_HISS, SoundSource.NEUTRAL, 1.0F, 1.2F);

                if (entity instanceof ServerPlayer player) {
                    player.displayClientMessage(Component.translatable("item.battleroyaleitem.chameleon.effect_ended_by_hurt").withStyle(ChatFormatting.RED), true);
                }
            }
        }

    }
}
