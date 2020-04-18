package org.joelson.mattias.turfgame.application.model;

import java.time.Instant;
import java.util.Objects;

public class TakeData extends VisitData {
    
    public TakeData(ZoneData zone, Instant when, UserData taker) {
        super(zone, when, taker);
    }
    
    public int getPph() {
        return getZone().getPph();
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
        if (obj instanceof TakeData) {
            TakeData that = (TakeData) obj;
            return Objects.equals(getZone(), that.getZone()) && Objects.equals(getWhen(), that.getWhen()) && Objects.equals(getTaker(), that.getTaker());
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(getZone(), getWhen(), getTaker());
    }
    
    @Override
    public String toString() {
        return String.format("TakeData[zone %s, when %s, taker %%s]", ModelUtil.toString(getZone()), getWhen(), ModelUtil.toString(getTaker())); //NON-NLS
    }
}
