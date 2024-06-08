package org.joelson.turf.lundkvist;

import org.joelson.turf.turfgame.apiv5.Zone;
import org.joelson.turf.turfgame.apiv5.ZonesTest;
import org.joelson.turf.util.KMLWriter;
import org.joelson.turf.util.URLReaderTest;
import org.joelson.turf.warded.TakenZones;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SwedenExplorerTest {

    private static boolean isNordicZone(Zone zone) {
        if (zone.getRegion() == null) {
            return false;
        }
        String country = zone.getRegion().getCountry();
        return country != null && (country.equals("se") || country.equals("no") || country.equals("dk")
                || country.equals("fi"));
    }

    private static boolean isSwedishZone(Zone zone) {
        String country = zone.getRegion().getCountry();
        return country != null && country.equals("se");
    }

    private static Map<String, Integer> readGlobalTakenZones() throws Exception {
        return URLReaderTest.readProperties("warded.global-unique.php.html", TakenZones::fromHTML);
    }

    @Test
    public void swedenExplorertest() throws Exception {
        Map<String, Zone> zoneNameMap = new HashMap<>();
        Map<Integer, Zone> zoneIdMap = new HashMap<>();
        Set<Zone> swedishZones = new HashSet<>();
        ZonesTest.getAllZones().stream()
                .filter(SwedenExplorerTest::isNordicZone)
                .forEach(zone -> {
                    zoneNameMap.put(zone.getName(), zone);
                    zoneIdMap.put(zone.getId(), zone);
                    if (isSwedishZone(zone)) {
                        swedishZones.add(zone);
                    }
                });
        Map<String, Integer> globalTakenZones = readGlobalTakenZones();

        Object[][] municipalityZones = new Object[][] {
                { "Vingåkers kommun", "VVingåkersKa" },
                { "Finspångs kommun", "SommarenEKort" },
                { "Åtvidabergs kommun", "GreboKyrka" },
                { "Kinda kommun", "ForsaStation" },
                { "Vadstena", "OrlundaKyrka" },
                { "Boxholms kommun", "NetCliq" },
                { "Tranås kommun", "HolyHolaved" },
                { "Ydre kommun", "Hestra" },
                { "Aneby kommun", "Sunhultsbrunn" },
                { "Nässjö kommun", "NorraSandsjö" },
                { "Sävsjö kommun", "VallsjöGamla" },
                { "Vetlanda kommun", "Nävelsjö" },
                { "Eksjö kommun", "HässlebyKyrka" },
                { "Vimmerby kommun", "Sörgården" },
                { "Hultsfreds kommun", "Lönneberga" },
                { "Högsby kommun", "TallPaint" },
                { "Borgholms kommun", "StoraRör" },
                { "Nybro kommun", "Glasporten" },
                { "Emmaboda kommun", "Algutsboda" },
                { "Lessebo kommun", "LesseboKyrka" },
                { "Uppvidinge kommun", "HerråkraKyrka" },
                { "Växjö kommun", "Fagrabäck" },
                { "Alvesta kommun", "SkatelövKyrka" },
                { "Älmhults kommun", "Härlunda" },
                { "Tingsryds kommun", "Almundsryds" },
                { "Olofströms kommun", "Rävabacken" },
                { "Osby kommun", "LönsbodaTorg" },
                { "Östra Göinge kommun", "Boalt" },

                { "Perstorps kommun", "Bläreträ" },
                { "Örkelljunga kommun", "Skåneporten" },
                { "Klippans kommun", "Mölletofta" },
                { "Ängelholms kommun", "Tostæthorp" },
                { "Båstads kommun", "FörslövsKyrka" },
                { "Höganäs kommun", "FarhultsMölla" },
                { "Helsingborgs kommun", "ByKyrkan" },
                { "Åstorps kommun", "Familia" },
                { "Bjuvs kommun", "BjuvVatten" },
                { "Landskrona kommun", "AsmundKyrka" },
                //{ "Svalövs kommun", "Billeberga" },
                { "Svalövs kommun", "NSkrävlingeKa" },
                { "Eslövs kommun", "SeStorgatan" },
                { "Kävlinge kommun", "Södervidinge" },
                { "Lomma kommun", "RomanskaDamen" },
                { "Lunds kommun", "FjelieZone" },
                { "Staffanstorps kommun", "HolyUppField" },
                { "Burlövs kommun", "ÅkarpVäxtZone" },
                { "Svedala kommun", "AlfonsÅberg" },
                { "Skurups kommun", "KorvMedMos" },
                { "Trelleborgs kommun", "Gröning" },
                { "Vellinge kommun", "VIngelstad" },
                { "Malmö kommun", "HolyBunkeflo" },

                { "Københavns Kommune", "OpenSpace´s" }, // Danmark, Hovedstaden
                { "Sorø Kommune", "Going" }, // Danmark, Sjælland
                { "Billund Kommune", "PlasticBrick" }, // Danmark,Syddanmark
                { "Herning Kommune", "HerningHeart" }, // Danmark, Midtjylland
                { "Aalborg Kommune", "IndkildeZone" }, // Danmark, Nordjylland
                { "Flekkefjord", "FlekkefKirke" }, // Norge, Sørlandet
                { "Lund", "Lundevang" }, // Norge, Vestlandet

                { "Sotenäs kommun", "HBovallstrand" },
                { "Lysekils kommun", "Finnsbofärjan" },
                { "Orust kommun", "Skåpesundsbro" },
                { "Tjörns kommun", "PaterNoster" },
                { "Lilla Edets kommun", "VästerlandaKa" },
                { "Ale kommun", "BohusCentrum" },
                { "Öckerö kommun", "Grönevik" },

                { "Marks kommun", "TostaredKyrka" },
                { "Kungsbacka kommun", "PreLanding" },
                { "Varbergs kommun", "Himle" },
                { "Falkenbergs kommun", "FalkensRast" },
                { "Halmstads kommun", "GetingeKyrka" },
                { "Hylte kommun", "TorupKyrka" },
                { "Gislaveds kommun", "BroarydKyrka" },
                { "Svenljunga kommun", "HolyHåcksvik" },
                { "Tranemo kommun", "SjötoftaKyrka" },
                { "Gnosjö kommun", "KällerydKyrka" },

                { "Mullsjö kommun", "Nykyrka" },
                { "Habo kommun", "Furusjöbadet" },
                { "Tidaholms kommun", "Hökensås" },
                { "Hjo", "NFågelåsKyrka" },
                { "Tibro", "TibroVila" },
                { "Karlsborgs kommun", "MölltorpKyrka" },
                { "Töreboda kommun", "Moholm" },

                { "Lidköpings kommun", "MellbyChurch" },
                { "Grästorps kommun", "HolyHåleTäng" },
                { "Essunga kommun", "Främmekyrkan" },
                { "Trollhättans kommun", "Väneåsaka" },
                { "Vänersborgs kommun", "Brålandazon" },
                { "Melleruds kommun", "BergsKullar" },
                { "Bengtsfors kommun", "BäckeKyrka" },
                { "Färgelanda kommun", "Gubbglutta" },
                { "Dals-Eds kommun", "RölandaKyrka" },
                { "Åmål", "Edslan" },

                { "Hammarö kommun", "FikaVidBron" },
                { "Forshaga kommun", "Öjenäs" },
                { "Kils kommun", "Majorfrågan" },
                { "Munkfors kommun", "KôrkaVeÄlva" },
                { "Sunne kommun", "TorsFönster" },
                { "Torsby kommun", "TorsbyH2OVy" },
                { "Hagfors kommun", "Totempålen" },
                { "Filipstads kommun", "NordmarkKyrka" },
                { "Storfors kommun", "Bjurbäcksbruk" },
                { "Hällefors kommun", "Saxhyttan" },
                { "Nora kommun", "Nyhyttan" },

                { "Fagersta kommun", "VästanChurch" },
                { "Norbergs kommun", "Bråfors" },
                { "Smedjebackens kommun", "Bärkehem" },
                { "Ludvika kommun", "OldRailStreet" },
                { "Vansbro kommun", "NåsKyrka" },
                { "Leksands kommun", "DjuraKyrka" },
                { "Mora kommun", "Moraklockan" },
                { "Orsa kommun", "Kaststjärnan" },
                { "Malung-Sälens kommun", "Soldaten" },
                { "Älvdalens kommun", "NyaSärna" },

                { "Härjedalens kommun", "CentrumFunes" },
                { "Røros", "BrekkenKirke" }, // Norge Trøndelag
                { "Bergs kommun", "KlövsjöKyrka" },
                { "Åre kommun", "HallensKyrka" },
                { "Krokoms kommun", "Krokomporten" },
                { "Östersunds kommun", "HäggenåsKyrka" },
                { "Strömsunds kommun", "Hammerdal" },

                { "Dorotea kommun", "HeligaDorotea" },
                { "Åsele kommun", "Nybyggare" },
                { "Vilhelmina kommun", "MineChurch" },
                { "Storumans kommun", "NorskShopping" },
                { "Rana", "NorwaySide" }, // Norge Nordnorge
                { "Sorsele kommun", "Beukaforsen" },
                { "Lycksele", "PåGudsHus" },
                { "Malå kommun", "MalåKyrka" },

                { "Arvidsjaurs kommun", "Slagnäs" },
                { "Arjeplogs kommun", "Sjulnäs" },
                { "Jokkmokks kommun", "Kåbdalis" },
                { "Älvsbyns kommun", "HolyVidsel" },
                { "Bodens kommun", "EnHedenKyrka" },
                { "Luleå kommun", "DowntownRåneå" },
                { "Kalix kommun", "MorjärvKyrka" },
                { "Överkalix kommun", "NilesZone" },
                { "Gällivare kommun", "Skrövån" },
                { "Kiruna kommun", "Masugnszonen" },
                { "Pajala kommun", "JunoChurch" },
                { "Övertorneå kommun", "SeFinland" },
                { "Pello", "PellonKko" }, // Finland, Lappi
                { "Haparanda kommun", "KallesKapell" },

                { "Ii", "IiKirkko" }, // Finland, Pohjois-Suomi
                { "Kiuruvesi", "Piistinaho" }, // Finland, Itä-Suomi
                { "Pihtipudas", "ForkedChurch" }, // Finland, Länsi- ja Sisä-Suomi
                { "Loimaa", "LoimaaChurch" }, // Finland, Lounais-Suomi
// ", "" },
        };

        Set<String> explorerMunicipalities = new HashSet<>();

        KMLWriter out = new KMLWriter("swedem_explorer.kml");
        out.writeFolder("Untaken Municipalities");
        for (int i = 0; i < municipalityZones.length; i += 1) {
            Object[] zoneData = municipalityZones[i];
            Object zoneIdentifier = zoneData[1];
            Zone zone = (zoneIdentifier instanceof String) ? zoneNameMap.get((String) zoneIdentifier)
                    : zoneIdMap.get((Integer) zoneIdentifier);
            if (zone != null) {
                String zoneDescription = String.format("%d - %s, %s", i + 1, zone.getName(),
                        zone.getRegion().getArea().getName());
                out.writePlacemark(zoneDescription, "", zone.getLongitude(), zone.getLatitude());
                System.out.printf("%s (%d) in %s%n", zoneDescription, zone.getId(), zoneData[0]);
                if (!zone.getRegion().getArea().getName().equals(zoneData[0])) {
                    throw new IllegalArgumentException();
                }
                if (isSwedishZone(zone)) {
                    explorerMunicipalities.add(zone.getRegion().getArea().getName());
                }
            } else {
                System.err.printf("Unknown zone %s in municipality %s!%n", zoneIdentifier, zoneData[0]);
            }
        }
        out.close();
        assertEquals(131, explorerMunicipalities.size());

        Set<String> municipalities = swedishZones.stream().map(zone -> zone.getRegion().getArea().getName())
                .collect(Collectors.toSet());
        assertEquals(290, municipalities.size());
        Set<String> visitedMunicipalities = swedishZones.stream()
                .filter(zone -> globalTakenZones.containsKey(zone.getName()))
                .map(zone -> zone.getRegion().getArea().getName())
                .collect(Collectors.toSet());
        assertEquals(159, visitedMunicipalities.size());
        Set<String> unvisitedMunicipalities = municipalities.stream().filter(s -> !visitedMunicipalities.contains(s))
                .collect(Collectors.toSet());
        assertEquals(131, unvisitedMunicipalities.size());

        assertEquals(unvisitedMunicipalities, explorerMunicipalities);
    }
}
