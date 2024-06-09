package org.joelson.turf.application.model;

final class ModelUtil {

    private ModelUtil() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }

    public static String toStringPart(UserData user) {
        return toStringPart(user.getId(), user.getName());
    }

    public static String toStringPart(ZoneData zone) {
        return toStringPart(zone.getId(), zone.getName());
    }

    public static String toStringPart(RegionData region) {
        return toStringPart(region.getId(), region.getName());
    }

    private static String toStringPart(int id, String name) {
        return String.format("%d - %s", id, name);
    }
}
