package net.harrison.battleroyaleitem.capabilities.armorplate;


import net.minecraft.nbt.CompoundTag;

public class ArmorPlate {
    private int numOfArmorPlate;
    private float ARMOR_PLATE_HP;
    public static final int MAX_ARMOR_PLATE = 3;
    public static final float MAX_HP_PER_ARMOR_PLATE = 10;

    public int getNumOfArmorPlate() {
        return numOfArmorPlate;
    }

    public void addArmorPlate(int num) {
        if (this.numOfArmorPlate == MAX_ARMOR_PLATE){
            this.ARMOR_PLATE_HP = MAX_HP_PER_ARMOR_PLATE;
        }
        this.numOfArmorPlate = Math.min(this.numOfArmorPlate + num, MAX_ARMOR_PLATE);

    }

    private void subArmorPlate() {
        this.numOfArmorPlate = Math.max(this.numOfArmorPlate - 1, 0);
    }

    public void subAllArmorPlate() {
        this.numOfArmorPlate = 0;
        this.ARMOR_PLATE_HP = 0;
    }

    //public void copyFrom(ArmorPlate source) {
    //    this.numOfArmorPlate = source.numOfArmorPlate;
    //}

    public void subHP(float sub) {
        float excessiveDamage =  sub - this.ARMOR_PLATE_HP;
        this.ARMOR_PLATE_HP = Math.max(this.ARMOR_PLATE_HP - sub, 0);
        if (this.ARMOR_PLATE_HP == 0) {
            this.subArmorPlate();
            this.ARMOR_PLATE_HP = MAX_HP_PER_ARMOR_PLATE;
        }
        if (excessiveDamage > 0 && this.numOfArmorPlate > 0) {
            subHP(excessiveDamage);
        }
    }

    public float getHP() {
        return this.ARMOR_PLATE_HP;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("numOfArmorPlate", this.numOfArmorPlate);
        nbt.putFloat("HP_PER_ARMOR_PLATE", this.ARMOR_PLATE_HP);
    }

    public void loadNBTData(CompoundTag nbt) {
        this.numOfArmorPlate = nbt.getInt("numOfArmorPlate");
        this.ARMOR_PLATE_HP = nbt.getInt("HP_PER_ARMOR_PLATE");
    }

}
