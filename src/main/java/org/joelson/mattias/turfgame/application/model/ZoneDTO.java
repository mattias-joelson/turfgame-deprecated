package org.joelson.mattias.turfgame.application.model;

import java.time.Instant;
import java.util.Objects;

public class ZoneDTO {
    
    private final int id;
    private final String name;
    private final RegionDTO region;
    private final Instant dateCreated;
    private final double latitude;
    private final double longitude;
    private final int tp;
    private final int pph;
    
    public ZoneDTO(int id, String name, RegionDTO region, Instant dateCreated, double latitude, double longitude, int tp, int pph) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.dateCreated = dateCreated;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tp = tp;
        this.pph = pph;
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public RegionDTO getRegion() {
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
    
    public int getTp() {
        return tp;
    }
    
    public int getPph() {
        return pph;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ZoneDTO) {
            ZoneDTO that = (ZoneDTO) obj;
            return id == that.id && Objects.equals(name, that.name) && Objects.equals(region, that.region) && Objects.equals(dateCreated, that.dateCreated)
                    && latitude == that.latitude && longitude == that.longitude && tp == that.tp && pph == that.pph;
        }
        return super.equals(obj);
    }
    
    @Override
    public int hashCode() {
        return id;
    }
    
    @Override
    public String toString() {
        return String.format("ZoneDTO[id %d, name %s, region %s, dateCreated %s, latitude %f, longitude %f, tp %d, pph %d]",
                id, name, region, dateCreated, latitude, longitude, tp, pph);
    }
}
