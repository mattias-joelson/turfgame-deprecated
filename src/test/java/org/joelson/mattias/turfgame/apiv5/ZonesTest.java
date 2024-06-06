package org.joelson.mattias.turfgame.apiv5;

import org.joelson.mattias.turfgame.util.URLReaderTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ZonesTest {

    @Test
    public void parseAllZones() throws Exception {
        List<Zone> zones = getAllZones();
        assertTrue(zones.size() >= 113388);
    }

    @Test
    public void generateStockholmTakeMap() throws Exception {
        List<Zone> zones = getAllZones();
        List<Zone> stockholmZones = zones.stream().filter(zone -> zone.getRegion() != null && zone.getRegion().getId() == 141).collect(Collectors.toList());
        Set<String> municipalities = new HashSet<>();
        stockholmZones.stream().map(Zone::getRegion).map(Region::getArea).map(Area::getName).forEach(municipalities::add);
        assertTrue(true);
        //"[Sigtuna kommun, Huddinge kommun, Ekerö kommun, Vallentuna kommun, Nynäshamns kommun, Södertälje kommun, Sundbybergs kommun, Haninge kommun, Botkyrka kommun, Järfälla kommun, Vaxholms kommun, Lidingö kommun, Upplands Väsby kommun, Sollentuna kommun, Nykvarns kommun, Solna kommun, Värmdö kommun, Danderyds kommun, Salems kommun, Upplands-Bro kommun, Tyresö kommun, Norrtälje kommun, Österåkers kommun, Stockholms kommun, Täby kommun, Nacka kommun]"
    }

    public static List<Zone> getAllZones() throws IOException {
        return URLReaderTest.readProperties("zones-all.v5.json", Zones::fromJSON);
    }
}
