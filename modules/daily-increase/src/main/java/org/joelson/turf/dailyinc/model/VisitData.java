package org.joelson.turf.dailyinc.model;

import java.time.Instant;
import java.util.Objects;

public abstract class VisitData {

    private final ZoneData zone;
    private final UserData user;
    private final Instant time;

    public VisitData(ZoneData zone, UserData user, Instant time) {
        this.zone = Objects.requireNonNull(zone);
        this.user = Objects.requireNonNull(user);
        this.time = Objects.requireNonNull(time);
    }

    public ZoneData getZone() {
        return zone;
    }

    public UserData getUser() {
        return user;
    }

    public Instant getTime() {
        return time;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof VisitData that) {
            return Objects.equals(zone, that.zone) && Objects.equals(user, that.user)
                    && Objects.equals(time, that.time);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(zone, user, time);
    }

    @Override
    public abstract String toString();

    protected String toStringPart() {
        return String.format("zone=%s, user=%s, time=%s", zone, user, time);
    }
}
