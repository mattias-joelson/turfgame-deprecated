package org.joelson.mattias.turfgame.zundin;

import org.joelson.mattias.turfgame.util.URLReaderTest;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class TodayTest {

    @Test
    public void testZopnerXToday() throws IOException {
        Today today = getZonerXToday();
        assertNotNull(today);
        assertEquals("ZonerX", today.getUserName());
        assertEquals("2018-06-30", today.getDate());
        assertEquals(880, today.getZones().size());
    }

    private static Today getZonerXToday() throws IOException {
        return readProperties("/todays_activity_zonerx_2018-06-30.html", "ZonerX", "2018-06-30");
    }

    @Test
    public void testOberoffToday() throws IOException {
        Today today = getOberoffToday();
        assertNotNull(today);
        assertEquals("Oberoff", today.getUserName());
        assertEquals("2018-07-06", today.getDate());
        assertEquals(341, today.getZones().size());
    }

    private static Today getOberoffToday() throws IOException {
        return readProperties("/todays_activity_oberoff_2018-07-06.html", "Oberoff", "2018-07-06");
    }

    private static Today readProperties(String resource, String name, String date) throws IOException {
        return URLReaderTest.readProperties(resource, s -> Today.fromHTML(name, date, s));
    }
}
