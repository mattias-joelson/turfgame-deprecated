package org.joelson.mattias.turfgame.application.model;

import java.time.Instant;
import java.util.Objects;

public abstract class VisitData {
    
    private final ZoneData zone;
    private final Instant when;
    private final UserData taker;
    
    public VisitData(ZoneData zone, Instant when, UserData taker) {
        this.zone = Objects.requireNonNull(zone, "Zone can not be null"); //NON-NLS
        this.when = Objects.requireNonNull(when, "When can not be null"); //NON-NLS
        this.taker = Objects.requireNonNull(taker, "Taker can not be null"); //NON-NLS
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
}
