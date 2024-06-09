package org.joelson.turf.application.model;

import java.time.Instant;
import java.util.Objects;

public class AssistData extends VisitData {

    private final UserData assister;

    public AssistData(ZoneData zone, Instant when, UserData taker, UserData assister) {
        super(zone, when, taker);
        this.assister = Objects.requireNonNull(assister, "Assister can not be null"); //NON_NLS
    }

    public UserData getAssister() {
        return assister;
    }

    @Override
    public String getType() {
        return "Assist";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof AssistData that) {
            return Objects.equals(getZone(), that.getZone()) && Objects.equals(getWhen(), that.getWhen())
                    && Objects.equals(getTaker(), that.getTaker()) && Objects.equals(assister, that.assister);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getZone(), getWhen(), getTaker(), assister);
    }

    @Override
    public String toString() {
        return String.format("AssistData[zone %s, when %s, taker %s, assister %s]", ModelUtil.toStringPart(getZone()),
                getWhen(), ModelUtil.toStringPart(getTaker()), ModelUtil.toStringPart(assister));
    }
}
