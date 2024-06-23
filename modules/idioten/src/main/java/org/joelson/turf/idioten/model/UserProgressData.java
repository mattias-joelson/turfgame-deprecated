package org.joelson.turf.idioten.model;

import org.joelson.turf.idioten.db.UserProgressType;

import java.time.Instant;
import java.util.Objects;

public class UserProgressData {

    private final UserData user;
    private final UserProgressType type;
    private final Instant date;
    private final int previousDayCompleted;
    private final int dayCompleted;
    private final Instant timeCompleted;

    public UserProgressData(UserData user, UserProgressType type, Instant date, int previousDayCompleted,
            int dayCompleted, Instant timeCompleted) {
        this.user = Objects.requireNonNull(user);
        this.type = Objects.requireNonNull(type);
        this.date = Objects.requireNonNull(date);
        this.previousDayCompleted = previousDayCompleted;
        this.dayCompleted = dayCompleted;
        this.timeCompleted = Objects.requireNonNull(timeCompleted);
    }

    public static int compareUserProgressData(UserProgressData o1, UserProgressData o2) {
        int userIdDiff = o1.getUser().getId() - o2.getUser().getId();
        if (userIdDiff != 0) {
            return userIdDiff;
        }
        int typeDiff = o1.getType().compareTo(o2.getType());
        return (typeDiff != 0) ? typeDiff : o1.getDate().compareTo(o2.getDate());
    }

    public UserData getUser() {
        return user;
    }

    public UserProgressType getType() {
        return type;
    }

    public Instant getDate() {
        return date;
    }

    public int getPreviousDayCompleted() {
        return previousDayCompleted;
    }

    public int getDayCompleted() {
        return dayCompleted;
    }

    public Instant getTimeCompleted() {
        return timeCompleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof UserProgressData that) {
            return Objects.equals(user, that.user) && type == that.type && Objects.equals(date, that.date)
                    && previousDayCompleted == that.previousDayCompleted && dayCompleted == that.dayCompleted
                    && Objects.equals(timeCompleted, that.timeCompleted);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, type, date);
    }

    @Override
    public String toString() {
        return String.format(
                "UserProgressData[user=%s, type=%s, date=%s, previousDayCompleted=%s, dayCompleted=%s, timeCompleted=%s]",
                user, type, date, previousDayCompleted, dayCompleted, timeCompleted);
    }
}
