package org.joelson.turf.application.model;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public abstract class VisitData {

    private final ZoneData zone;
    private final Instant when;
    private final UserData taker;

    protected VisitData(ZoneData zone, Instant when, UserData taker) {
        this.zone = Objects.requireNonNull(zone, "Zone can not be null");
        this.when = Objects.requireNonNull(when, "When can not be null");
        this.taker = Objects.requireNonNull(taker, "Taker can not be null");
    }

    public ZoneData getZone() {
        return zone;
    }

    public Instant getWhen() {
        return when;
    }

    public UserData getTaker() {
        return taker;
    }

    public int getTp() {
        return zone.getTp();
    }

    public int getPph() {
        return 0;
    }

    public Duration getDuration() {
        return null;
    }

    public abstract String getType();
}
