package org.joelson.turf.idioten.model;

import org.joelson.turf.idioten.db.VisitType;

import java.time.Instant;
import java.util.Objects;

public class VisitData {

    private final ZoneData zone;
    private final UserData user;
    private final Instant when;
    private final VisitType type;
    private final UserData assistedUser;

    public VisitData(ZoneData zone, UserData user, Instant when, VisitType type, UserData assistedUser) {
        this.zone = zone;
        this.user = user;
        this.when = when;
        this.type = type;
        this.assistedUser = assistedUser;
    }

    public ZoneData getZone() {
        return zone;
    }

    public UserData getUser() {
        return user;
    }

    public Instant getWhen() {
        return when;
    }

    public VisitType getType() {
        return type;
    }

    public UserData getAssistedUser() {
        return assistedUser;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof VisitData that) {
            return Objects.equals(zone, that.zone) && Objects.equals(user, that.user)
                    && Objects.equals(when, that.when) && Objects.equals(type, that.type)
                    && Objects.equals(assistedUser, that.assistedUser);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(zone, user, when);
    }

    @Override
    public String toString() {
        return String.format("TakeData[zone %s, user %s, when %s, type %s, assistedUser %s]",
                ModelUtil.toStringPart(zone), ModelUtil.toStringPart(user), when, type,
                ModelUtil.toStringPart(assistedUser));
    }
}
