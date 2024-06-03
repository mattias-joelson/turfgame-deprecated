package org.joelson.mattias.turfgame.application.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.joelson.mattias.turfgame.application.model.RegionData;
import org.joelson.mattias.turfgame.util.StringUtil;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "regions") //NON-NLS
public class RegionEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(updatable = false, nullable = false)
    private int id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @Nullable
    private String country;
    
    public RegionEntity() {
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
        this.name = StringUtil.requireNotNullAndNotEmpty(name, "Name can not be null", "Name can not be empty"); //NON-NLS
    }
    
    @Nullable
    public String getCountry() {
        return country;
    }
    
    public void setCountry(@Nullable String country) {
        this.country = StringUtil.requireNullOrNonEmpty(country, "Country can not be empty"); //NON-NLS
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof RegionEntity) {
            RegionEntity that = (RegionEntity) obj;
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
        return String.format("RegionEntity[id %d, name %s, country %s]", id, name, country); //NON-NLS
    }
    
    public RegionData toData() {
        return new RegionData(id, name, country);
    }
    
    static RegionEntity build(int id, @NotNull String name, @Nullable String country) {
        RegionEntity region = new RegionEntity();
        region.setId(id);
        region.setName(name);
        region.setCountry(country);
        return region;
    }
}
