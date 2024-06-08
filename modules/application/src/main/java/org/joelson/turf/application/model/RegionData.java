package org.joelson.mattias.turfgame.application.model;

import org.joelson.mattias.turfgame.util.StringUtil;

import java.io.Serializable;
import java.util.Objects;

public class RegionData implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final int id;
    private final String name;
    private final String country;
    
    public RegionData(int id, String name, String country) {
        this.id = id;
        this.name = StringUtil.requireNotNullAndNotEmpty(name, "Name can not be null", "Name can not be empty"); //NON-NLS
        this.country = StringUtil.requireNullOrNonEmpty(country, "Country can not be empty"); //NON-NLS
    }
    
    public int getId() {
        return id;
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
        if (obj instanceof RegionData) {
            RegionData that = (RegionData) obj;
            return id == that.id && Objects.equals(name, that.name) && Objects.equals(country, that.country);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return id;
    }
    
    @Override
    public String toString() {
        return String.format("RegionData[id %d, name %s, country %s]", id, name, country); //NON-NLS
    }
}
