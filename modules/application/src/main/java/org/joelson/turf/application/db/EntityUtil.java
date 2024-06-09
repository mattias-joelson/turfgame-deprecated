package org.joelson.turf.application.db;

import java.util.function.Function;
import java.util.function.ToIntFunction;

final class EntityUtil {

    private EntityUtil() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }

    public static String toStringPart(RegionEntity region) {
        return toStringPart(region, RegionEntity::getId, RegionEntity::getName);
    }

    public static String toStringPart(UserEntity user) {
        return toStringPart(user, UserEntity::getId, UserEntity::getName);
    }

    public static String toStringPart(ZoneEntity zone) {
        return toStringPart(zone, ZoneEntity::getId, ZoneEntity::getName);
    }

    private static <T> String toStringPart(T entity, ToIntFunction<T> id, Function<T, String> name) {
        return (entity != null) ? String.format("%d - %s", id.applyAsInt(entity), name.apply(entity)) : null;
    }

    public static String toStringPart(MunicipalityEntity municipality) {
        return (municipality != null)
                ? String.format("%s - %s", toStringPart(municipality.getRegion()), municipality.getName())
                : null;
    }
}
