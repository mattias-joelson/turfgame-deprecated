package org.joelson.mattias.turfgame.apiv4;

import org.joelson.mattias.turfgame.util.URLReaderTest;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class RegionsTest {

    @Test
    public void parseRegions() throws Exception {
        List<Region> regions = getRegions();
        assertEquals(328, regions.size());
    }

    public static List<Region> getRegions() throws Exception {
        return URLReaderTest.readProperties("regions.json", Regions::fromJSON);
    }
    
    @Test
    public void parseAllRegions() throws Exception {
        List<Region> regions = getAllRegions();
        assertEquals(330, regions.size());
    }
    
    public static List<Region> getAllRegions() throws Exception {
        return URLReaderTest.readProperties("regions-all.json", Regions::fromJSON);
    }
}
