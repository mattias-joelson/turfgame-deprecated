package org.joelson.mattias.turfgame.application.db;

import com.sun.istack.NotNull;
import org.joelson.mattias.turfgame.application.model.ZoneDataHistoryDTO;
import org.joelson.mattias.turfgame.util.StringUtils;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "zone_data_history")
public class ZoneDataHistoryEntity implements Serializable {
    
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
    
    public ZoneDataHistoryEntity() {
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
    
    @NotNull
    public String getName() {
        return name;
    }
    
    public void setName(@NotNull String name) {
        this.name = StringUtils.requireNotNullAndNotEmpty(name, "Name can not be null", "Name can not be empty");
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
        if (obj instanceof ZoneDataHistoryEntity) {
            ZoneDataHistoryEntity that = (ZoneDataHistoryEntity) obj;
            return Objects.equals(zone, that.zone) && Objects.equals(from, that.from) && Objects.equals(name, that.name) && Objects.equals(region, that.region)
                    && Objects.equals(dateCreated, that.dateCreated) && latitude == that.latitude && longitude == that.longitude;
        }
        return super.equals(obj);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(zone, from);
    }
    
    @Override
    public String toString() {
        return String.format("ZoneEntity[zone %s, from %s, name %s, region %s, dateCreated %s, latitude %f, longitude %f]",
                zone, from, name, region, dateCreated, latitude, longitude);
    }
    
    public ZoneDataHistoryDTO toDTO() {
        return new ZoneDataHistoryDTO(zone.getId(), from, name, region.toDTO(), dateCreated, latitude, longitude);
    }
    
    static ZoneDataHistoryEntity build(ZoneEntity zone, Instant from, @NotNull String name, @NotNull RegionEntity region, @NotNull Instant dateCreated,
            double latitude, double longitude) {
        ZoneDataHistoryEntity zoneDataHistory = new ZoneDataHistoryEntity();
        zoneDataHistory.setZone(zone);
        zoneDataHistory.setFrom(from);
        zoneDataHistory.setName(name);
        zoneDataHistory.setRegion(region);
        zoneDataHistory.setDateCreated(dateCreated);
        zoneDataHistory.setLatitude(latitude);
        zoneDataHistory.setLongitude(longitude);
        return zoneDataHistory;
    }
}
