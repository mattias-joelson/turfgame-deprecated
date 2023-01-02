package org.joelson.mattias.turfgame.apiv5;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joelson.mattias.turfgame.util.StringUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Region {

    private final String country;
    private final Area area;
    private final String name;
    private final int id;

    @JsonCreator
    public Region(
            @Nullable @JsonProperty("country") String country,
            @Nonnull @JsonProperty("area") Area area,
            @Nonnull @JsonProperty("name") String name,
            @JsonProperty("id") int id,
            @JsonProperty("regionLord") User regionLord
    ) {
        this.country = StringUtil.requireNullOrNonEmpty(country);
        this.area = area;
        this.name = StringUtil.requireNotNullAndNotTrimmedEmpty(name);
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public Area getArea() {
        return area;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
