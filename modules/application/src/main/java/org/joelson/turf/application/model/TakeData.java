package org.joelson.turf.application.model;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class TakeData extends VisitData {

    private final Duration duration;

    public TakeData(ZoneData zone, Instant when, UserData taker) {
        this(zone, when, taker, null);
    }

    public TakeData(ZoneData zone, Instant when, UserData taker, Duration duration) {
        super(zone, when, taker);
        this.duration = duration;
    }

    @Override
    public int getPph() {
        return getZone().getPph();
    }

    public boolean isOwning() {
        return duration == null;
    }

    @Override
    public Duration getDuration() {
        return (duration != null) ? duration : Duration.between(getWhen(), Instant.now());
    }

    @Override
    public String getType() {
        return "Take";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof TakeData that) {
            return Objects.equals(getZone(), that.getZone()) && Objects.equals(getWhen(), that.getWhen())
                    && Objects.equals(getTaker(), that.getTaker());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getZone(), getWhen(), getTaker());
    }

    @Override
    public String toString() {
        return String.format("TakeData[zone %s, when %s, taker %s]",
                ModelUtil.toStringPart(getZone()), getWhen(), ModelUtil.toStringPart(getTaker()));
    }
}
