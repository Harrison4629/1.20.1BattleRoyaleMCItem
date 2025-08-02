package net.harrison.battleroyaleitem.data;

public class ClientArmorPlateData {
    private static int numOfArmorPlate;

    public static void set(int num) {
        numOfArmorPlate = num;
    }

    public static int getArmorNum() {
        return numOfArmorPlate;
    }
}
