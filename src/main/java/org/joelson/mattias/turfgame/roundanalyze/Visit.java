package org.joelson.mattias.turfgame.roundanalyze;

import java.time.LocalDateTime;

public abstract class Visit {

    private final LocalDateTime time;
    private final int zoneId;
    private final int tp;

    protected Visit(LocalDateTime time, int zoneId, int tp) {
        this.time = time;
        this.zoneId = zoneId;
        this.tp = tp;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public int getZoneId() {
        return zoneId;
    }

    public int getTp() {
        return tp;
    }

    public int getPoints() {
        return tp;
    }
}
