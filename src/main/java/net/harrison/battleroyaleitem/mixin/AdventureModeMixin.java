package net.harrison.battleroyaleitem.mixin;

import net.harrison.battleroyaleitem.items.placeitem.LiftDeviceItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerGameMode.class)
public class AdventureModeMixin {
    @Shadow
    private GameType gameModeForPlayer;

    @Shadow
    protected ServerPlayer player;

    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    private void handleSpawnEggUse(ServerPlayer serverPlayer, Level level, ItemStack itemStack,
                                   net.minecraft.world.InteractionHand hand, BlockHitResult blockHitResult,
                                   CallbackInfoReturnable<InteractionResult> cir) {

        if (itemStack.getItem() instanceof LiftDeviceItem &&
                this.gameModeForPlayer == GameType.ADVENTURE) {

            UseOnContext useOnContext =
                    new UseOnContext(serverPlayer, hand, blockHitResult);

            InteractionResult result = itemStack.getItem().useOn(useOnContext);
            cir.setReturnValue(result);
        }
    }
}
