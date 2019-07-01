package org.joelson.mattias.turfgame.lundkvist;

import org.joelson.mattias.turfgame.util.URLReader;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Municipality {
    
    private static final String ZONE_LINK = "<td><a href='https://turfgame.com/map/";
    private static final String ROW_END = "</td></tr>";
    
    public static Map<String, Boolean> fromLundkvist(String userName, String country, int region, String municipality) throws IOException {
        String request = String.format("https://turf.lundkvist.com/?user=%s&country=%s&region=%d&city=%s", userName, country, region, municipality);
        return fromHTML(URLReader.getRequest(request));
    }
    
    public static Map<String, Boolean> fromHTML(String html) {
        int pos = html.indexOf(ZONE_LINK);
        if (pos == -1) {
            return Collections.emptyMap();
        }
        Map<String, Boolean> municipalityZones = new HashMap<>();
        while (pos > -1 && pos < html.length()) {
            String zoneName = zoneNameFromHTML(pos, html);
            int end = lineEndInHTML(pos, html);
            boolean taken = zoneTakenFromHTML(end, html);
            municipalityZones.put(zoneName, taken);
            pos = html.indexOf(ZONE_LINK, end + ROW_END.length());
        }
        return municipalityZones;
    }
    
    private static String zoneNameFromHTML(int pos, String html) {
        int end = html.indexOf("'", pos + ZONE_LINK.length());
        String zoneName = html.substring(pos + ZONE_LINK.length(), end);
        return zoneName;
    }
    
    private static int lineEndInHTML(int pos, String html) {
        int end = html.indexOf(ROW_END, pos);
        return end;
    }
    
    private static boolean zoneTakenFromHTML(int end, String html) {
        int start = html.lastIndexOf('>', end);
        String taken = html.substring(start +1 , end);
        return taken.toLowerCase().equals("ja");
    }
}
