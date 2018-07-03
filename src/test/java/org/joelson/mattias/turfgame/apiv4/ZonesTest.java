package org.joelson.mattias.turfgame.apiv4;

import org.joelson.mattias.turfgame.util.URLReader;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ZonesTest {
    @Test
    public void parseAllZones() throws IOException {
        List<Zone> zones = getAllZones();
        assertEquals(55774, zones.size());
    }

    private List<Zone> getAllZones() throws IOException {
        File file = new File(getClass().getResource("/zones-all.json").getFile());
        FileInputStream input = new FileInputStream(file);
        List<Zone> zones = Zones.fromHTML(URLReader.asString(input));
        input.close();
        return zones;
    }
}
