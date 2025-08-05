package net.harrison.battleroyaleitem.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.harrison.battleroyaleitem.Battleroyaleitem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class ArmorPlateDamageFeedbackHudOverlay {
    private static int DisplayTicks = 0;
    private static final int maxDisplayTick = 30;
    private static boolean IsBroke = false;

    private static final ResourceLocation BROKE_ARMOR_PLATE = ResourceLocation.fromNamespaceAndPath(Battleroyaleitem.MODID,
            "textures/armorplate/broke_armor_plate_feedback.png");
    private static final ResourceLocation ARMOR_PLATE = ResourceLocation.fromNamespaceAndPath(Battleroyaleitem.MODID,
            "textures/armorplate/armor_plate_feedback.png");

    public static final IGuiOverlay ARMOR_PLATE_Feedback = ((gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null) {
            return;
        }

        int y = screenHeight / 2 + 20;
        int x = screenWidth / 2 - 6;

        float alpha = (float) DisplayTicks / maxDisplayTick;

        if (alpha <= 0 ) {
            return;
        }

        //Todo:轻微抖动效果

        RenderSystem.enableBlend();

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);

        if (IsBroke) {
            guiGraphics.blit(BROKE_ARMOR_PLATE, x, y,0, 0, 12, 12,
                    12, 12);
        } else {
            guiGraphics.blit(ARMOR_PLATE, x, y,0, 0, 12, 12,
                    12, 12);
        }

    });

    public static void setDisplay(boolean isBroke) {
        IsBroke = isBroke;
        DisplayTicks = maxDisplayTick;
    }

    public static void ticks() {
        if (DisplayTicks > 0) {
            DisplayTicks--;
        }
    }
}
