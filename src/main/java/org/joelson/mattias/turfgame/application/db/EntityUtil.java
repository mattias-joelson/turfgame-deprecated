package org.joelson.mattias.turfgame.application.db;

final class EntityUtil {
    
    private EntityUtil() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }
    
    public static String toStringPart(RegionEntity region) {
        return (region != null) ? String.format("%d - %s", region.getId(), region.getName()) : null; //NON-NLS
    }

    public static String toStringPart(UserEntity user) {
        return (user != null) ? String.format("%d - %s", user.getId(), user.getName()) : null; //NON-NLS
    }

    public static String toStringPart(ZoneEntity zone) {
        return (zone != null) ? String.format("%d - %s", zone.getId(), zone.getName()) : null; //NON-NLS
    }
}
