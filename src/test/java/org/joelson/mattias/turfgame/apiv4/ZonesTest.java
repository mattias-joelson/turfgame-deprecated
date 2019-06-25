package org.joelson.mattias.turfgame.apiv4;

import org.joelson.mattias.turfgame.util.URLReader;
import org.joelson.mattias.turfgame.util.URLReaderTest;
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
        assertEquals(56376, zones.size());
    }

    public static List<Zone> getAllZones() throws IOException {
        return URLReaderTest.readProperties("/zones-all.json", Zones::fromHTML);
    }
}
