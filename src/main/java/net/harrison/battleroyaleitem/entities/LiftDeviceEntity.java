package net.harrison.battleroyaleitem.entities;

import net.harrison.battleroyaleitem.events.LiftDeviceFallImmuneEvent;
import net.harrison.battleroyaleitem.init.ModMessages;
import net.harrison.battleroyaleitem.networking.s2cpacket.DeltaMovementSyncS2CPacket;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class LiftDeviceEntity extends Entity {
    private static final EntityDataAccessor<Float> HEALTH = SynchedEntityData.defineId(LiftDeviceEntity.class, EntityDataSerializers.FLOAT);
    private static final float MAX_HEALTH = 20.0F;
    private static final int LAST_TIME = 200;

    public LiftDeviceEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setHealth(MAX_HEALTH);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.tickCount >= LAST_TIME) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.WITHER_BREAK_BLOCK, SoundSource.BLOCKS, 0.5F, 0.6F);
            this.remove(RemovalReason.KILLED);
            return;
        }

        if (this.tickCount % 5 == 0) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 0.3F, 0.9F);
        }

        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.CLOUD, this.getX() + this.random.nextDouble() * 0.5D - 0.15D,
                    this.getY() +0.3D,
                    this.getZ() + this.random.nextDouble() * 0.5D - 0.15D,
                    0.0D, 0.5D, 0.0D);

        }

        if (this.level() instanceof ServerLevel level) {

            AABB area = new AABB(this.getX() - 0.5F, this.getY(), this.getZ() - 0.5F,
                                this.getX() + 0.5F, this.getY() + 5.0F, this.getZ() + 0.5F);

            List<ServerPlayer> playersInArea = level.getEntitiesOfClass(ServerPlayer.class, area, player -> !player.isSpectator());
            for (ServerPlayer player : playersInArea)
            {
                if (player.getDeltaMovement().y < 1.2) {
                    Vec3 speed = player.getDeltaMovement();
                    Vec3 delta = new Vec3(1.9 * speed.x, 1.2, 1.9 * speed.z);
                    player.setDeltaMovement(delta);
                    ModMessages.sendToPlayer(new DeltaMovementSyncS2CPacket(delta), player);

                    player.level().playSound(null, player.blockPosition(), SoundEvents.FIRECHARGE_USE, SoundSource.NEUTRAL,0.5F, 1.0F);
                    LiftDeviceFallImmuneEvent.setImmune(player.getUUID());
                }
            }
        }
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

    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        }

        float currentHealth = this.getHealth();
        this.setHealth(currentHealth - amount);
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.METAL_HIT, SoundSource.BLOCKS, 0.8F, 0.5F);
        return true;
    }


    @Override
    protected void defineSynchedData() {
        this.entityData.define(HEALTH, MAX_HEALTH);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        if (pCompound.contains("Health")) {
            this.setHealth(pCompound.getFloat("Health"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putFloat("Health", this.getHealth());
    }

    public void setHealth(float health) {
        this.entityData.set(HEALTH, Math.max(0.0F, Math.min(health, MAX_HEALTH)));
        if (health <= 0.0F && !this.isRemoved()) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.WITHER_BREAK_BLOCK, SoundSource.BLOCKS, 0.8F, 0.6F);
            this.remove(RemovalReason.KILLED);
        }
    }

    public float getHealth() {
        return this.entityData.get(HEALTH);
    }
}
