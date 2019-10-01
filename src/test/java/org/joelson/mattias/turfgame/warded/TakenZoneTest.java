package org.joelson.mattias.turfgame.warded;

import org.joelson.mattias.turfgame.apiv4.Zone;
import org.joelson.mattias.turfgame.apiv4.ZonesTest;
import org.joelson.mattias.turfgame.lundkvist.MunicipalityTest;
import org.joelson.mattias.turfgame.util.KMLWriter;
import org.joelson.mattias.turfgame.util.URLReaderTest;
import org.junit.Test;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TakenZoneTest {
    
    @Test
    public void takenZonesTest() throws IOException {
        Map<String, Integer> takenZones = readTakenZones();
        assertEquals(2738, takenZones.size());
        assertTrue(takenZones.containsKey("Åbergssons"));
        assertEquals(487, (long) takenZones.get("Åbergssons"));
    }
    
    @Test
    public void danderydHeatmap() throws IOException {
        municipalityHeatmap("danderyd_heatmap.kml", MunicipalityTest.getDanderydZones());
    }

    @Test
    public void solnaHeatmap() throws IOException {
        municipalityHeatmap("solna_heatmap.kml", MunicipalityTest.getSolnaZones());
    }

    @Test
    public void sundbybergHeatmap() throws IOException {
        municipalityHeatmap("sundbyberg_heatmap.kml", MunicipalityTest.getSundbybergZones());
    }

    private void municipalityHeatmap(String filename, Map<String, Boolean> municipalityZones) throws IOException {
        List<Zone> allZones = ZonesTest.getAllZones();
        Map<String, Integer> takenZones = readTakenZones();
    
        Map<Zone, Integer> untaken = new HashMap<>();
        Map<Zone, Integer> green = new HashMap<>();
        Map<Zone, Integer> yellow = new HashMap<>();
        Map<Zone, Integer> orange = new HashMap<>();
        Map<Zone, Integer> red = new HashMap<>();
        Map<Zone, Integer> violet = new HashMap<>();
        for (String zoneName : municipalityZones.keySet()) {
            int count = 0;
            if (takenZones.containsKey(zoneName)) {
                count = takenZones.get(zoneName);
            }
            for (Zone zone : allZones) {
                if (zone.getName().equals(zoneName)) {
                    if (count == 0) {
                        untaken.put(zone, 0);
                    } else if (count <= 1) {
                        green.put(zone, count);
                    } else if (count <= 10) {
                        yellow.put(zone, count);
                    } else if (count <= 20) {
                        orange.put(zone, count);
                    } else if (count <= 50) {
                        red.put(zone, count);
                    } else  {
                        violet.put(zone, count);
                    }
                    break;
                }
            }
        }
        
        KMLWriter out = new KMLWriter(filename);
        writeHeatmapFolder(out, untaken, "untaken");
        writeHeatmapFolder(out, green, "green");
        writeHeatmapFolder(out, yellow, "yellow");
        writeHeatmapFolder(out, orange, "orange");
        writeHeatmapFolder(out, red, "red");
        writeHeatmapFolder(out, violet, "violet");
        out.close();
    }
    
    private void writeHeatmapFolder( KMLWriter out, Map<Zone, Integer> zoneCounts, String folderName) {
        if (zoneCounts.isEmpty()) {
            return;
        }
        out.writeFolder(folderName);
        zoneCounts.entrySet().stream()
                .sorted(getEntryComparator())
                .forEach(zoneCountEntry -> out.writePlacemark(String.format("%d - %s", zoneCountEntry.getValue(), zoneCountEntry.getKey().getName()),
                        "", zoneCountEntry.getKey().getLongitude(), zoneCountEntry.getKey().getLatitude()));
    }
    
    private Comparator<Map.Entry<Zone, Integer>> getEntryComparator() {
        return (o1, o2) -> {
            int countDiff = o1.getValue() - o2.getValue();
            if (countDiff != 0) {
                return  countDiff;
            }
            return o1.getKey().getName().compareTo(o2.getKey().getName());
        };
    }
    
    public static Map<String, Integer> readTakenZones() throws IOException {
        return URLReaderTest.readProperties("/warded.unique.php.html", TakenZones::fromHTML);
    }
}
