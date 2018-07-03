package org.joelson.mattias.turfgame.apiv4;

import org.joelson.mattias.turfgame.util.JSONArray;
import org.joelson.mattias.turfgame.util.JSONObject;
import org.joelson.mattias.turfgame.util.JSONParser;
import org.joelson.mattias.turfgame.util.JSONValue;
import org.joelson.mattias.turfgame.util.URLReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class Zones {

    private static final String ALL_ZONES_REQUEST = "http://api.turfgame.com/v4/zones/all";

    private Zones() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }

    public static List<Zone> readAllZones() throws IOException {
        return fromHTML(URLReader.asString(ALL_ZONES_REQUEST));
    }

    static List<Zone> fromHTML(String s) {
        JSONArray valueArray = (JSONArray) new JSONParser().parse(s);
        List<Zone> zones = new ArrayList<>();
        for (JSONValue value : valueArray.getElements()) {
            zones.add(Zone.fromJSON((JSONObject) value));
        }
        return zones;
    }
}
