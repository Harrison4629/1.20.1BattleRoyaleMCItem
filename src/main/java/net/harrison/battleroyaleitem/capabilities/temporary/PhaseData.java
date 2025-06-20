package net.harrison.battleroyaleitem.capabilities.temporary;

import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PhaseData {
    private final Vec3 originPos;
    private final Vec3 direction;
    private int leftTicks;

    public static final Map<UUID, PhaseData> DATA = new HashMap<>();

    public PhaseData(Vec3 originPos, Vec3 direction, int leftTicks) {
        this.originPos = originPos;
        this.direction = direction;
        this.leftTicks = leftTicks;
    }

    public void modifyRemainingTick(int leftTicks) {
        this.leftTicks = leftTicks;
    }

    public int readRemainingTick() {
        return this.leftTicks;
    }

    public Vec3 readDirection() {
        return this.direction;
    }

    public Vec3 readOriginPos() {
        return this.originPos;
    }

}
