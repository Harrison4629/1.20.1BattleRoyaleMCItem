package net.harrison.battleroyaleitem.items.rholditem;

import net.harrison.battleroyaleitem.capabilities.armorplate.ArmorPlate;
import net.harrison.battleroyaleitem.capabilities.armorplate.ArmorPlateProvider;
import net.harrison.battleroyaleitem.init.ModMessages;
import net.harrison.battleroyaleitem.items.AbsRHoldItem;
import net.harrison.battleroyaleitem.networking.s2cpacket.ArmorPlateSyncS2CPacket;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class ArmorPlateItem extends AbsRHoldItem {
    private static final int USE_DURATION = 20;
    private static final int COOLDOWN_TICKS = 10;


    public ArmorPlateItem(Properties properties) {
        super(properties, USE_DURATION, COOLDOWN_TICKS);
    }

    @Override
    protected boolean conditionsMet(Player player, Level level) {

        return player.getCapability(ArmorPlateProvider.ARMOR_PLATE_CAPABILITY).map(
                armorPlate -> armorPlate.getNumOfArmorPlate() < ArmorPlate.MAX_ARMOR_PLATE || armorPlate.getHP() < ArmorPlate.MAX_HP_PER_ARMOR_PLATE)
                .orElse(false);
    }

    @Override
    protected void applyItem(Player player, Level level) {
        player.getCapability(ArmorPlateProvider.ARMOR_PLATE_CAPABILITY).ifPresent(numofArmorPlate -> {
            numofArmorPlate.addArmorPlate(1);
            ModMessages.sendToPlayer(new ArmorPlateSyncS2CPacket(numofArmorPlate.getNumOfArmorPlate()), (ServerPlayer) player);
        });
    }

    @Override
    protected String getUseTooShortTranslationKey() {
        return "item.battleroyaleitem.armor_plate.use_short";
    }

    @Override
    protected String getTooltipTranslationKey() {
        return "item.battleroyaleitem.armor_plate.tooltip";
    }

    @Override
    protected String getUseTooltipTranslationKey() {
        return "item.battleroyaleitem.armor_plate.tooltip.use";
    }

    @Override
    protected String getUseFailTranslationKey() {
        return "item.battleroyaleitem.armor_plate.use_fail";
    }

    @Override
    public SoundEvent getUsingSound() {
        return SoundEvents.ARMOR_EQUIP_CHAIN;
    }

    @Override
    protected SoundEvent getFinishSound() {
        return SoundEvents.ARMOR_EQUIP_LEATHER;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    protected ParticleOptions getParticleType() {
        return ParticleTypes.ELECTRIC_SPARK;
    }

    @Override
    public float getVolume() {
        return 1.5F;
    }

    @Override
    public float getPitch() {
        return 1.5F;
    }
}
