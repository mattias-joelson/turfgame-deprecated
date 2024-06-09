package org.joelson.turf.application.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.joelson.turf.application.model.ZoneVisitData;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "zone_visits")
public class ZoneVisitEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @ManyToOne(optional = false)
    private UserEntity user;

    @NotNull
    @Id
    @ManyToOne(optional = false)
    private ZoneEntity zone;

    @Column(nullable = false)
    private int visits;

    public ZoneVisitEntity() {
    }

    static ZoneVisitEntity build(UserEntity user, ZoneEntity zone, int visits) {
        ZoneVisitEntity zoneVisit = new ZoneVisitEntity();
        zoneVisit.setUser(user);
        zoneVisit.setZone(zone);
        zoneVisit.setVisits(visits);
        return zoneVisit;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = Objects.requireNonNull(user, "User can not be null!");
    }

    public ZoneEntity getZone() {
        return zone;
    }

    public void setZone(ZoneEntity zone) {
        this.zone = Objects.requireNonNull(zone, "Zone can not be null");
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ZoneVisitEntity that) {
            return user.equals(that.user) && zone.equals(that.zone) && visits == that.visits;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, zone, visits);
    }

    @Override
    public String toString() {
        return String.format("ZoneVisitEntity[user %s, zone %s, visits %d]", EntityUtil.toStringPart(user),
                EntityUtil.toStringPart(zone), visits);
    }

    public ZoneVisitData toData() {
        return new ZoneVisitData(user.toData(), zone.toData(), visits);
    }
}
