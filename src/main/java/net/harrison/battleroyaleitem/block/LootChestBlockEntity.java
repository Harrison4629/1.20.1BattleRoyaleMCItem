package net.harrison.battleroyaleitem.block;

import net.harrison.battleroyaleitem.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;

public class LootChestBlockEntity extends BlockEntity implements Container, MenuProvider {
    private final NonNullList<ItemStack> items = NonNullList.withSize(9, ItemStack.EMPTY);

    private ResourceLocation lootTable;
    private long lootTableSeed;

    public LootChestBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.Loot_Chest_BE.get(), pPos, pBlockState);
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        return this.items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int pSlot) {
        return pSlot >= 0 && pSlot < this.items.size() ? this.items.get(pSlot) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        return ContainerHelper.removeItem(this.items, pSlot, pAmount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        return ContainerHelper.takeItem(this.items, pSlot);
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        if (pSlot >= 0 && pSlot < this.items.size()) {
            this.items.set(pSlot, pStack);
        }
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return this.getBlockPos().getCenter().distanceToSqr(pPlayer.position()) < 64.0D;
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.loot_chest");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new ChestMenu(MenuType.GENERIC_9x1, pContainerId, pPlayerInventory, this, 1);
    }

    public void popupItem() {
        if (this.level == null || this.level.isClientSide) {
            return;
        }
        Containers.dropContents(this.level, this.getBlockPos(), this.items);
    }

    public boolean hasLootTableToBeUnpacked() {
        return this.lootTable != null;
    }

    public void unpackLootTable(Level level) {
        if (this.lootTable != null && level.getServer() != null) {

            this.clearContent();
            LootTable loottable = level.getServer().getLootData().getLootTable(this.lootTable);

            LootParams.Builder lootparams_builder =
                    new LootParams.Builder((ServerLevel)level)
                            .withParameter(LootContextParams.ORIGIN, this.worldPosition.getCenter());

            LootParams lootparams = lootparams_builder.create(LootContextParamSets.CHEST);

            loottable.fill(this, lootparams, this.lootTableSeed);

            this.lootTable = null;
            this.lootTableSeed = 0L;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        if (this.lootTable != null) {
            compound.putString("LootTable", this.lootTable.toString());
            if (this.lootTableSeed != 0L) {
                compound.putLong("LootTableSeed", this.lootTableSeed);
            }
        } else {
            ContainerHelper.saveAllItems(compound, this.items);
        }
    }

    @Override
    public void load(CompoundTag compound) {
        if (compound.contains("LootTable", 8)) {
            this.lootTable = ResourceLocation.parse(compound.getString("LootTable"));
            this.lootTableSeed = compound.getLong("LootTableSeed");
        } else {
            ContainerHelper.loadAllItems(compound, this.items);
        }
    }

    public void setLootTable(ResourceLocation lootTable, long lootTableSeed) {
        this.lootTable = lootTable;
        this.setChanged();
        this.lootTableSeed = lootTableSeed;
    }
}
