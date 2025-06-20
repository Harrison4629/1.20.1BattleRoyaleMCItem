package net.harrison.battleroyaleitem.capabilities.armorplate;

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

public class NumofArmorPlateProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static final Capability<NumofArmorPlate> NUMOF_ARMOR_PLATE_CAPABILITY = CapabilityManager.get(new CapabilityToken<NumofArmorPlate>() {
    });

    private NumofArmorPlate numofArmorPlate = null;
    private final LazyOptional<NumofArmorPlate> optional = LazyOptional.of(this::createNumofArmorPlate);

    private NumofArmorPlate createNumofArmorPlate() {
        if (this.numofArmorPlate == null) {
            this.numofArmorPlate = new NumofArmorPlate();
        }
        return this.numofArmorPlate;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == NUMOF_ARMOR_PLATE_CAPABILITY) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createNumofArmorPlate().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createNumofArmorPlate().loadNBTData(nbt);
    }
}
