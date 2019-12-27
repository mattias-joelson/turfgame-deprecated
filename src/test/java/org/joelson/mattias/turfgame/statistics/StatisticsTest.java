package org.joelson.mattias.turfgame.statistics;

import org.joelson.mattias.turfgame.apiv4.RegionsTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StatisticsTest {

    @Test
    public void testImportRegions() throws Exception {
        Statistics statistics = new Statistics();
        statistics.importRegions(RegionsTest.getRegions());
        assertEquals(166, statistics.getCountries().size());
        assertEquals(328, statistics.getRegions().size());
    }
}
