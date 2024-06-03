package org.joelson.mattias.turfgame.apiv4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class ZonesFind {

    private ZonesFind() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated"); //NON-NLS
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println(String.format("Usage:\n\t%s zone_name file [files...]", ZonesFind.class)); //NON-NLS
            return;
        }
        String zoneName = args[0];
        for (int i = 1; i < args.length; i += 1) {
            String fileName = args[i];
            String json = Files.readString(Path.of(fileName));
            List<Zone> zones = Zones.fromJSON(json);
            Zone zone = zones.stream().filter(z -> z.getName().equals(zoneName)).findFirst().orElse(null);
            System.out.println(String.format("%s: %s", fileName, toString(zone))); // NON-NLS
        }
    }

    private static String toString(Zone zone) {
        if (zone == null) {
            return String.valueOf((Object) null);
        }
        return String.format("{ %d, %s, %d - %s, %s, %f, %f, %d, %d }", zone.getId(), zone.getName(), zone.getRegion().getId(), zone.getRegion().getName(),
                zone.getDateCreated(), zone.getLatitude(), zone.getLongitude(), zone.getTakeoverPoints(), zone.getPointsPerHour());
    }
}
