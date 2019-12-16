package org.joelson.mattias.turfgame.application.db;

import com.sun.istack.NotNull;
import org.joelson.mattias.turfgame.application.model.ZoneDataHistoryDTO;
import org.joelson.mattias.turfgame.application.model.ZonePointsHistoryDTO;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "zone_point_history")
public class ZonePointsHistoryEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @ManyToOne(optional = false)
    private ZoneEntity zone;

    @NotNull
    @Id
    @Column(name = "from_timestamp", nullable = false)
    private Instant from;

    @Column(nullable = false)
    private int tp;

    @Column(nullable = false)
    private int pph;
    
    public ZonePointsHistoryEntity() {
    }
    
    @NotNull
    public ZoneEntity getZone() {
        return zone;
    }
    
    public void setZone(@NotNull ZoneEntity zone) {
        this.zone = Objects.requireNonNull(zone, "Zome can not be null");
    }
    
    @NotNull
    public Instant getFrom() {
        return from;
    }
    
    public void setFrom(@NotNull Instant from) {
        this.from = Objects.requireNonNull(from, "From can not be null");
    }
    
    public int getTp() {
        return tp;
    }
    
    public void setTp(int tp) {
        this.tp = tp;
    }
    
    public int getPph() {
        return pph;
    }
    
    public void setPph(int pph) {
        this.pph = pph;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ZonePointsHistoryEntity) {
            ZonePointsHistoryEntity that = (ZonePointsHistoryEntity) obj;
            return Objects.equals(zone, that.zone) && Objects.equals(from, that.from) && tp == that.tp && pph == that.pph;
        }
        return super.equals(obj);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(zone, from);
    }
    
    @Override
    public String toString() {
        return String.format("ZonePointsHistoryEntity[zone %s, from %s, tp %d, pph %d]", zone, from, tp, pph);
    }
    
    public ZonePointsHistoryDTO toDTO() {
        return new ZonePointsHistoryDTO(zone.getId(), from, tp, pph);
    }
    
    static ZonePointsHistoryEntity build(ZoneEntity zone, Instant from, int tp, int pph) {
        ZonePointsHistoryEntity zonePointsHistory = new ZonePointsHistoryEntity();
        zonePointsHistory.setZone(zone);
        zonePointsHistory.setFrom(from);
        zonePointsHistory.setTp(tp);
        zonePointsHistory.setPph(pph);
        return zonePointsHistory;
    }
}
