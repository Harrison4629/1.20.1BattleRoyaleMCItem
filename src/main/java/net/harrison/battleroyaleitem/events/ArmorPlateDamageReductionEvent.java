package net.harrison.battleroyaleitem.events;

import net.harrison.battleroyaleitem.Battleroyaleitem;
import net.harrison.battleroyaleitem.capabilities.armorplate.NumofArmorPlate;
import net.harrison.battleroyaleitem.capabilities.armorplate.NumofArmorPlateProvider;
import net.harrison.battleroyaleitem.init.ModMessages;
import net.harrison.battleroyaleitem.networking.s2cpacket.ArmorPlateSyncS2CPacket;
import net.harrison.battleroyaleitem.particles.ParticleSummon;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Battleroyaleitem.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ArmorPlateDamageReductionEvent {

    @SubscribeEvent
    public static void onPlayerHurt(LivingHurtEvent event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        DamageSource source = event.getSource();

        if (source.is(DamageTypes.FALL) || source.is(DamageTypes.MAGIC) ||
                source.is(DamageTypes.FELL_OUT_OF_WORLD)) {
            return;
        }

        LazyOptional<NumofArmorPlate> armorCapability = player.getCapability(
                NumofArmorPlateProvider.NUMOF_ARMOR_PLATE_CAPABILITY);

        armorCapability.ifPresent(numofArmorPlate -> {
            int armorPlateCount = numofArmorPlate.getNumofArmorPlate();

            if (armorPlateCount > 0) {
                float damage = event.getAmount();
                numofArmorPlate.subHP(damage);
                event.setCanceled(true);

                if (numofArmorPlate.getNumofArmorPlate() == 0) {
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS,0.8F, 1.0F);
                    ParticleSummon.explosion(player.level(), player.getPosition(1.0F), 5);
                }

            }
            ModMessages.sendToPlayer(new ArmorPlateSyncS2CPacket(numofArmorPlate.getNumofArmorPlate()), (ServerPlayer) player);
        });
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        LazyOptional<NumofArmorPlate> armorCapability = player.getCapability(NumofArmorPlateProvider.NUMOF_ARMOR_PLATE_CAPABILITY);
        armorCapability.ifPresent(numofArmorPlate -> {
                numofArmorPlate.subAllArmorPlate();
            ModMessages.sendToPlayer(new ArmorPlateSyncS2CPacket(numofArmorPlate.getNumofArmorPlate()), (ServerPlayer) player);
        });
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }

        if (!(event.getEntity() instanceof ServerPlayer player)){
            return;
        }

        LazyOptional<NumofArmorPlate> armorCapability = player.getCapability(NumofArmorPlateProvider.NUMOF_ARMOR_PLATE_CAPABILITY);
        armorCapability.ifPresent(numofArmorPlate -> ModMessages.sendToPlayer(new ArmorPlateSyncS2CPacket(numofArmorPlate.getNumofArmorPlate()), player));


    }
}
