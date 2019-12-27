package org.joelson.mattias.turfgame.apiv4;

import org.joelson.mattias.turfgame.util.JSONArray;
import org.joelson.mattias.turfgame.util.JSONObject;
import org.joelson.mattias.turfgame.util.URLReader;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

public final class Zones {

    private static final String ALL_ZONES_REQUEST = "http://api.turfgame.com/v4/zones/all"; //NON-NLS
    private static final String DEFAULT_ZONES_FILENAME = "zones-all.json"; //NON-NLS
    
    private Zones() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }

    public static List<Zone> readAllZones() throws IOException, ParseException {
        return fromJSON(getAllZonesJSON());
    }

    private static String getAllZonesJSON() throws IOException {
        return URLReader.getRequest(ALL_ZONES_REQUEST);
    }

    public static void main(String[] args) throws IOException {
        Files.writeString(Path.of(DEFAULT_ZONES_FILENAME), getAllZonesJSON(), StandardCharsets.UTF_8);
    }

    public static List<Zone> fromJSON(String s) throws ParseException {
        return JSONArray.parseArray(s).stream()
                .map(JSONObject.class::cast)
                .map(Zone::fromJSON)
                .collect(Collectors.toList());
    }
}
