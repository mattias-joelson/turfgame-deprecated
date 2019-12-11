package org.joelson.mattias.turfgame.apiv4;

import org.joelson.mattias.turfgame.util.JSONArray;
import org.joelson.mattias.turfgame.util.JSONObject;
import org.joelson.mattias.turfgame.util.JSONParser;
import org.joelson.mattias.turfgame.util.JSONValue;
import org.joelson.mattias.turfgame.util.URLReader;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public final class Regions {

    private static final String REGIONS_REQUEST = "http://api.turfgame.com/v4/regions";

    private Regions() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }

    public static List<Region> readRegions() throws IOException {
        return fromHTML(getAllRegionsJSON());
    }
    
    private static String getAllRegionsJSON() throws IOException {
        return URLReader.getRequest(REGIONS_REQUEST);
    }
    
    public static void main(String[] args) throws IOException {
        String json = getAllRegionsJSON();
        PrintWriter writer = new PrintWriter("regions-all.json", "UTF8");
        writer.println(json);
        writer.close();
    }

    static List<Region> fromHTML(String s) {
        JSONArray valueArray = (JSONArray) new JSONParser().parse(s);

        List<Region> regions = new ArrayList<>();
        for (JSONValue value : valueArray.getElements()) {
            regions.add(Region.fromJSON((JSONObject) value));
        }

        return regions;
    }
}
