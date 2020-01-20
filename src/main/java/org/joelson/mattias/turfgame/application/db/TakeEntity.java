package org.joelson.mattias.turfgame.application.db;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "takes", //NON-NLS
        uniqueConstraints = @UniqueConstraint(columnNames = { "zone_id", "when_timestamp"}),  //NON-NLS
        indexes = @Index(columnList = "zone_id")) //NON-NLS
public class TakeEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private int id;
    
    @ManyToOne(optional = false)
    private ZoneEntity zone;
    
    @Column(name = "when_timestamp", nullable = false) //NON-NLS
    private Instant when;
    
    public TakeEntity() {
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public ZoneEntity getZone() {
        return zone;
    }
    
    public void setZone(ZoneEntity zone) {
        this.zone = Objects.requireNonNull(zone, "Zone can not bu null!"); //NON-NLS
    }
    
    public Instant getWhen() {
        return when;
    }
    
    public void setWhen(Instant when) {
        this.when = Objects.requireNonNull(when, "When can not be null!"); //NON-NLS
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof TakeEntity) {
            TakeEntity that = (TakeEntity) obj;
            return id == that.id && Objects.equals(zone, that.zone) && Objects.equals(when, that.when);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return id;
    }
    
    @Override
    public String toString() {
        return String.format("TakeEntity{id %d, zone %s, when %s", id, EntityUtil.toStringPart(zone), when); //NON-NLS
    }
    
    public static TakeEntity build(ZoneEntity zone, Instant when) {
        TakeEntity take = new TakeEntity();
        take.setZone(zone);
        take.setWhen(when);
        return take;
    }
}
