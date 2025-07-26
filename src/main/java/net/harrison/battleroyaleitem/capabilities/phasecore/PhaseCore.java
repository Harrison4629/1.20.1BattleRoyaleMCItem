package net.harrison.battleroyaleitem.capabilities.phasecore;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

public class PhaseCore {
    private int ticksLeft;
    private final int PhaseCoreDuration = 100;
    private double iniPosX;
    private double iniPosY;
    private double iniPosZ;
    private double directionX;
    private double directionY;
    private double directionZ;
    private boolean hasTracedBack = true;


    public boolean hasTracedBack() {
        return hasTracedBack;
    }

    public void reset() {
        iniPosX = 0;
        iniPosY = 0;
        iniPosZ = 0;
        directionX = 0;
        directionY = 0;
        directionZ = 0;
        ticksLeft = 0;
        hasTracedBack = true;
    }

    public boolean timeIsUp() {
        return ticksLeft == 0;
    }

    public void activePhaseCore(Vec3 iniPos, Vec3 direction) {
        this.ticksLeft = PhaseCoreDuration;
        this.iniPosX = iniPos.x;
        this.iniPosY = iniPos.y;
        this.iniPosZ = iniPos.z;
        this.directionX = direction.x;
        this.directionY = direction.y;
        this.directionZ = direction.z;
        this.hasTracedBack = false;
    }

    public void subTicks() {
        this.ticksLeft = Math.max(this.ticksLeft - 1, 0);
    }

    public void copyFrom(PhaseCore source) {
        this.ticksLeft = source.ticksLeft;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("ticksLeft", this.ticksLeft);
        nbt.putDouble("iniPosX", this.iniPosX);
        nbt.putDouble("iniPosY", this.iniPosY);
        nbt.putDouble("iniPosZ", this.iniPosZ);
        nbt.putDouble("directionX", this.directionX);
        nbt.putDouble("directionY", this.directionY);
        nbt.putDouble("directionZ", this.directionZ);
        nbt.putBoolean("hasTracedBack", this.hasTracedBack);
    }

    public void loadNBTData(CompoundTag nbt) {
        this.ticksLeft = nbt.getInt("ticksLeft");
        this.iniPosX = nbt.getDouble("iniPosX");
        this.iniPosY = nbt.getDouble("iniPosY");
        this.iniPosZ = nbt.getDouble("iniPosZ");
        this.directionX = nbt.getDouble("directionX");
        this.directionY = nbt.getDouble("directionY");
        this.directionZ = nbt.getDouble("directionZ");
        this.hasTracedBack = nbt.getBoolean("hasTracedBack");
    }

    public Vec3 getIniPos() {
        return new Vec3(iniPosX, iniPosY, iniPosZ);
    }

    public Vec3 getDirection() {
        return new Vec3(directionX, directionY, directionZ);
    }

}
