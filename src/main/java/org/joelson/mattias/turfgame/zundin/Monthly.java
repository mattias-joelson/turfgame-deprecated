package org.joelson.mattias.turfgame.zundin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;

public class Monthly {


    private static final String TURF_LINK_TAG = "<a href=\"https://www.turfgame.com/zone/";

    private final String userName;
    private final Set<MonthlyZone> zones;

    public Monthly(String userName, Set<MonthlyZone> zones) {
        this.userName = userName;
        this.zones = new HashSet<>();
        this.zones.addAll(zones);
    }

    public String getUserName() {
        return userName;
    }

    public Set<MonthlyZone> getZones() {
        return zones;
    }

    public static Monthly fromZundin(String userName, int round) throws IOException {
        String request = "http://frut.zundin.se/monthly.php?userid=" + userName;
        if (round > 0) {
            request += "&roundid=" + round;
        }
        URL url = new URL(request);
        URLConnection connection = url.openConnection();

        try (InputStream input = connection.getInputStream()) {
            return fromHTMLStream(userName, input);
        }
    }

    public static Monthly fromHTMLStream(String userName, InputStream input) throws IOException {
        StringBuilder htmlBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line = reader.readLine();
        while (line != null) {
            htmlBuilder.append(line).append('\n');
            line = reader.readLine();
        }
        return fromHTML(userName, htmlBuilder.toString());
    }

    public static Monthly fromHTML(String userName, String html) {
        Set<MonthlyZone> zones = new HashSet<>();
        int pos = html.indexOf(TURF_LINK_TAG);
        if (pos == -1) {
            return new Monthly(userName, zones);
        }
        while (pos < html.length()) {
            int end = html.indexOf(TURF_LINK_TAG, pos + TURF_LINK_TAG.length());
            if (end == -1) {
                end = html.length();
            }
            MonthlyZone zone = MonthlyZone.fromHTML(html.substring(pos, end));
            zones.add(zone);
            pos = end;
        }
        return new Monthly(userName, zones);
    }

}
