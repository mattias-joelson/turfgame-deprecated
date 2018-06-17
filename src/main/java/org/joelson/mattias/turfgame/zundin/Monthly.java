package org.joelson.mattias.turfgame.zundin;

import org.joelson.mattias.turfgame.statistics.Municipality;
import org.joelson.mattias.turfgame.statistics.Round;
import org.joelson.mattias.turfgame.statistics.Statistics;
import org.joelson.mattias.turfgame.statistics.User;
import org.joelson.mattias.turfgame.statistics.Visits;
import org.joelson.mattias.turfgame.statistics.Zone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Monthly {


    private static final String TURF_LINK_TAG = "<a href=\"https://www.turfgame.com/zone/";

    private final String userName;
    private final int round;
    private final Set<MonthlyZone> zones;

    public Monthly(String userName, int round, Set<MonthlyZone> zones) {
        this.userName = userName;
        this.round = round;
        this.zones = new HashSet<>();
        this.zones.addAll(zones);
    }

    public String getUserName() {
        return userName;
    }

    public int getRound() {
        return round;
    }

    public Set<MonthlyZone> getZones() {
        return Collections.unmodifiableSet(zones);
    }

    public static void addToStatistics(Monthly monthly, Statistics statistics) {
        User user = statistics.getUser(monthly.getUserName());
        Round round = statistics.getRound(monthly.getRound());
        for (MonthlyZone monthlyZone : monthly.getZones()) {
            Zone zone = statistics.getZone(monthlyZone.getName());
            if (zone == null) {
                Municipality municipality = statistics.getMunicipality(monthlyZone.getMunicipality());
                zone = new Zone(-1, monthlyZone.getName(), municipality, 0f, 0f);
                statistics.addZone(zone);
            }
            Visits visits = new Visits(zone, user, round, monthlyZone.getTP(), monthlyZone.getPPH(),
                    monthlyZone.getTakes(), monthlyZone.getAssists(), monthlyZone.getRevisits());
            if (!statistics.addVisits(visits)) {
                System.err.println("Visits already exists! " + visits);
            }
        }
    }

    public static Monthly fromZundin(String userName, int round) throws IOException {
        String request = "http://frut.zundin.se/monthly.php?userid=" + userName;
        if (round > 0) {
            request += "&roundid=" + round;
        }
        URL url = new URL(request);
        URLConnection connection = url.openConnection();

        try (InputStream input = connection.getInputStream()) {
            return fromHTMLStream(userName, round, input);
        }
    }

    public static Monthly fromHTMLStream(String userName, int round, InputStream input) throws IOException {
        StringBuilder htmlBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line = reader.readLine();
        while (line != null) {
            htmlBuilder.append(line).append('\n');
            line = reader.readLine();
        }
        return fromHTML(userName, round, htmlBuilder.toString());
    }

    public static Monthly fromHTML(String userName, int round, String html) {
        Set<MonthlyZone> zones = new HashSet<>();
        int pos = html.indexOf(TURF_LINK_TAG);
        if (pos == -1) {
            return new Monthly(userName, round, zones);
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
        return new Monthly(userName, round, zones);
    }

}
