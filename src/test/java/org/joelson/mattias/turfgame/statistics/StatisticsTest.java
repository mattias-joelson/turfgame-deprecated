package org.joelson.mattias.turfgame.statistics;

import org.joelson.mattias.turfgame.apiv4.RegionsTest;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;


public class StatisticsTest {

    @Test
    public void testImportRegions() throws IOException {
        Statistics statistics = new Statistics();
        statistics.importRegions(RegionsTest.getRegions());
        assertEquals(163, statistics.getCountries().size());
        assertEquals(304, statistics.getRegions().size());
    }
}
