package org.joelson.turf.application.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.joelson.turf.application.model.ZoneHistoryData;
import org.joelson.turf.util.StringUtil;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "zone_data_history")
public class ZoneHistoryEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @ManyToOne(optional = false)
    private ZoneEntity zone;

    @NotNull
    @Id
    @Column(name = "from_timestamp", nullable = false)
    private Instant from;

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

    public ZoneHistoryEntity() {
    }

    static ZoneHistoryEntity build(
            ZoneEntity zone, Instant from, @NotNull String name, @NotNull RegionEntity region,
            @NotNull Instant dateCreated, double latitude, double longitude) {
        ZoneHistoryEntity zoneDataHistory = new ZoneHistoryEntity();
        zoneDataHistory.setZone(zone);
        zoneDataHistory.setFrom(from);
        zoneDataHistory.setName(name);
        zoneDataHistory.setRegion(region);
        zoneDataHistory.setDateCreated(dateCreated);
        zoneDataHistory.setLatitude(latitude);
        zoneDataHistory.setLongitude(longitude);
        return zoneDataHistory;
    }

    @NotNull
    public ZoneEntity getZone() {
        return zone;
    }

    public void setZone(@NotNull ZoneEntity zone) {
        this.zone = Objects.requireNonNull(zone, "Zone can not be null");
    }

    @NotNull
    public Instant getFrom() {
        return from;
    }

    public void setFrom(@NotNull Instant from) {
        this.from = Objects.requireNonNull(from, "From can not be null");
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ZoneHistoryEntity that) {
            return Objects.equals(zone, that.zone) && Objects.equals(from, that.from) && Objects.equals(name, that.name)
                    && Objects.equals(region, that.region) && Objects.equals(dateCreated, that.dateCreated)
                    && latitude == that.latitude && longitude == that.longitude;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(zone, from);
    }

    @Override
    public String toString() {
        return String.format(
                "ZoneHistoryEntity[zone %s, from %s, name %s, region %s, dateCreated %s, latitude %f, longitude %f]",
                EntityUtil.toStringPart(zone), from, name, region, dateCreated, latitude, longitude);
    }

    public ZoneHistoryData toData() {
        return new ZoneHistoryData(zone.getId(), from, name, region.toData(), dateCreated, latitude, longitude);
    }
}
