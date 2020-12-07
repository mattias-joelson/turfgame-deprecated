package org.joelson.mattias.turfgame.util;

import org.joelson.mattias.turfgame.apiv4.Zone;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZoneUtil {

    private static final double R = 6371.0e3; // radius of Earth in meters

    private ZoneUtil() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }

    /**
     * @see <a href="https://stackoverflow.com/questions/27928/calculate-distance-between-two-latitude-longitude-points-haversine-formula">Stack overflow</a>
     * @see <a href="https://en.wikipedia.org/wiki/Haversine_formula">Haversine formula</a>
     */
    public static double calcDistance(Zone p1, Zone p2) {
        double phi1 = toRadians(p1.getLatitude());
        double phi2 = toRadians(p2.getLatitude());
        double deltaPhi = toRadians(p2.getLatitude() - p1.getLatitude());
        double deltaLambda = toRadians(p2.getLongitude() - p1.getLongitude());

        double a = StrictMath.sin(deltaPhi / 2) * StrictMath.sin(deltaPhi / 2)
                + StrictMath.cos(phi1) * StrictMath.cos(phi2) * StrictMath.sin(deltaLambda / 2) * StrictMath.sin(deltaLambda / 2);
        double c = 2 * StrictMath.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private static double toRadians(double degrees) {
        return degrees * Math.PI / 180;
    }

    public static Map<String, Zone> toNameMap(Collection<Zone> zones) {
        Map<String, Zone> zonesMap = new HashMap<>(zones.size());
        zones.forEach(zone -> zonesMap.put(zone.getName(), zone));
        return zonesMap;
    }

    public static Map<Integer, Zone> toIdMap(Collection<Zone> zones) {
        Map<Integer, Zone> zonesMap = new HashMap<>(zones.size());
        zones.forEach(zone -> zonesMap.put(zone.getId(), zone));
        return zonesMap;
    }
}
