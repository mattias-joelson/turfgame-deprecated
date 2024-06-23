package org.joelson.turf.dailyinc.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class UserVisitsData implements Serializable {

    @Serial
    private final static long serialVersionUID = 1;

    private final UserData user;
    private final Instant date;
    private final int visits;

    public UserVisitsData(UserData user, Instant date, int visits) {
        this.user = Objects.requireNonNull(user);
        this.date = Objects.requireNonNull(date);
        this.visits = visits;
    }

    public static int compareUserVisitsData(UserVisitsData o1, UserVisitsData o2) {
        int userIdDiff = o1.getUser().getId() - o2.getUser().getId();
        return (userIdDiff != 0) ? userIdDiff : o1.getDate().compareTo(o2.getDate());
    }

    public UserData getUser() {
        return user;
    }

    public Instant getDate() {
        return date;
    }

    public int getVisits() {
        return visits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof UserVisitsData that) {
            return Objects.equals(user, that.user) && Objects.equals(date, that.date);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, date);
    }

    @Override
    public String toString() {
        return String.format("UserVisitsData[user=%s, date=%s, visits=%d]", user, date, visits);
    }
}
