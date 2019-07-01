package org.joelson.mattias.turfgame.lundkvist;

import org.joelson.mattias.turfgame.util.URLReaderTest;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MunicipalityTest {
    
    @Test
    public void readSolna() throws IOException {
        Map<String, Boolean> zones = getSolnaZones();
        assertEquals(184, zones.size());
        int taken = 0;
        for (Boolean takenZone : zones.values()) {
            if (takenZone) {
                taken += 1;
            }
        }
        assertEquals(184, taken);
    }
    
    @Test
    public void readStockholm() throws IOException {
        Map<String, Boolean> zones = getStockholmZones();
        assertEquals(1375, zones.size());
        int taken = 0;
        for (Boolean takenZone : zones.values()) {
            if (takenZone) {
                taken += 1;
            }
        }
        assertEquals(813, taken);
    }

    public static Map<String, Boolean> getSolnaZones() throws IOException {
        return URLReaderTest.readProperties("/lundkvist_141_solna.html", Municipality::fromHTML);
    }
    
    public static Map<String, Boolean> getStockholmZones() throws IOException {
        return URLReaderTest.readProperties("/lundkvist_141_stockholm.html", Municipality::fromHTML);
    }
}
