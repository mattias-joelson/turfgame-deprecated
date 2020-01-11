package org.joelson.mattias.turfgame.application.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class ZoneHistoryData implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final int id;
    private final Instant from;
    private final String name;
    private final RegionData region;
    private final Instant dateCreated;
    private final double latitude;
    private final double longitude;
    
    public ZoneHistoryData(int id, Instant from, String name, RegionData region, Instant dateCreated, double latitude, double longitude) {
        this.id = id;
        this.from = from;
        this.name = name;
        this.region = region;
        this.dateCreated = dateCreated;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    public int getId() {
        return id;
    }
    
    public Instant getFrom() {
        return from;
    }
    
    public String getName() {
        return name;
    }
    
    public RegionData getRegion() {
        return region;
    }
    
    public Instant getDateCreated() {
        return dateCreated;
    }
    
    public double getLatitude() {
        return latitude;
    }
    
    public double getLongitude() {
        return longitude;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ZoneHistoryData) {
            ZoneHistoryData that = (ZoneHistoryData) obj;
            return id == that.id && Objects.equals(name, that.name) && Objects.equals(from, that.from) && Objects.equals(region, that.region)
                    && Objects.equals(dateCreated, that.dateCreated) && latitude == that.latitude && longitude == that.longitude;
        }
        return super.equals(obj);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, from);
    }
    
    @Override
    public String toString() {
        return String.format("ZoneHistoryData[id %d, from %s, name %s, region %s, dateCreated %s, latitude %f, longitude %f]", //NON-NLS
                id, from, name, region, dateCreated, latitude, longitude);
    }
}
