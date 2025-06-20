package net.harrison.battleroyaleitem.items.rholditem.bioRadar;

import net.harrison.battleroyaleitem.Battleroyaleitem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BioRadarItemModel extends GeoModel<BioRadarItem> {
    @Override
    public ResourceLocation getModelResource(BioRadarItem bioRadarItem) {
        return new ResourceLocation(Battleroyaleitem.MODID, "geo/bio_radar.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BioRadarItem bioRadarItem) {
        return new ResourceLocation(Battleroyaleitem.MODID, "textures/item/bio_radar.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BioRadarItem bioRadarItem) {
        return new ResourceLocation(Battleroyaleitem.MODID, "animations/bio_radar.animation.json");
    }
}
