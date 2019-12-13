package org.joelson.mattias.turfgame.apiv4;

import org.joelson.mattias.turfgame.util.URLReaderTest;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RegionsTest {

    @Test
    public void parseRegions() throws IOException {
        List<Region> regions = getRegions();
        assertEquals(328, regions.size());
    }

    public static List<Region> getRegions() throws IOException {
        return URLReaderTest.readProperties("/regions.json", Regions::fromHTML);
    }
    
    @Test
    public void parseAllRegions() throws IOException {
        List<Region> regions = getAllRegions();
        assertEquals(330, regions.size());
    }
    
    public static List<Region> getAllRegions() throws IOException {
        return URLReaderTest.readProperties("/regions-all.json", Regions::fromHTML);
    }
}
