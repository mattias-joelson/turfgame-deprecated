package org.joelson.mattias.turfgame.application.model;

import org.joelson.mattias.turfgame.apiv4.Zone;
import org.joelson.mattias.turfgame.apiv4.Zones;
import org.joelson.mattias.turfgame.application.db.DatabaseEntityManager;
import org.joelson.mattias.turfgame.util.TimeUtil;
import org.joelson.mattias.turfgame.util.URLReaderTest;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ZoneCollectionTest {
    
    private static final String PERSISTANCE_H2 = "turfgame-test-h2";
    
    @Test
    public void instantTest() {
        Instant i1 = Instant.parse("2019-11-30T12:12:00.00Z");
        Instant i2 = Instant.parse("2019-11-30T11:12:00.00Z");
        assertTrue(i2.isBefore(i1));
        Instant i3 = TimeUtil.turfTimestampToInstant("2019-11-30T12:12:00");
        assertTrue(i3.equals(i2));
        assertTrue(i1.isAfter(i3));
        Instant i4 = TimeUtil.turfTimestampToInstant("2019-11-30T12:12:00+01:00");
        assertTrue(i4.equals(i3));
    }
    
    @Test
    public void zoneDataH2Test() throws Exception {
        testData(new ZoneCollection(new DatabaseEntityManager(PERSISTANCE_H2)));
    }
    
    private static void testData(ZoneCollection zoneData) throws Exception {
        zoneData.updateZones(TimeUtil.turfTimestampToInstant("2019-11-30T12:12:00.00+01:00"), getZones("zones-all.2019-11-30.json"));
        
        assertEquals(71, zoneData.getZone("FolkeFilbyter").getId());
        assertEquals(63920, zoneData.getZones().size());
        assertEquals(63920, zoneData.getZoneHistory().size());
        assertEquals(63920, zoneData.getZonePointsHistory().size());
        
        zoneData.updateZones(TimeUtil.turfTimestampToInstant("2019-12-01T22:42:00.00+01:00"), getZones("zones-all.2019-12-01.json"));
    
        assertEquals(63927, zoneData.getZones().size());
        assertEquals(63927, zoneData.getZoneHistory().size());
        assertEquals(77344, zoneData.getZonePointsHistory().size());
    }
    
    private static List<Zone> getZones(String filename) throws Exception {
        return URLReaderTest.readProperties(filename, Zones::fromJSON);
    }
}
