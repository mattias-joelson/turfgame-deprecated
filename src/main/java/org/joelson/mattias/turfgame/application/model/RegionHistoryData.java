package org.joelson.mattias.turfgame.application.model;

import org.joelson.mattias.turfgame.util.StringUtil;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class RegionHistoryData implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final int id;
    private final Instant from;
    private final String name;
    private final String country;
    
    public RegionHistoryData(int id, Instant from, String name, String country) {
        this.id = id;
        this.from = Objects.requireNonNull(from, "From can not be null"); //NON-NLS
        this.name = StringUtil.requireNotNullAndNotEmpty(name, "Name can not be null", "Name can not be empty"); //NON-NLS
        this.country = StringUtil.requireNullOrNonEmpty(country, "Country can not be empty"); //NON-NLS
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
    
    public String getCountry() {
        return country;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof RegionHistoryData) {
            RegionHistoryData that = (RegionHistoryData) obj;
            return id == that.id && Objects.equals(from, that.from) && Objects.equals(name, that.name) && Objects.equals(country, that.country);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, from);
    }
    
    @Override
    public String toString() {
        return String.format("RegionHistoryData[id %d, from %s, name %s, country %s]", id, from, name, country); //NON-NLS
    }
}
