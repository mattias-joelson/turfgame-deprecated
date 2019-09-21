package org.joelson.mattias.turfgame.warded;

import org.joelson.mattias.turfgame.apiv4.Zone;
import org.joelson.mattias.turfgame.apiv4.ZonesTest;
import org.joelson.mattias.turfgame.lundkvist.MunicipalityTest;
import org.joelson.mattias.turfgame.util.URLReaderTest;
import org.joelson.mattias.turfgame.zundin.Mission;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TakenZoneTest {
    
    @Test
    public void takenZonesTest() throws IOException {
        Map<String, Integer> takenZones = readTakenZones();
        assertEquals(2638, takenZones.size());
        assertTrue(takenZones.containsKey("Åbergssons"));
        assertEquals(484, (long) takenZones.get("Åbergssons"));
    }
    
    @Test
    public void solnaHeatmap() throws IOException {
        List<Zone> allZones = ZonesTest.getAllZones();
        Map<String, Boolean> zones = MunicipalityTest.getSolnaZones();
        Map<String, Integer> takenZones = readTakenZones();
        
        
    }

    public static Map<String, Integer> readTakenZones() throws IOException {
        return URLReaderTest.readProperties("/warded.unique.php.html", TakenZones::fromHTML);
    }
}
