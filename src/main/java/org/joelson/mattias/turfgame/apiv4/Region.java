package org.joelson.mattias.turfgame.apiv4;

import org.joelson.mattias.turfgame.util.JSONNumber;
import org.joelson.mattias.turfgame.util.JSONObject;
import org.joelson.mattias.turfgame.util.JSONString;

import java.io.Serializable;

public final class Region implements Serializable {

    private static final String COUNTRY = "country";
    private static final String NAME = "name";
    private static final String ID = "id";

    private final String country;
    private final String name;
    private final int id;

    private Region(String country, String name, int id) {
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

    static Region fromJSON(JSONObject obj) {
        JSONString name = (JSONString) obj.getValue(NAME);
        JSONNumber id = (JSONNumber) obj.getValue(ID);
        if (obj.containsName(COUNTRY)) {
            JSONString country = (JSONString) obj.getValue(COUNTRY);
            return new Region(country.stringValue(), name.stringValue(), id.intValue());
        }
        return new Region(null, name.stringValue(), id.intValue());
    }
}
