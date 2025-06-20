package net.harrison.battleroyaleitem.capabilities.armorplate;


import net.minecraft.nbt.CompoundTag;

public class NumofArmorPlate {
    private int numofArmorPlate;
    private float ARMOR_PLATE_HP;
    public static final int MAX_ARMOR_PLATE = 3;
    public static final float MAX_HP_PER_ARMOR_PLATE = 10;

    public int getNumofArmorPlate() {
        return numofArmorPlate;
    }

    public void addArmorPlate(int num) {
        if (this.numofArmorPlate == MAX_ARMOR_PLATE){
            this.ARMOR_PLATE_HP = MAX_HP_PER_ARMOR_PLATE;
        }
        this.numofArmorPlate = Math.min(this.numofArmorPlate + num, MAX_ARMOR_PLATE);

    }

    private void subArmorPlate() {
        this.numofArmorPlate = Math.max(this.numofArmorPlate - 1, 0);
    }

    public void subAllArmorPlate() {
        this.numofArmorPlate = 0;
    }

    public void copyFrom(NumofArmorPlate source) {
        this.numofArmorPlate = source.numofArmorPlate;
    }

    public void subHP(float sub) {
        float excessiveDamage =  sub - this.ARMOR_PLATE_HP;
        this.ARMOR_PLATE_HP = Math.max(this.ARMOR_PLATE_HP - sub, 0);
        if (this.ARMOR_PLATE_HP == 0) {
            this.subArmorPlate();
            this.ARMOR_PLATE_HP = MAX_HP_PER_ARMOR_PLATE;
        }
        //最后一块护甲板减免一次致命伤害
        if (excessiveDamage > 0 && this.numofArmorPlate > 0) {
            subHP(excessiveDamage);
        }
    }

    public float getHP() {
        return this.ARMOR_PLATE_HP;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("numofArmorPlate", this.numofArmorPlate);
        nbt.putFloat("HP_PER_ARMOR_PLATE", this.ARMOR_PLATE_HP);
    }

    public void loadNBTData(CompoundTag nbt) {
        this.numofArmorPlate = nbt.getInt("numofArmorPlate");
        this.ARMOR_PLATE_HP = nbt.getInt("HP_PER_ARMOR_PLATE");
    }

}
