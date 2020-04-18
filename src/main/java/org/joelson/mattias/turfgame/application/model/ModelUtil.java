package org.joelson.mattias.turfgame.application.model;

final class ModelUtil {

    private ModelUtil() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!"); //NON-NLS
    }

    public static String toString(UserData user) {
        return String.format("%d - %s", user.getId(), user.getName()); //NON-NLS
    }

    public static String toString(ZoneData zone) {
        return String.format("%d - %s", zone.getId(), zone.getName()); //NON-NLS
    }
}
