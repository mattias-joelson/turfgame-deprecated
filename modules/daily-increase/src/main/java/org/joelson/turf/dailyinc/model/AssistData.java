package org.joelson.turf.dailyinc.model;

import java.time.Instant;
import java.util.Objects;

public class AssistData extends VisitData {

    private final UserData assisted;

    public AssistData(ZoneData zone, UserData user, Instant time, UserData assisted) {
        super(zone, user, time);
        this.assisted = Objects.requireNonNull(assisted);
    }

    @Override
    public String toString() {
        return String.format("AssistData[%s, assisted=%s]", toStringPart(), assisted);
    }
}
