package org.joelson.mattias.turfgame.zundin;

import org.joelson.mattias.turfgame.util.URLReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Mission {
    
    private static final String ZONE_TABLE = "<th align='right'>Pos</th><th align='left'>Zone</th><th></th>";
    private static final String ZONE_ENTRY = "</td><td><a href='zone.php?zoneid=";
    
    public static List<Integer> fromZundin(String user, int mission) throws IOException {
        String request = String.format("https://frut.zundin.se/mission.php?missionid=%d", mission);
        if (user != null && user.length() > 0) {
            request += "&userid=" + user;
        }
        return fromHTML(URLReader.getRequest(request));
    }
    
    public static List<Integer> fromHTML(String html) {
        int pos = html.indexOf(ZONE_TABLE);
        pos = html.indexOf(ZONE_ENTRY, pos);
        List<Integer> zones = new ArrayList<>();
        while (pos != -1 && pos < html.length()) {
            int end = html.indexOf('\'', pos + ZONE_ENTRY.length());
            String zone = html.substring(pos + ZONE_ENTRY.length(), end);
            zones.add(Integer.valueOf(zone));
            pos = html.indexOf(ZONE_ENTRY, end);
        }
        return zones;
    }
}
