package net.harrison.battleroyaleitem.init;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class ModKeyBinds {
    public static final String KEY_CATEGORY = "key.category.battleroyaleitem";
    public static final String KEY_BIND_STOP_PHASING = "key.battleroyaleitem.stop_phasing";

    public static final KeyMapping STOP_PHASING = new KeyMapping(KEY_BIND_STOP_PHASING, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, KEY_CATEGORY);
}
