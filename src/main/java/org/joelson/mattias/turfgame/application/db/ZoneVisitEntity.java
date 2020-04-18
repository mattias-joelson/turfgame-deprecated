package org.joelson.mattias.turfgame.application.db;

import com.sun.istack.NotNull;
import org.joelson.mattias.turfgame.application.model.ZoneVisitData;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "zone_visits") //NON-NLS
public class ZoneVisitEntity implements Serializable {

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

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public ZoneEntity getZone() {
        return zone;
    }

    public void setZone(ZoneEntity zone) {
        this.zone = zone;
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
        if (obj instanceof ZoneVisitEntity) {
            ZoneVisitEntity that = (ZoneVisitEntity) obj;
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
        return String.format("ZoneVisitEntity[user %s, zone %s, visits %d]", EntityUtil.toStringPart(user), EntityUtil.toStringPart(zone), visits); //NON-NLS
    }

    public ZoneVisitData toData() {
        return new ZoneVisitData(user.toData(), zone.toData(), visits);
    }

    static ZoneVisitEntity build(UserEntity user, ZoneEntity zone, int visits) {
        ZoneVisitEntity zoneVisit = new ZoneVisitEntity();
        zoneVisit.setUser(user);
        zoneVisit.setZone(zone);
        zoneVisit.setVisits(visits);
        return zoneVisit;
    }
}
