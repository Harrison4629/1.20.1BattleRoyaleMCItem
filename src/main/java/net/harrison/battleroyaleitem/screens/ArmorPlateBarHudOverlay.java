package net.harrison.battleroyaleitem.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.harrison.battleroyaleitem.Battleroyaleitem;
import net.harrison.battleroyaleitem.capabilities.armorplate.ArmorPlate;
import net.harrison.battleroyaleitem.data.ClientArmorPlateData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class ArmorPlateBarHudOverlay {
    private static final ResourceLocation ARMOR_PLATE_BAR = ResourceLocation.fromNamespaceAndPath(Battleroyaleitem.MODID,
            "textures/armorplate/armor_plate_bar.png");

    public static final IGuiOverlay HUD_ARMOR_PLATE_BAR = ((gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null || minecraft.player.isCreative() || minecraft.player.isSpectator()) {
            return;
        }

        final int beginX = 20;
        final int beginY = screenHeight - 38;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.9F);
        RenderSystem.enableBlend();

        int armorNum = ClientArmorPlateData.getArmorNum();
        int fullArmorNum = armorNum - 1;
        float armorHP = ClientArmorPlateData.getArmorHP();

        //绘制边框
        for (int i = 0; i < 3; i++) {
            guiGraphics.blit(ARMOR_PLATE_BAR, beginX + (i * 28), beginY + 1, 0F, 0F, 28, 5,
                    32, 32);
        }

        //绘制满的护甲
        for (int i = 0; i < fullArmorNum; i++) {
            guiGraphics.blit(ARMOR_PLATE_BAR, beginX + (i * 28), beginY + 2, 0F, 6F, 28, 3,
                    32, 32);
        }

        //绘制非满护甲
        if (armorNum != 0) {
            guiGraphics.blit(ARMOR_PLATE_BAR, beginX + fullArmorNum * 28, beginY + 2, 0F, 6F, (int) (28 * (armorHP / ArmorPlate.MAX_HP_PER_ARMOR_PLATE)), 3,
                    32, 32);
        }
    });
}
