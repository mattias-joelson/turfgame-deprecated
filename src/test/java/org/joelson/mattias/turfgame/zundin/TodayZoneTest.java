package org.joelson.mattias.turfgame.zundin;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class TodayZoneTest {

    private static final String HTML_ROW = "          <tr>\n" +
            "           <td><script language='javascript' type='text/javascript'>document.write(getLocalDate('2018-07-06 17:48:44'));</script></td>\n" +
            "           <td></td>\n" +
            "\t\t   <td><a href='https://www.turfgame.com/zone/DockZone'><img height='12' src='images/turf.ico'/></a> <a href='http://frut.zundin.se/zone.php?zoneid=326&roundid=97'>DockZone</a></td>\n" +
            "\t\t   <td align='left'>Stockholms kommun</td>\n" +
            "\t\t   <td align='right'>65</td>\n" +
            "\t\t   <td style='padding-left:10px' align='left'></td>\n" +
            "\t\t   <td bgcolor='orange'>Assist</td>\n" +
            "\t\t   <td><a href='https://www.turfgame.com/user/albee'><img height='12' src='images/turf.ico'/></a> <a href='http://frut.zundin.se/unika.php?userid=albee'>albee</a></td>\n" +
            "\t\t   <td>1</td>\n" +
            "\t\t</tr>";

    @Test
    public void parseRowTest() {
        TodayZone zone = TodayZone.fromHTML(HTML_ROW);
        assertNotNull(zone);
        assertEquals("2018-07-06 17:48:44", zone.getDate());
        assertEquals("DockZone", zone.getZoneName());
        assertEquals(326, zone.getZoneId());
        assertEquals("Stockholms kommun", zone.getAreaName());
        assertEquals(65, zone.getTP());
        assertEquals(-1, zone.getPPH());
        assertEquals("Assist", zone.getActivity());
        assertEquals("albee", zone.getUserId());
        assertEquals(1, zone.getTake());
    }
}
