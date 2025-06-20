package net.harrison.battleroyaleitem.init;

import net.harrison.battleroyaleitem.Battleroyaleitem;
import net.harrison.battleroyaleitem.entities.airdrop.AirdropEntity;
import net.harrison.battleroyaleitem.entities.airdrop.AirdropModel;
import net.harrison.battleroyaleitem.entities.airdrop.AirdropRenderer;
import net.harrison.battleroyaleitem.entities.liftdevice.LiftDeviceEntity;
import net.harrison.battleroyaleitem.entities.liftdevice.LiftDeviceModel;
import net.harrison.battleroyaleitem.entities.liftdevice.LiftDeviceRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Battleroyaleitem.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Battleroyaleitem.MODID);

    public static final RegistryObject<EntityType<LiftDeviceEntity>> LIFTDEVICE = ENTITY_TYPES.register("liftdevice",
            () -> EntityType.Builder.of(LiftDeviceEntity::new, MobCategory.MISC)
                    .sized(1.0F, 0.2F)
                    .clientTrackingRange(20)
                    .updateInterval(3)
                    .fireImmune()
                    .build(new ResourceLocation(Battleroyaleitem.MODID, "liftdevice").toString()));

    public static final RegistryObject<EntityType<AirdropEntity>> AIRDROP = ENTITY_TYPES.register("airdrop",
            () -> EntityType.Builder.of(AirdropEntity::new, MobCategory.MISC)
                    .sized(2.0F, 2.2F)
                    .clientTrackingRange(200)
                    .updateInterval(3)
                    .fireImmune()
                    .build(new ResourceLocation(Battleroyaleitem.MODID, "airdrop").toString()));



    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(AIRDROP.get(), AirdropRenderer::new);
        event.registerEntityRenderer(LIFTDEVICE.get(), LiftDeviceRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(LiftDeviceModel.LAYER_LOCATION, LiftDeviceModel::createBodyLayer);
        event.registerLayerDefinition(AirdropModel.LAYER_LOCATION, AirdropModel::createBodyLayer);
    }
}
