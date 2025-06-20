package net.harrison.battleroyaleitem.events;

import net.harrison.battleroyaleitem.Battleroyaleitem;
import net.harrison.battleroyaleitem.init.ModKeyBinds;
import net.harrison.battleroyaleitem.init.ModMessages;
import net.harrison.battleroyaleitem.networking.c2spacket.StopPhasingPacket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Battleroyaleitem.MODID, value = Dist.CLIENT)
public class KeyPressEvent {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();

        // 检查是否在游戏中且不在GUI界面
        if (mc.level == null || mc.screen != null) return;

        // 检查按键是否被按下
        if (ModKeyBinds.STOP_PHASING.consumeClick()) {
            // 执行按键对应的动作
            ModMessages.sendToServer(new StopPhasingPacket());
        }
    }
}
