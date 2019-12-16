package org.joelson.mattias.turfgame.application.model;

import java.time.Instant;
import java.util.Objects;

public class ZonePointsHistoryDTO {
    
    private final int id;
    private final Instant from;
    private final int tp;
    private final int pph;
    
    public ZonePointsHistoryDTO(int id, Instant from, int tp, int pph) {
        this.id = id;
        this.from = from;
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
        if (obj instanceof ZonePointsHistoryDTO) {
            ZonePointsHistoryDTO that = (ZonePointsHistoryDTO) obj;
            return id == that.id && Objects.equals(from, that.from) && tp == that.tp && pph == that.pph;
        }
        return super.equals(obj);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, from);
    }
    
    @Override
    public String toString() {
        return String.format("ZonePointsHistoryDTO[id %d, from %s, tp %d, pph %d]",
                id, from, tp, pph);
    }
}
