package org.joelson.mattias.turfgame.apiv5;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joelson.mattias.turfgame.util.StringUtil;

import javax.annotation.Nonnull;

public class Area {

    private final String name;
    private final int id;

    public Area(
            @Nonnull @JsonProperty("name") String name,
            @JsonProperty("id") int id
    ) {
        this.name = StringUtil.requireNotNullAndNotTrimmedEmpty(name);
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
