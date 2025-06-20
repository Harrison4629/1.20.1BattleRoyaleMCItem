package net.harrison.battleroyaleitem.client;

public class ClientArmorPlateData {
    private static int numOfArmorPlate;

    public static void set(int num) {
        ClientArmorPlateData.numOfArmorPlate = num;
    }

    public static int getArmorNum() {
        return numOfArmorPlate;
    }
}
