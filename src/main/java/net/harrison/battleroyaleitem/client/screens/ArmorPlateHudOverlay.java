package net.harrison.battleroyaleitem.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.harrison.battleroyaleitem.Battleroyaleitem;
import net.harrison.battleroyaleitem.client.ClientArmorPlateData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class ArmorPlateHudOverlay {
    private static final ResourceLocation LOADED_ARMOR_PLATE = ResourceLocation.fromNamespaceAndPath(Battleroyaleitem.MODID,
            "textures/armorplate/loaded_armor_plate.png");
    private static final ResourceLocation EMPTY_ARMOR_PLATE = ResourceLocation.fromNamespaceAndPath(Battleroyaleitem.MODID,
            "textures/armorplate/empty_armor_plate.png");

    public static final IGuiOverlay HUD_ARMOR_PLATE = ((gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player != null && (minecraft.player.isCreative() || minecraft.player.isSpectator())) {
            return;
        }

        int x = screenWidth/2;
        int y = screenHeight;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, EMPTY_ARMOR_PLATE);
        for (int i = 0; i<3; i++) {
            guiGraphics.blit(EMPTY_ARMOR_PLATE, x + 7 + (i * 27), y - 60 , 0, 0, 32, 32,
                    32, 32);
        }

        RenderSystem.setShaderTexture(0, LOADED_ARMOR_PLATE);
        for (int i = 0; i<3; i++) {
            if (ClientArmorPlateData.getArmorNum() > i) {
                guiGraphics.blit(LOADED_ARMOR_PLATE,x + 7 + (i * 27), y - 60 , 0, 0, 32, 32,
                        32, 32);

            } else {
                break;
            }
        }
    });
}
