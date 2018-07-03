package org.joelson.mattias.turfgame.apiv4;

import org.joelson.mattias.turfgame.util.JSONNumber;
import org.joelson.mattias.turfgame.util.JSONObject;
import org.joelson.mattias.turfgame.util.JSONString;

public class Region {

    private final String country;
    private final String name;
    private final int id;

    public Region(String country, String name, int id) {
        this.country = country;
        this.name = name;
        this.id = id;
    }

    public static Region fromJSON(JSONObject obj) {
        JSONString name = (JSONString) obj.getValue("name");
        JSONNumber id = (JSONNumber) obj.getValue("id");
        if (obj.containsString("country")) {
            JSONString country = (JSONString) obj.getValue("country");
            return new Region(country.asJava(), name.asJava(), id.asJava().intValue());
        }
        return new Region(null, name.asJava(), id.asJava().intValue());
    }
}
