package net.harrison.battleroyaleitem.events;

import net.harrison.battleroyaleitem.Battleroyaleitem;
import net.harrison.battleroyaleitem.items.rholditem.ChameleonItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Battleroyaleitem.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChameleonEvent {

    @SubscribeEvent
    public static void onPlayerHurt(LivingHurtEvent event) {
        if (!event.getEntity().level().isClientSide && event.getEntity() instanceof Player player) {
            if (ChameleonItem.INVISIBLE_PLAYERS.containsKey(player.getUUID())) {
                player.removeEffect(MobEffects.INVISIBILITY);

                ChameleonItem.INVISIBLE_PLAYERS.remove(player.getUUID());
                player.displayClientMessage(Component.translatable("item.battleroyaleitem.chameleon.effect_ended_by_hurt").withStyle(ChatFormatting.RED), true);

                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.playNotifySound(SoundEvents.CAT_HISS, SoundSource.PLAYERS, 0.8F, 1.2F);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.player.level().isClientSide) {
            Player player = event.player;
            if (ChameleonItem.INVISIBLE_PLAYERS.containsKey(player.getUUID())) {
                int leftTicks = ChameleonItem.INVISIBLE_PLAYERS.get(player.getUUID());

                leftTicks--;

                if (leftTicks <= 0 ) {
                    player.removeEffect(MobEffects.INVISIBILITY);

                    ChameleonItem.INVISIBLE_PLAYERS.remove(player.getUUID());

                    player.displayClientMessage(Component.translatable("item.battleroyaleitem.chameleon.effect_ended_by_time").withStyle(ChatFormatting.RED), true);

                    if (player instanceof ServerPlayer serverPlayer) {
                        serverPlayer.playNotifySound(SoundEvents.CAT_HISS, SoundSource.PLAYERS, 0.6F, 1.2F);
                    }
                } else {
                    ChameleonItem.INVISIBLE_PLAYERS.put(player.getUUID(), leftTicks);
                }
            }
        }
    }
}
