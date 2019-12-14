package org.joelson.mattias.turfgame.application.model;

import com.sun.istack.NotNull;
import org.joelson.mattias.turfgame.util.StringUtils;

import java.time.Instant;
import java.util.Objects;

public class RegionHistoryDTO {
    
    private final int id;
    private final Instant from;
    private final String name;
    private final String country;
    
    public RegionHistoryDTO(int id, Instant from, String name, String country) {
        this.id = id;
        this.from = Objects.requireNonNull(from, "From can not be null");
        this.name = StringUtils.requireNotNullAndNotEmpty(name, "Name can not be null", "Name can not be empty");
        this.country = StringUtils.requireNullOrNonEmpty(country, "Country can not be empty");
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
        if (obj instanceof RegionHistoryDTO) {
            RegionHistoryDTO that = (RegionHistoryDTO) obj;
            return id == that.id && Objects.equals(from, that.from) && Objects.equals(name, that.name) && Objects.equals(country, that.country);
        }
        return super.equals(obj);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, from);
    }
    
    @Override
    public String toString() {
        return String.format("RegionHistoryDTO{id %d, from %s, name %s, country %s", id, from, name, country);
    }
}
