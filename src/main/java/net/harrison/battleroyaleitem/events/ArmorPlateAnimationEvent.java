package net.harrison.battleroyaleitem.events;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.harrison.battleroyaleitem.Battleroyaleitem;
import net.harrison.battleroyaleitem.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Battleroyaleitem.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ArmorPlateAnimationEvent {

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        if (player.isUsingItem() && player.getUseItem().getItem() == ModItems.ARMOR_PLATE.get()) {
            if (event.getHand() == player.getUsedItemHand()) {
                event.setCanceled(true);

                PoseStack poseStack = event.getPoseStack();
                MultiBufferSource buffer = event.getMultiBufferSource();
                int combinedLight = event.getPackedLight();
                ItemStack itemStack = event.getItemStack();

                renderArmorPlateAnimation(player, poseStack, buffer, combinedLight, itemStack);
            }
        }
    }

    private static void renderArmorPlateAnimation(LocalPlayer player, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, ItemStack itemStack) {

        poseStack.pushPose();

        poseStack.translate(0.5F, -0.52F, -0.72F);

        float useDuration = player.getUseItem().getUseDuration();
        float remainingTicks = player.getUseItemRemainingTicks();

        float progress = 1.0F - (remainingTicks / useDuration);

        float deltaY = -0.1F;
        float deltaX = -0.4F;
        float deltaZ = -0.3F;


        if (progress < 0.4f) {
            float liftProgress = progress / 0.4f;
            float mth = Mth.sin((float) (liftProgress * Math.PI/ 2.0));
            poseStack.translate(mth * deltaX, mth * deltaY, mth * deltaZ);
            poseStack.mulPose(Axis.YP.rotationDegrees(mth * 90F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(-mth * 70F));
            poseStack.mulPose(Axis.XP.rotationDegrees(mth * 20F));
        } else if (progress < 0.5F) {
            poseStack.translate(deltaX,deltaY, deltaZ);
            poseStack.mulPose(Axis.YP.rotationDegrees(90F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(-70F));
            poseStack.mulPose(Axis.XP.rotationDegrees(20F));

        } else {
            poseStack.translate(deltaX,deltaY, deltaZ);
            poseStack.mulPose(Axis.YP.rotationDegrees(90F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(-70F));
            poseStack.mulPose(Axis.XP.rotationDegrees(20F));

            float backProgress = (progress - 0.5F) / 0.5F;
            poseStack.translate(0, -Mth.sin((float) (backProgress * Math.PI/ 2.0)), 0);
        }


        Minecraft.getInstance().getItemRenderer().renderStatic(
                itemStack,
                ItemDisplayContext.FIRST_PERSON_RIGHT_HAND,
                combinedLight,
                -1,
                poseStack,
                buffer,
                player.level(),
                player.getId()
        );

        poseStack.popPose();
    }
}
