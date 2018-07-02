package org.joelson.mattias.turfgame.zundin;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class MonthlyZoneTest {

    private static final String HTML_ROW = "   <tr bgcolor='#b0e0e6'>\n" +
            "    <td align='right'>4</td>\n" +
            "    <td><a href='https://www.turfgame.com/zone/Southball'><img height='12' src='./monthly_oberoff_round96_files/turf.ico'></a> <a href='http://frut.zundin.se/zone.php?zoneid=257&amp;roundid=96'>Southball</a></td>\n" +
            "\t<td align='right'>65</td>\n" +
            "\t<td style='padding-left:10px' align='left'> +9</td>\n" +
            "\t<td>Stockholms kommun</td>\n" +
            "\t<td align='right'>6</td>\n" +
            "\t<td align='right'>6</td>\n" +
            "\t<td align='right'>0</td>\n" +
            "\t<td align='right'>1</td>\n" +
            "   </tr>\n" +
            "\t\t\n";

    @Test
    public void parseRowTest() {
        MonthlyZone zone = MonthlyZone.fromHTML(HTML_ROW);
        assertNotNull(zone);
        assertEquals("Southball", zone.getName());
        assertEquals(65, zone.getTP());
        assertEquals(9, zone.getPPH());
        assertEquals("Stockholms kommun", zone.getMunicipality());
        assertEquals(6, zone.getTakes());
        assertEquals(0, zone.getRevisits());
        assertEquals(1, zone.getAssists());
    }
}
