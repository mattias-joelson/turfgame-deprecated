package org.joelson.mattias.turfgame.warded;

import org.joelson.mattias.turfgame.apiv4.Zone;
import org.joelson.mattias.turfgame.apiv4.ZonesTest;
import org.joelson.mattias.turfgame.lundkvist.MunicipalityTest;
import org.joelson.mattias.turfgame.util.KMLWriter;
import org.joelson.mattias.turfgame.util.URLReaderTest;
import org.junit.Test;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TakenZoneTest {
    
    @Test
    public void takenZonesTest() throws IOException {
        Map<String, Integer> takenZones = readTakenZones();
        assertEquals(2781, takenZones.size());
        assertTrue(takenZones.containsKey("Bockholmen"));
        assertEquals(1, (long) takenZones.get("Lambastranden"));
    }
    
    public static Map<String, Integer> readTakenZones() throws IOException {
        return URLReaderTest.readProperties("/warded.unique.php.html", TakenZones::fromHTML);
    }
}
