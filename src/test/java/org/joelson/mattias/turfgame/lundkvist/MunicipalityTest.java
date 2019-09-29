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
        assertEquals(187, zones.size());
        int taken = 0;
        for (Boolean takenZone : zones.values()) {
            if (takenZone) {
                taken += 1;
            }
        }
        assertEquals(187, taken);
    }
    
    @Test
    public void readStockholm() throws IOException {
        Map<String, Boolean> zones = getStockholmZones();
        assertEquals(1378, zones.size());
        int taken = 0;
        for (Boolean takenZone : zones.values()) {
            if (takenZone) {
                taken += 1;
            }
        }
        assertEquals(1378, taken);
    }
    
    @Test
    public void readSundbyberg() throws IOException {
        Map<String, Boolean> zones = getSundbybergZones();
        assertEquals(102, zones.size());
        int taken = 0;
        for (Boolean takenZone : zones.values()) {
            if (takenZone) {
                taken += 1;
            }
        }
        assertEquals(102, taken);
    }
    
    public static Map<String, Boolean> getDanderydZones() throws IOException {
        return URLReaderTest.readProperties("/lundkvist_141_danderyd.html", Municipality::fromHTML);
    }
    
    public static Map<String, Boolean> getHuddingeZones() throws IOException {
        return URLReaderTest.readProperties("/lundkvist_141_huddinge.html", Municipality::fromHTML);
    }

    public static Map<String, Boolean> getJarfallaZones() throws IOException {
        return URLReaderTest.readProperties("/lundkvist_141_jarfalla.html", Municipality::fromHTML);
    }

    public static Map<String, Boolean> getNackaZones() throws IOException {
        return URLReaderTest.readProperties("/lundkvist_141_nacka.html", Municipality::fromHTML);
    }

    public static Map<String, Boolean> getSollentunaZones() throws IOException {
        return URLReaderTest.readProperties("/lundkvist_141_sollentuna.html", Municipality::fromHTML);
    }

    public static Map<String, Boolean> getSolnaZones() throws IOException {
        return URLReaderTest.readProperties("/lundkvist_141_solna.html", Municipality::fromHTML);
    }
    
    public static Map<String, Boolean> getStockholmZones() throws IOException {
        return URLReaderTest.readProperties("/lundkvist_141_stockholm.html", Municipality::fromHTML);
    }
    
    public static Map<String, Boolean> getSundbybergZones() throws IOException {
        return URLReaderTest.readProperties("/lundkvist_141_sundbyberg.html", Municipality::fromHTML);
    }
    
    public static Map<String, Boolean> getUpplandsBroZones() throws IOException {
        return URLReaderTest.readProperties("/lundkvist_141_upplands-bro.html", Municipality::fromHTML);
    }
}
