package org.joelson.turf.dailyinc.model;

import java.time.Instant;

public class RevisitData extends VisitData {

    public RevisitData(ZoneData zone, UserData user, Instant time) {
        super(zone, user, time);
    }

    @Override
    public String toString() {
        return String.format("RevisitData[%s]", toStringPart());
    }
}
