package org.joelson.mattias.turfgame.application.model;

import java.util.Objects;

public class ZoneVisitData {

    private final UserData user;
    private final ZoneData zone;
    private final int visits;

    public ZoneVisitData(UserData user, ZoneData zone, int visits) {
        this.user = Objects.requireNonNull(user, "User can not be null"); //NON-NLS
        this.zone = Objects.requireNonNull(zone, "Zone can not be null"); //NON-NLS
        this.visits = requirePositiveVisits(visits);
    }

    public UserData getUser() {
        return user;
    }

    public ZoneData getZone() {
        return zone;
    }

    public int getVisits() {
        return visits;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ZoneVisitData) {
            ZoneVisitData that = (ZoneVisitData) obj;
            return user.equals(that.user) && zone.equals(that.zone) && visits == that.visits;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, zone, visits);
    }

    @Override
    public String toString() {
        return String.format("ZoneVisitData[user %s, zone %s, visits %d]", ModelUtil.toStringPart(user), ModelUtil.toStringPart(zone), visits); //NON-NLS
    }

    private static int requirePositiveVisits(int visits) {
        if (visits < 0) {
            throw new IllegalArgumentException("Vistits can not be negative"); //NON-NLS
        }
        return visits;
    }
}
