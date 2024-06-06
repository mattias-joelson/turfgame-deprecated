package org.joelson.mattias.turfgame.application.model;

import org.joelson.mattias.turfgame.apiv4.RegionsTest;
import org.joelson.mattias.turfgame.application.db.DatabaseEntityManager;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegionCollectionTest {
    
    public static final String PERSISTANCE_H2 = "turfgame-test-h2";
    
    @Test
    public void regionDataH2Test() throws Exception {
        testData(new RegionCollection(new DatabaseEntityManager(PERSISTANCE_H2)));
    }
    
    private static void testData(RegionCollection regionData) throws Exception {
        regionData.updateRegions(Instant.parse("2019-06-30T22:20:00.00Z"), RegionsTest.getRegions());
        regionData.updateRegions(Instant.parse("2019-12-11T18:42:00.00Z"), RegionsTest.getAllRegions());
        
        assertEquals(388, regionData.getRegion("Maryland").getId());
        assertEquals(330, regionData.getRegions().size());
        assertEquals(331, regionData.getRegionHistory().size());
    }
}
