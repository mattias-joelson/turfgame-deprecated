package org.joelson.mattias.turfgame.warded;

import org.joelson.mattias.turfgame.util.URLReaderTest;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TakenZoneTest {
    
    @Test
    public void takenZonesTest() throws Exception {
        Map<String, Integer> takenZones = readTakenZones();
        assertEquals(3161, takenZones.size());
        assertTrue(takenZones.containsKey("Bockholmen"));
        assertEquals(1, (long) takenZones.get("Lambastranden"));
    }
    
    public static Map<String, Integer> readTakenZones() throws Exception {
        return URLReaderTest.readProperties("warded.unique.php.html", TakenZones::fromHTML);
    }
}
