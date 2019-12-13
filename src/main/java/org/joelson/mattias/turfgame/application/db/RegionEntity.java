package org.joelson.mattias.turfgame.application.db;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "regions")
public class RegionEntity {
    
    @Id
    private int id;
    private String name;
    private String country;
    
    public RegionEntity() {
    }
    
    public RegionEntity(int id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    @Override
    public String toString() {
        return String.format("RegionEntity[id %d, name %s, country %s]",
                id, name, country);
    }
}
