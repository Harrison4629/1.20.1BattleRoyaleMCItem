package net.harrison.battleroyaleitem.capabilities.phasecore;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

public class PhaseCore {
    private int ticksLeft;
    private static final int PhaseCoreDuration = 100;
    private boolean isActive = false;
    private Vec3 initialPosition = Vec3.ZERO;
    private Vec3 direction = Vec3.ZERO;


    public void reset() {
        initialPosition = Vec3.ZERO;
        direction = Vec3.ZERO;
        ticksLeft = 0;
        isActive = false;
    }


    public void activePhaseCore(Vec3 iniPos, Vec3 direction) {
        this.ticksLeft = PhaseCoreDuration;
        this.initialPosition = iniPos;
        this.direction = direction;
        this.isActive = true;
    }

    public void subTicks() {
        this.ticksLeft = Math.max(this.ticksLeft - 1, 0);
    }

    public void copyFrom(PhaseCore source) {
        this.ticksLeft = source.ticksLeft;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public boolean isTimeUp() {
        return ticksLeft == 0;
    }

    public Vec3 getIniPos() {
        return initialPosition;
    }

    public Vec3 getDirection() {
        return direction;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("ticksLeft", this.ticksLeft);

        nbt.putDouble("iniPosX", this.initialPosition.x);
        nbt.putDouble("iniPosY", this.initialPosition.y);
        nbt.putDouble("iniPosZ", this.initialPosition.z);

        nbt.putDouble("directionX", this.direction.x);
        nbt.putDouble("directionY", this.direction.y);
        nbt.putDouble("directionZ", this.direction.z);
        nbt.putBoolean("hasTracedBack", this.isActive);
    }

    public void loadNBTData(CompoundTag nbt) {
        this.ticksLeft = nbt.getInt("ticksLeft");

        this.initialPosition = new Vec3(
                nbt.getDouble("iniPosX"),
                nbt.getDouble("iniPosY"),
                nbt.getDouble("iniPosZ"));

        this.direction = new Vec3(
                nbt.getDouble("directionX"),
                nbt.getDouble("directionY"),
                nbt.getDouble("directionZ")
        );

        this.isActive = nbt.getBoolean("hasTracedBack");
    }

}
