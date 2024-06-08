package org.joelson.turf.lundkvist;

import org.joelson.turf.turfgame.apiv4.Zone;
import org.joelson.turf.turfgame.apiv4.ZonesTest;
import org.joelson.turf.util.URLReaderTest;
import org.joelson.turf.warded.HeatmapTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MunicipalityTest {

    public static Map<String, Boolean> getDanderydZones() throws IOException {
        return URLReaderTest.readProperties("lundkvist_141_danderyd.html", Municipality::fromHTML);
    }

    public static Map<String, Boolean> getNackaZones() throws Exception {
        return URLReaderTest.readProperties("lundkvist_141_nacka.html", Municipality::fromHTML);
    }

    public static Map<String, Boolean> getSollentunaZones() throws Exception {
        return URLReaderTest.readProperties("lundkvist_141_sollentuna.html", Municipality::fromHTML);
    }

    public static Map<String, Boolean> getSolnaZones() throws Exception {
        return URLReaderTest.readProperties("lundkvist_141_solna.html", Municipality::fromHTML);
    }

    public static Map<String, Boolean> getStockholmZones() throws Exception {
        return URLReaderTest.readProperties("lundkvist_141_stockholm.html", Municipality::fromHTML);
    }

    public static Map<String, Boolean> getSundbybergZones() throws Exception {
        return URLReaderTest.readProperties("lundkvist_141_sundbyberg.html", Municipality::fromHTML);
    }

    public static Map<String, Boolean> getTabyZones() throws Exception {
        return URLReaderTest.readProperties("lundkvist_141_taby.html", Municipality::fromHTML);
    }

    public static Map<String, Boolean> getUpplandsBroZones() throws Exception {
        return URLReaderTest.readProperties("lundkvist_141_upplands-bro.html", Municipality::fromHTML);
    }

    public static Map<String, Boolean> getLeifonsSolnaZones() throws Exception {
        return URLReaderTest.readProperties("lundkvist_leifons_141_solna.html", Municipality::fromHTML);
    }

    public static Map<String, Boolean> getLeifonsSundbybergZones() throws Exception {
        return URLReaderTest.readProperties("lundkvist_leifons_141_sundbyberg.html", Municipality::fromHTML);
    }

    @Test
    public void circleZonesSorted() throws Exception {
        System.out.println("Circle zones in date order");
        List<Zone> zones = getZonesSorted(HeatmapTest.getCircleZones(), Comparator.comparing(Zone::getDateCreated));
        IntStream.range(0, zones.size()).forEach(
                i -> System.out.printf("%3d - %s, %s%n", i + 1, zones.get(i).getName(), zones.get(i).getDateCreated()));
    }

    private List<Zone> getZonesSorted(Set<String> filterZones, Comparator<Zone> comparator) throws Exception {
        List<Zone> zones = ZonesTest.getAllZones();
        return zones.stream().filter(zone -> filterZones.contains(zone.getName())).sorted(comparator).collect(
                Collectors.toList());
    }

    @Test
    public void solnaZonesSorted() throws Exception {
        System.out.println("Solna zones in date order");
        List<Zone> zones = ZonesTest.getAllZones();
        Map<String, Boolean> solnaLundkvist = getSolnaZones();
        List<Zone> solnaZones = zones.stream().filter(zone -> solnaLundkvist.containsKey(zone.getName())).sorted(
                Comparator.comparing(Zone::getDateCreated)).collect(Collectors.toList());
        IntStream.range(0, solnaZones.size()).forEach(
                i -> System.out.printf("%3d - %s, %s%n", i, solnaZones.get(i).getName(),
                        solnaZones.get(i).getDateCreated()));
    }

    @Test
    public void danderydZonesSorted() throws Exception {
        System.out.println("Danderyd zones in date order");
        List<Zone> zones = ZonesTest.getAllZones();
        Map<String, Boolean> danderydLundkvist = getDanderydZones();
        List<Zone> solnaZones = zones.stream().filter(zone -> danderydLundkvist.containsKey(zone.getName())).sorted(
                Comparator.comparing(Zone::getName)).collect(Collectors.toList());
        IntStream.range(0, solnaZones.size()).forEach(
                i -> System.out.printf("%3d - %s, %s%n", i, solnaZones.get(i).getName(),
                        solnaZones.get(i).getDateCreated()));
    }
}
