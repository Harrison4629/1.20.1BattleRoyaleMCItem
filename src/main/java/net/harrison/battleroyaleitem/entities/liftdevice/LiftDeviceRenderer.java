package net.harrison.battleroyaleitem.entities.liftdevice;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.harrison.battleroyaleitem.Battleroyaleitem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class LiftDeviceRenderer extends EntityRenderer<LiftDeviceEntity> {
    private final LiftDeviceModel<LiftDeviceEntity> model;

    public LiftDeviceRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.4F;
        this.model = new LiftDeviceModel<>(context.bakeLayer(LiftDeviceModel.LAYER_LOCATION));

    }

    @Override
    public void render(LiftDeviceEntity entity, float entityyaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(180 - entityyaw));

        this.model.renderToBuffer(
                poseStack,
                buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity))),
                packedLight,
                OverlayTexture.NO_OVERLAY,
                1.0F, 1.0F, 1.0F, 1.0F
        );

        super.render(entity, entityyaw, partialTick, poseStack, buffer, packedLight);
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(LiftDeviceEntity entity) {
        return new ResourceLocation(Battleroyaleitem.MODID, "textures/entity/liftdevice.png");
    }
}
