package org.joelson.mattias.turfgame.apiv4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ZonesCompare {

    public static void main(String[] args) throws IOException, ParseException {
        if (args.length < 2) {
            System.out.println(String.format("Usage:\n\t%s zonefile1.json zonefile2.json", ZonesCompare.class.getName()));
            return;
        }
        for (int i = 0; i + 1 < args.length; i += 1) {
            System.out.println(">>> Comparing " + args[i] + " and " + args[i + 1]);
            List<Zone> zones1 = readZones(Path.of(args[i]));
            List<Zone> zones2 = readZones(Path.of(args[i + 1]));
            Map<String, Zone> zonesMap = zones1.stream().collect(Collectors.toMap(Zone::getName, Function.identity()));
            zones2.stream().forEach(zone -> compareZones(zonesMap.get(zone.getName()), zone));
        }
    }

    private static void compareZones(Zone zone1, Zone zone2) {
        if (zone1 == null) {
            System.out.println("New zone: " + toString(zone2));
        } else if (zone1.getDateCreated() == null || zone2.getDateCreated() == null) {
            System.out.println("Zone '" + zone1.getName() + "' date problem: " + zone1.getDateCreated() + ", " + zone2.getDateCreated());
        } else if (!zone1.getName().equals(zone2.getName())
                || !zone1.getDateCreated().equals(zone2.getDateCreated())
                || zone1.getId() != zone2.getId()
                || zone1.getPointsPerHour() != zone2.getPointsPerHour()
                || zone1.getTakeoverPoints() != zone2.getTakeoverPoints()
                || zone1.getRegion().getId() != zone2.getRegion().getId()
                || zone1.getLatitude() != zone2.getLatitude()
                || zone1.getLongitude() != zone2.getLongitude()) {
            System.out.println(toString(zone1) + " differs from " + toString(zone2));
        }
    }

    private static String toString(Zone zone) {
        return String.format("{ %d, %s, %d - %s, %s, %f, %f, %d, %d }", zone.getId(), zone.getName(), zone.getRegion().getId(), zone.getRegion().getName(),
                zone.getDateCreated(), zone.getLatitude(), zone.getLongitude(), zone.getTakeoverPoints(), zone.getPointsPerHour());
    }

    private static List<Zone> readZones(Path file) throws IOException, ParseException {
        return Zones.fromJSON(Files.readString(file));
    }
}
