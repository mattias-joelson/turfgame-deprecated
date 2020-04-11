package org.joelson.mattias.turfgame.application.model;

import org.joelson.mattias.turfgame.util.StringUtil;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class ZoneData implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final int id;
    private final String name;
    private final RegionData region;
    private final Instant dateCreated;
    private final double latitude;
    private final double longitude;
    private final int tp;
    private final int pph;
    
    public ZoneData(int id, String name, RegionData region, Instant dateCreated, double latitude, double longitude, int tp, int pph) {
        this.id = id;
        this.name = StringUtil.requireNotNullAndNotEmpty(name, "Name can not be null", "Name can not be empty"); //NON-NLS
        this.region = Objects.requireNonNull(region, "Region can not be null"); //NON-NLS
        this.dateCreated = Objects.requireNonNull(dateCreated, "Date created can not be null"); //NON-NLS
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
    
    public int getTp() {
        return tp;
    }
    
    public int getPph() {
        return pph;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ZoneData) {
            ZoneData that = (ZoneData) obj;
            return id == that.id && Objects.equals(name, that.name) && Objects.equals(region, that.region) && Objects.equals(dateCreated, that.dateCreated)
                    && latitude == that.latitude && longitude == that.longitude && tp == that.tp && pph == that.pph;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return id;
    }
    
    @Override
    public String toString() {
        return String.format("ZoneData[id %d, name %s, region %s, dateCreated %s, latitude %f, longitude %f, tp %d, pph %d]", //NON-NLS
                id, name, region, dateCreated, latitude, longitude, tp, pph);
    }
}
