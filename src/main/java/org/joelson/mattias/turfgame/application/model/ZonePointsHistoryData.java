package org.joelson.mattias.turfgame.application.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class ZonePointsHistoryData implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final int id;
    private final Instant from;
    private final int tp;
    private final int pph;
    
    public ZonePointsHistoryData(int id, Instant from, int tp, int pph) {
        this.id = id;
        this.from = Objects.requireNonNull(from, "From can not be null"); //NON-NLS
        this.tp = tp;
        this.pph = pph;
    }
    
    public int getId() {
        return id;
    }
    
    public Instant getFrom() {
        return from;
    }
    
    public int getTp() {
        return tp;
    }
    
    public int getPph() {
        return pph;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ZonePointsHistoryData) {
            ZonePointsHistoryData that = (ZonePointsHistoryData) obj;
            return id == that.id && Objects.equals(from, that.from) && tp == that.tp && pph == that.pph;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, from);
    }
    
    @Override
    public String toString() {
        return String.format("ZonePointsHistoryData[id %d, from %s, tp %d, pph %d]", id, from, tp, pph);  //NON-NLS
    }
}
