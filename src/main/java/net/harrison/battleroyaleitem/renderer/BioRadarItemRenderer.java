package net.harrison.battleroyaleitem.renderer;

import net.harrison.battleroyaleitem.items.rholditem.BioRadarItem;
import net.harrison.battleroyaleitem.model.BioRadarItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class BioRadarItemRenderer extends GeoItemRenderer<BioRadarItem> {
    public BioRadarItemRenderer() {
        super(new BioRadarItemModel());
    }
}
