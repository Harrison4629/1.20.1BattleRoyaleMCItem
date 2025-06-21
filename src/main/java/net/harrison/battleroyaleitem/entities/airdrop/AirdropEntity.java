package net.harrison.battleroyaleitem.entities.airdrop;

import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;


public class AirdropEntity extends Entity implements Container, MenuProvider{
    private final NonNullList<ItemStack> items = NonNullList.withSize(54, ItemStack.EMPTY);

    private static final double FALL_SPEED = -0.05D; // 负值表示向下，可以调整这个值来控制速度
    private static final double TERMINAL_VELOCITY = -0.15D; // 终端速度，防止无限加速

    private final float AIRDROP_LUCKY_VALUE = 1.0F;

    private ResourceLocation lootTable;
    private long lootTableSeed;

    public AirdropEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.blocksBuilding = true;
        this.noPhysics = false;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.CLOUD, this.getX() + this.random.nextDouble() * 0.5D - 0.25D,
                    this.getY() +2.5D + this.random.nextDouble() * 0.5D,
                    this.getZ() + this.random.nextDouble() * 0.5D - 0.25D,
                    0.0D, 0.01D, 0.0D);
            this.level().addParticle(ParticleTypes.CLOUD, this.getX() + this.random.nextDouble() * 0.3D - 0.15D,
                    this.getY() +3.0D + this.random.nextDouble() * 0.5D,
                    this.getZ() + this.random.nextDouble() * 0.3D - 0.15D,
                    0.0D, 0.05D, 0.0D);
            this.level().addParticle(ParticleTypes.CLOUD, this.getX() + this.random.nextDouble() * 0.2D - 0.15D,
                    this.getY() +3.7D + this.random.nextDouble() * 0.5D,
                    this.getZ() + this.random.nextDouble() * 0.2D - 0.15D,
                    0.0D, 0.1D, 0.0D);

        }

        Vec3 fall = this.getDeltaMovement();
        if (this.onGround()) {
            fall = Vec3.ZERO;
        } else {
            fall = fall.add(0.0D, FALL_SPEED, 0.0D);
            if (fall.y < TERMINAL_VELOCITY) {
                fall = new Vec3(fall.x, TERMINAL_VELOCITY, fall.z);
            }

            double horizontalDrag = 0.7; // 水平阻力系数，数值越大阻力越小
            fall = new Vec3(
                    fall.x * horizontalDrag,
                    fall.y,
                    fall.z * horizontalDrag);

            if (Math.abs(fall.x) < 0.003) fall = new Vec3(0, fall.y, fall.z);
            if (Math.abs(fall.z) < 0.003) fall = new Vec3(fall.x, fall.y, 0);
        }

        this.setDeltaMovement(fall);
        this.move(MoverType.SELF, this.getDeltaMovement());

    }

    @Override
    public void stopOpen(Player pPlayer) {
        if (!this.level().isClientSide) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.SHULKER_BOX_CLOSE, SoundSource.BLOCKS, 0.3F, 1.0F);
        }
    }

    @Override
    public void startOpen(Player pPlayer) {
        if (!this.level().isClientSide) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.SHULKER_BOX_OPEN, SoundSource.BLOCKS, 0.3F, 0.7F);
        }
    }

    @Override
    public InteractionResult interact(Player pPlayer, InteractionHand pHand) {
        if (!this.level().isClientSide && pHand == InteractionHand.MAIN_HAND) {
            if (this.lootTable != null) {
                this.unpackLootTable();
            }
            pPlayer.openMenu(this);
        }
        return InteractionResult.CONSUME;
    }

    public void setLootTable(ResourceLocation lootTable, long lootTableSeed) {
        this.lootTable = lootTable;
        this.lootTableSeed = lootTableSeed;
    }

    public void unpackLootTable() {
        if (this.lootTable != null && this.level().getServer() != null) {

            LootTable loottable = this.level().getServer().getLootData().getLootTable(this.lootTable);

            LootParams.Builder lootparams_builder =
                    new LootParams.Builder((ServerLevel)this.level())
                            .withParameter(LootContextParams.ORIGIN, this.position())
                            .withLuck(AIRDROP_LUCKY_VALUE);

            LootParams lootparams = lootparams_builder.create(LootContextParamSets.CHEST);

            loottable.fill(this, lootparams, this.lootTableSeed);

            this.lootTable = null;
            this.lootTableSeed = 0L;
        }
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float pAmount) {
        if (source.is(DamageTypeTags.IS_PROJECTILE) && !this.level().isClientSide) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.SHIELD_BLOCK, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return false;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("LootTable", 8)) {
            this.lootTable = new ResourceLocation(compound.getString("LootTable"));
            this.lootTableSeed = compound.getLong("LootTableSeed");
        } else {
            ContainerHelper.loadAllItems(compound, this.items);
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
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
    public void setChanged() {

    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return this.distanceToSqr(pPlayer) < 64.0D;
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return ChestMenu.sixRows(pContainerId, pPlayerInventory, this) ;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.airdrop");
    }
}
