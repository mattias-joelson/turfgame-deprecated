package org.joelson.turf.idioten.model;

final class ModelUtil {

    private ModelUtil() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }

    public static String toStringPart(PlayerData player) {
        if (player == null) {
            return "N/A";
        }
        return toStringPart(player.getId(), player.getName());
    }

    public static String toStringPart(ZoneData zone) {
        return toStringPart(zone.getId(), zone.getName());
    }

    private static String toStringPart(int id, String name) {
        return String.format("%d - %s", id, name);
    }
}
