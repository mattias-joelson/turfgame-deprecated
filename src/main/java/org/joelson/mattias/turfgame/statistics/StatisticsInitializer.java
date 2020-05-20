package org.joelson.mattias.turfgame.statistics;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class StatisticsInitializer {

    private StatisticsInitializer() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }

    public static Statistics initialize() {
        Statistics statistics = new Statistics();

        Country spain = new Country("es");
        statistics.addCountry(spain);
        Country sweden = new Country("se");
        statistics.addCountry(sweden);

        Region stockholm = new Region(141, "Stockholm", sweden);
        statistics.addRegion(stockholm);
        addMunicipality(statistics, stockholm, "Danderyds kommun");
        addMunicipality(statistics, stockholm, "Huddinge kommun");
        addMunicipality(statistics, stockholm, "Järfälla kommun");
        addMunicipality(statistics, stockholm, "Lidingö kommun");
        addMunicipality(statistics, stockholm, "Nacka kommun");
        addMunicipality(statistics, stockholm, "Sigtuna kommun");
        addMunicipality(statistics, stockholm, "Solna kommun");
        addMunicipality(statistics, stockholm, "Sollentuna kommun");
        addMunicipality(statistics, stockholm, "Stockholms kommun");
        addMunicipality(statistics, stockholm, "Sundbybergs kommun");
        addMunicipality(statistics, stockholm, "Täby kommun");
        addMunicipality(statistics, stockholm, "Vallentuna kommun");
        addMunicipality(statistics, stockholm, "Upplands Väsby kommun");
        addMunicipality(statistics, stockholm, "Upplands-Bro kommun");

        Region uppsala = new Region(142, "Uppsala", sweden);
        statistics.addRegion(uppsala);
        addMunicipality(statistics, stockholm, "Knivsta kommun");
        addMunicipality(statistics, uppsala, "Uppsala kommun");
    
        Region orebro = new Region(131, "Örebro", sweden);
        statistics.addRegion(uppsala);
        addMunicipality(statistics, stockholm, "Lindesbergs kommun");

        Region spainRegion = new Region(211, "Spain", spain);
        statistics.addRegion(spainRegion);
        addMunicipality(statistics, spainRegion, "Spain");

        statistics.addUser(new User(80119, "Oberoff"));

        statistics.addRound(new Round(113, "June",
                ZonedDateTime.of(2019, 11, 3,
                        11, 0, 0, 0,
                        ZoneId.of("UTC+00:00"))));

        return statistics;
    }

    private static void addMunicipality(Statistics statistics, Region region, String name) {
        statistics.addMunicipality(new Municipality(name, region));
    }
}
