package net.harrison.battleroyaleitem.capabilities.phasecore;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PhaseCoreProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static final Capability<PhaseCore> PHASE_CORE_CAPABILITY = CapabilityManager.get(new CapabilityToken<PhaseCore>() {
    });

    private PhaseCore phaseCore = null;
    private final LazyOptional<PhaseCore> optional = LazyOptional.of(this::createPhaseCore);

    private PhaseCore createPhaseCore() {
        if (this.phaseCore == null) {
            this.phaseCore = new PhaseCore();
        }
        return this.phaseCore;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PHASE_CORE_CAPABILITY) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPhaseCore().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPhaseCore().loadNBTData(nbt);
    }
}
