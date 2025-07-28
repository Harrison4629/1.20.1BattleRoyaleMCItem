package net.harrison.battleroyaleitem.entities.airdrop;

import net.harrison.battleroyaleitem.init.ModParticles;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AirdropEntity extends Entity implements Container, MenuProvider{
    private static final Logger log = LoggerFactory.getLogger(AirdropEntity.class);
    private final NonNullList<ItemStack> items = NonNullList.withSize(54, ItemStack.EMPTY);

    private static final double FALL_SPEED = -0.04D; // 负值表示向下，可以调整这个值来控制速度
    private static final double TERMINAL_VELOCITY = -1; // 终端速度，防止无限加速

    private static final float AIRDROP_LUCKY_VALUE = 1.0F;
    private static final float Horizontal_Drag = 0.3F;

    private static final int MaxSmokeDuration = 1000;

    private int SmokeDuration;

    private boolean wasOnGround = false;

    private ResourceLocation lootTable;
    private long lootTableSeed;

    public AirdropEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.blocksBuilding = true;
        this.noPhysics = false;
        this.SmokeDuration = MaxSmokeDuration;
    }

    @Override
    public void tick() {
        super.tick();


        AABB aabb = this.getBoundingBox().inflate(-1E-4).move(new Vec3(0, -1E-3, 0));

        boolean isOnGround = !this.level().noCollision(this, aabb);

        Vec3 iniSpeed = this.getDeltaMovement();

        if (isOnGround) {
            this.setDeltaMovement(Horizontal_Drag * iniSpeed.x, 0.7 * iniSpeed.y, Horizontal_Drag * iniSpeed.z);

            if (SmokeDuration > 0) {
                if (this.level().isClientSide) {

                    if (this.tickCount %2 == 0) {

                        for (int i = 0; i < 3 ; i++) {

                            double spawnX = this.getX() + (this.random.nextDouble() - 0.5D) * 0.6D;
                            double spawnY = this.getY() + 2.5D;
                            double spawnZ = this.getZ() + (this.random.nextDouble() - 0.5D) * 0.6D;

                            double speedX = (this.random.nextDouble() - 0.5D) * 0.02D;
                            double speedY = 0.2D + (this.random.nextDouble() * 0.04D);
                            double speedZ = (this.random.nextDouble() - 0.5D) * 0.02D;

                            this.level().addParticle(ModParticles.AIRDROP_SMOKE_PARTICLES.get(),
                                    true,
                                    spawnX,
                                    spawnY,
                                    spawnZ,
                                    speedX,
                                    speedY,
                                    speedZ
                            );
                        }
                    }
                }
                SmokeDuration--;
            }
        } else {
            Vec3 newSpeed;

            Vec3 delta = iniSpeed.add(0, FALL_SPEED, 0);

            if (delta.y > TERMINAL_VELOCITY) {
                newSpeed = new Vec3(delta.x, delta.y, delta.z);
            } else {
                newSpeed = new Vec3(delta.x, TERMINAL_VELOCITY, delta.z);
            }
            this.setDeltaMovement(newSpeed);
        }

        if (isOnGround && !this.wasOnGround) {
            this.level().playSound(null, this.blockPosition(), SoundEvents.WOOD_PLACE, SoundSource.NEUTRAL, 3.0F, 2.0F);
        }

        this.move(MoverType.SELF, this.getDeltaMovement());

        this.wasOnGround = isOnGround;

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
