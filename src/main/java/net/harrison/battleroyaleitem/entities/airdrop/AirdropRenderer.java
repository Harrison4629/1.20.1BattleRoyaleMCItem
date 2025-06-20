package net.harrison.battleroyaleitem.entities.airdrop;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.harrison.battleroyaleitem.Battleroyaleitem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class AirdropRenderer extends EntityRenderer<AirdropEntity> {
    private final AirdropModel<AirdropEntity> model;

    public AirdropRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 1.0F;
        this.model = new AirdropModel<>(context.bakeLayer(AirdropModel.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getTextureLocation(AirdropEntity pEntity) {
        return new ResourceLocation(Battleroyaleitem.MODID, "textures/entity/airdrop.png");
    }

    @Override
    public void render(AirdropEntity entity, float entityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(180 - entityYaw));

        this.model.renderToBuffer(
                poseStack,
                buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity))),
                packedLight,
                OverlayTexture.NO_OVERLAY,
                1.0F, 1.0F, 1.0F, 1.0F
        );

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);

        poseStack.popPose();
    }
}
