package skywars;

public class EngineEvents {
    private static String lastDestruction = null;

    public static void recordDestruction(Ship ship) {
        lastDestruction = "Master Ship destroyed " + formatShipName(ship);
    }

    public static String getAndClearLastDestruction() {
        String msg = lastDestruction;
        lastDestruction = null;
        return msg;
    }

    private static String formatShipName(Ship ship) {
        return ship.getClass().getSimpleName().replaceAll("([a-z])([A-Z])", "$1 $2");
    }
}
