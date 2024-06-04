package org.joelson.mattias.turfgame.application.db;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;

@Entity
@Table(name = "takes", //NON-NLS
        uniqueConstraints = @UniqueConstraint(columnNames = { "zone_id", "when_timestamp"}),  //NON-NLS
        indexes = @Index(columnList = "zone_id")) //NON-NLS
public class TakeEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private int id;
    
    @ManyToOne(optional = false)
    private ZoneEntity zone;
    
    @Column(name = "when_timestamp", nullable = false) //NON-NLS
    private Instant when;
    
    @OneToMany(cascade= CascadeType.ALL)
    @JoinColumn(name="take_id") //NON-NLS
    private ArrayList<VisitEntity> visits;
    
    public TakeEntity() {
    }
    
    public int getId() {
        return id;
    }
    
    public ZoneEntity getZone() {
        return zone;
    }
    
    public void setZone(ZoneEntity zone) {
        this.zone = Objects.requireNonNull(zone, "Zone can not be null!"); //NON-NLS
    }
    
    public Instant getWhen() {
        return when;
    }
    
    public void setWhen(Instant when) {
        this.when = Objects.requireNonNull(when, "When can not be null!"); //NON-NLS
    }
    
    public ArrayList<VisitEntity> getVisits() {
        return visits;
    }
    
    public VisitEntity getTakeVisit() {
        return visits.stream().filter(visit -> visit.getType() == VisitType.TAKE).findAny().orElseThrow();
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
        return String.format("TakeEntity[id %d, zone %s, when %s]", id, EntityUtil.toStringPart(zone), when); //NON-NLS
    }
    
    public static TakeEntity build(ZoneEntity zone, Instant when) {
        TakeEntity take = new TakeEntity();
        take.setZone(zone);
        take.setWhen(when);
        take.visits = new ArrayList<>();
        return take;
    }
}
