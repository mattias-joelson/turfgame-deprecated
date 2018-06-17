package org.joelson.mattias.turfgame.statistics;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class StatisticsInitializer {

    public static Statistics initialize() {
        Statistics statistics = new Statistics();

        Country sweden = new Country("se", "Sweden");
        statistics.addCountry(sweden);

        Region stockholm = new Region(141, "Stockholm", sweden);
        statistics.addRegion(stockholm);
        addMunicipality(statistics, stockholm, "Danderyds kommun");
        addMunicipality(statistics, stockholm, "Sigtuna kommun");
        addMunicipality(statistics, stockholm, "Solna kommun");
        addMunicipality(statistics, stockholm, "Stockholmns kommun");
        addMunicipality(statistics, stockholm, "Sundbybergs kommun");

        Region uppsala = new Region(142, "Uppsala", sweden);
        statistics.addRegion(uppsala);
        addMunicipality(statistics, stockholm, "Knivsta kommun");
        addMunicipality(statistics, uppsala, "Uppsala kommun");

        statistics.addUser(new User(80119, "Oberoff"));

        statistics.addRound(new Round(96, "June",
                ZonedDateTime.of(2018, 6, 3,
                        10, 0, 0, 0,
                        ZoneId.of("UTC+00:00"))));

        return statistics;
    }

    private static void addMunicipality(Statistics statistics, Region region, String name) {
        statistics.addMunicipality(new Municipality(name, region));
    }
}
