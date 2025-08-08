package net.harrison.battleroyaleitem.data;

public class ClientArmorPlateData {
    private static int NumOfArmorPlate;
    private static float ArmorHP;

    public static void set(int armorPlateNum, float armorHP) {
        NumOfArmorPlate = armorPlateNum;
        ArmorHP = armorHP;
    }

    public static int getArmorNum() {
        return NumOfArmorPlate;
    }

    public static float getArmorHP() {
        return ArmorHP;
    }

}
