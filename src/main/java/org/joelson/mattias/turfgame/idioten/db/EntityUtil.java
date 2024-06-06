package org.joelson.mattias.turfgame.idioten.db;

import java.util.function.Function;
import java.util.function.ToIntFunction;

final class EntityUtil {

    private EntityUtil() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!"); // NON-NLS
    }

    public static String toStringPart(IdiotPlayerEntity user) {
        if (user == null) {
            return "N/A";
        }
        return toStringPart(user, IdiotPlayerEntity::getId, IdiotPlayerEntity::getName);
    }

    public static String toStringPart(IdiotZoneEntity zone) {
        return toStringPart(zone, IdiotZoneEntity::getId, IdiotZoneEntity::getName);
    }

    private static <T> String toStringPart(T entity, ToIntFunction<T> id, Function<T, String> name) {
        return (entity != null) ? String.format("%d - %s", id.applyAsInt(entity), name.apply(entity)) : null;
    }
}
