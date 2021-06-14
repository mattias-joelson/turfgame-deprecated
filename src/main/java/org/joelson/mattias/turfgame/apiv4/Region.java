package org.joelson.mattias.turfgame.apiv4;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Region {

    private final String country;
    private final String name;
    private final int id;

    @JsonCreator
    private Region(
            @JsonProperty("country") String country,
            @JsonProperty("name") String name,
            @JsonProperty("id") int id,
            @JsonProperty("regionLord") User regionLord) {
        this.country = country;
        this.name = name;
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Region{" +
                "country='" + country + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
