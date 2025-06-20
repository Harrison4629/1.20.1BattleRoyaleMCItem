package net.harrison.battleroyaleitem.items.placeitem;

import net.harrison.battleroyaleitem.entities.liftdevice.LiftDeviceEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class LiftDeviceItem extends Item {
    private final Supplier<EntityType<?>> typeSupplier;

    public LiftDeviceItem(RegistryObject<EntityType<LiftDeviceEntity>> type, Properties properties) {
        super(properties);
        this.typeSupplier = type::get;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!(level instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        }

        ItemStack stack = context.getItemInHand();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();

        BlockPos spawnPos = pos.above();
        EntityType<?> entityType = typeSupplier.get();

        Entity entity = entityType.create(level);
        if (entity != null) {
            entity.moveTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5,
                    entity.getYRot(), entity.getXRot());
            level.addFreshEntity(entity);

            if (player == null || !player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltip, isAdvanced);
        tooltip.add(Component.translatable("item.battleroyaleitem.lift_device.tooltip")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.battleroyaleitem.lift_device.tooltip.use")
                .withStyle(ChatFormatting.BLUE));
    }
}
