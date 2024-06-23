package org.joelson.turf.idioten.db;

import java.util.function.Function;
import java.util.function.ToIntFunction;

final class EntityUtil {

    private EntityUtil() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }

    public static String toStringPart(AssistEntity assist) {
        return String.format("visit=[%s], user=[%s]", toStringPart(assist.getVisit()), toStringPart(assist.getUser()));
    }

    public static String toStringPart(UserEntity user) {
        if (user == null) {
            return "N/A";
        }
        return toStringPart(user, UserEntity::getId, UserEntity::getName);
    }

    public static String toStringPart(UserProgressEntity userProgress) {
        return String.format("user=[%s], type=%s, date=%s, previousDayCompleted=%d, dayCompleted=%d, timeCompleted=%s",
                userProgress.getUser(), userProgress.getType(), userProgress.getDate(),
                userProgress.getPreviousDayCompleted(), userProgress.getDayCompleted(),
                userProgress.getTimeCompleted());
    }

    public static String toStringPart(UserVisitsEntity userVisits) {
        return String.format("user=[%s], date=%s, visits=%d", toStringPart(userVisits.getUser()), userVisits.getDate(),
                userVisits.getVisits());
    }

    public static String toStringPart(VisitEntity visit) {
        return toStringPart(visit, VisitEntity::getId,
                v -> String.format("zone=[%s], user=[%s], time=%s, type=%s", toStringPart(visit.getZone()),
                        toStringPart(visit.getUser()), visit.getTime(), visit.getType()));
    }

    public static String toStringPart(ZoneEntity zone) {
        return toStringPart(zone, ZoneEntity::getId, ZoneEntity::getName);
    }

    private static <T> String toStringPart(T entity, ToIntFunction<T> id, Function<T, String> name) {
        return (entity != null) ? String.format("%d - %s", id.applyAsInt(entity), name.apply(entity)) : null;
    }
}
