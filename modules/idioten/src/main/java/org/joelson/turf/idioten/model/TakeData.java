package org.joelson.turf.idioten.model;

import java.time.Instant;

public class TakeData extends VisitData {

    public TakeData(ZoneData zone, UserData user, Instant time) {
        super(zone, user, time);
    }

    @Override
    public String toString() {
        return String.format("TakeData[%s]", toStringPart());
    }
}
