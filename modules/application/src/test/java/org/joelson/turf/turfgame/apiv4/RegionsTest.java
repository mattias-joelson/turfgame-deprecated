package org.joelson.turf.turfgame.apiv4;

import org.joelson.turf.util.URLReaderTest;

import java.util.List;

public class RegionsTest {

    public static List<Region> getRegions() throws Exception {
        return URLReaderTest.readProperties("regions.json", Regions::fromJSON);
    }

    public static List<Region> getAllRegions() throws Exception {
        return URLReaderTest.readProperties("regions-all.json", Regions::fromJSON);
    }
}
