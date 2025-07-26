package net.harrison.battleroyaleitem.init;

import net.harrison.battleroyaleitem.Battleroyaleitem;
import net.harrison.battleroyaleitem.effects.ChameleonEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Battleroyaleitem.MODID);

    public static final RegistryObject<MobEffect> CHAMELEON_EFFECT = MOB_EFFECTS.register("chameleon_effect",
            () -> new ChameleonEffect(MobEffectCategory.BENEFICIAL, 0x99A9B5));



    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
