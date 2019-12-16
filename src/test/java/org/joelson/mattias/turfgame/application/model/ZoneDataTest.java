package org.joelson.mattias.turfgame.application.model;

import org.joelson.mattias.turfgame.apiv4.Zone;
import org.joelson.mattias.turfgame.apiv4.Zones;
import org.joelson.mattias.turfgame.application.db.DatabaseEntityManager;
import org.joelson.mattias.turfgame.util.URLReaderTest;
import org.junit.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ZoneDataTest {
    
    private static final String PERSISTANCE_DERBY = "turfgame-test-derby";
    private static final String PERSISTANCE_H2 = "turfgame-test-h2";
    
    @Test
    public void zoneDataDerbyTest() throws IOException {
        testData(new ZoneData(new DatabaseEntityManager(PERSISTANCE_DERBY)));
    }
    
    @Test
    public void zoneDataH2Test() throws IOException {
        testData(new ZoneData(new DatabaseEntityManager(PERSISTANCE_H2)));
    }
    
    private static void testData(ZoneData zoneData) throws IOException {
        zoneData.updateZones(Instant.parse("2019-11-30T12:12:00.00Z"), getZones("zones-all.2019-11-30.json"));
        
        assertEquals(71, zoneData.getZone("FolkeFilbyter").getId());
        assertEquals(63920, zoneData.getZones().size());
        assertEquals(63920, zoneData.getZoneDataHistory().size());
        assertEquals(63920, zoneData.getZonePointsHistory().size());
        
        zoneData.updateZones(Instant.parse("2019-12-01T22:42:00.00Z"), getZones("zones-all.2019-12-01.json"));
    
        assertEquals(63927, zoneData.getZones().size());
        assertEquals(63927, zoneData.getZoneDataHistory().size());
        assertEquals(77344, zoneData.getZonePointsHistory().size());
    }
    
    public static List<Zone> getZones(String filename) throws IOException {
        return URLReaderTest.readProperties(String.format("/%s", filename), Zones::fromHTML);
    }
}
