package org.joelson.turf.application.model;

import java.time.Instant;
import java.util.Objects;

public class RevisitData extends VisitData {

    public RevisitData(ZoneData zone, Instant when, UserData user) {
        super(zone, when, user);
    }

    @Override
    public String getType() {
        return "Revisit";
    }

    @Override
    public int getTp() {
        return super.getTp() / 2;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof RevisitData that) {
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
        return String.format("RevisitData[zone %s, when %s, taker %%s]",
                ModelUtil.toStringPart(getZone()), getWhen(), ModelUtil.toStringPart(getTaker()));
    }
}
