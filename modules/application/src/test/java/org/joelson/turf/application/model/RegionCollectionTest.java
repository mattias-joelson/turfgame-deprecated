package org.joelson.turf.application.model;

import org.joelson.turf.application.db.DatabaseEntityManager;
import org.joelson.turf.turfgame.apiv4.RegionsTest;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegionCollectionTest {

    public static final String PERSISTENCE_H2 = "turfgame-test-h2";

    @Test
    public void regionDataH2Test() throws Exception {
        RegionCollection regionData = new RegionCollection(new DatabaseEntityManager(PERSISTENCE_H2));
        regionData.updateRegions(Instant.parse("2019-06-30T22:20:00.00Z"), RegionsTest.getRegions());
        regionData.updateRegions(Instant.parse("2019-12-11T18:42:00.00Z"), RegionsTest.getAllRegions());

        assertEquals(388, regionData.getRegion("Maryland").getId());
        assertEquals(330, regionData.getRegions().size());
        assertEquals(331, regionData.getRegionHistory().size());
    }
}
