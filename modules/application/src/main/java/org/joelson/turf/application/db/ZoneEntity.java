package org.joelson.turf.application.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.joelson.turf.application.model.ZoneData;
import org.joelson.turf.util.StringUtil;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "zones")
public class ZoneEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(updatable = false, nullable = false)
    private int id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @ManyToOne(optional = false)
    private RegionEntity region;

    @NotNull
    @Column(nullable = false)
    private Instant dateCreated;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(nullable = false)
    private int tp;

    @Column(nullable = false)
    private int pph;

    public ZoneEntity() {
    }

    static ZoneEntity build(
            int id, @NotNull String name, @NotNull RegionEntity region, @NotNull Instant dateCreated, double latitude,
            double longitude, int tp, int pph) {
        ZoneEntity zone = new ZoneEntity();
        zone.setId(id);
        zone.setName(name);
        zone.setRegion(region);
        zone.setDateCreated(dateCreated);
        zone.setLatitude(latitude);
        zone.setLongitude(longitude);
        zone.setTp(tp);
        zone.setPph(pph);
        return zone;
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = StringUtil.requireNotNullAndNotEmpty(name, "Name can not be null", "Name can not be empty");
    }

    @NotNull
    public RegionEntity getRegion() {
        return region;
    }

    public void setRegion(RegionEntity region) {
        this.region = Objects.requireNonNull(region, "Region can not be null");
    }

    @NotNull
    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(@NotNull Instant dateCreated) {
        this.dateCreated = Objects.requireNonNull(dateCreated, "Date created can not be null");
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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
        if (this == obj) {
            return true;
        }
        if (obj instanceof ZoneEntity that) {
            return id == that.id && Objects.equals(name, that.name) && Objects.equals(region, that.region)
                    && Objects.equals(dateCreated, that.dateCreated) && latitude == that.latitude
                    && longitude == that.longitude && tp == that.tp && pph == that.pph;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return String.format(
                "ZoneEntity[id %d, name %s, region %s, dateCreated %s, latitude %f, longitude %f, tp %d, pph %d]", id,
                name, region, dateCreated, latitude, longitude, tp, pph);
    }

    public ZoneData toData() {
        return new ZoneData(id, name, region.toData(), dateCreated, latitude, longitude, tp, pph);
    }
}
