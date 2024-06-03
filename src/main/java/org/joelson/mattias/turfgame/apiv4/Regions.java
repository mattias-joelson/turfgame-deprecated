package org.joelson.mattias.turfgame.apiv4;

import org.joelson.mattias.turfgame.util.JacksonUtil;
import org.joelson.mattias.turfgame.util.URLReader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public final class Regions {

    private static final String REGIONS_REQUEST = "https://api.turfgame.com/v4/regions"; //NON-NLS
    private static final String DEFAULT_REGIONS_FILENAME = "regions-all.json"; //NON-NLS

    private Regions() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }

    public static List<Region> readRegions() throws IOException {
        return fromJSON(getAllRegionsJSON());
    }
    
    private static String getAllRegionsJSON() throws IOException {
        return URLReader.getRequest(REGIONS_REQUEST);
    }
    
    public static void main(String[] args) throws IOException {
        Files.writeString(Path.of(DEFAULT_REGIONS_FILENAME), getAllRegionsJSON(), StandardCharsets.UTF_8);
    }

    static List<Region> fromJSON(String s) {
        return Arrays.asList(JacksonUtil.readValue(s, Region[].class));
    }
}
