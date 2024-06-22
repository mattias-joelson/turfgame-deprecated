package org.joelson.turf.idioten.controller;

import org.joelson.turf.idioten.db.DatabaseEntityManager;
import org.joelson.turf.idioten.db.DatabaseEntityManagerTest;
import org.joelson.turf.idioten.model.AssistData;
import org.joelson.turf.idioten.model.RevisitData;
import org.joelson.turf.idioten.model.UserData;
import org.joelson.turf.idioten.model.VisitData;
import org.joelson.turf.idioten.model.ZoneData;
import org.joelson.turf.idioten.util.InstantUtil;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FeedImporterTest extends DatabaseEntityManagerTest {

    public static void readFeedResource(DatabaseEntityManager entityManager) {
        String filename = FeedImporterTest.class.getResource("/feeds_takeover_2024-06-17_10-24-17.json").getFile();
        new FeedImporter(entityManager, new ProgressUpdater(entityManager)).addVisits(new File(filename).toPath());
    }

    @Test
    public void testAddVisits() {
        readFeedResource(getEntityManager());
        assertEquals(146, getEntityManager().getAssists().size());
        assertEquals(458, getEntityManager().getUsers().size());
        List<VisitData> visits = getEntityManager().getVisits();
        assertEquals(1653, visits.size());
        int noRevisits = 0;
        for (VisitData visit : visits) {
            if (visit instanceof RevisitData) {
                noRevisits += 1;
            }
        }
        assertEquals(37, noRevisits);
        assertEquals(1638, getEntityManager().getZones().size());
    }

    //@Test
    public void testPrintVisits() {
        readFeedResource(getEntityManager());
        List<AssistData> assists = getEntityManager().getAssists();
        List<VisitData> visits = getEntityManager().getVisits();
        Map<ZoneData, Integer> zoneVisitsMap = visits.stream().map(VisitData::getZone)
                .sorted(Comparator.comparingInt(ZoneData::getId))
                .collect(Collectors.toMap(e -> e, e -> 1, Integer::sum))
                .entrySet().stream().filter(e -> e.getValue() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        zoneVisitsMap.entrySet().forEach(System.out::println);
        assists.stream().filter(assist -> zoneVisitsMap.containsKey(assist.getZone())).forEach(System.out::println);
        Map<UserData, Integer> userVisitsMap = visits.stream().map(VisitData::getUser)
                .sorted(Comparator.comparing(UserData::getId)).collect(Collectors.toMap(e -> e, e -> 1, Integer::sum))
                .entrySet().stream().filter(e -> e.getValue() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        userVisitsMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(System.out::println);
        assists.stream().filter(assist -> userVisitsMap.containsKey(assist.getUser())).forEach(System.out::println);

        int noFreshwaterVisits = 0;
        int noGamlaStanTVisits = 0;
        int noJossesDaVisits = 0;
        int noPuffaVisits = 0;
        Instant from = InstantUtil.toInstant("2024-06-17T10:20:50");
        Instant to = InstantUtil.toInstant("2024-06-17T10:24:10");
        int noBetweenVisits = 0;

        for (VisitData visit : visits) {
            if (visit.getZone().getId() == 1747) {
                noFreshwaterVisits += 1;
            } else if (visit.getZone().getId() == 21667) {
                noGamlaStanTVisits += 1;
            }
            if (visit.getUser().getId() == 113416) {
                noJossesDaVisits += 1;
            } else if (visit.getUser().getId() == 414549) {
                noPuffaVisits += 1;
            }
            if (visit.getTime().compareTo(from) >= 0 && visit.getTime().compareTo(to) <= 0) {
                noBetweenVisits += 1;
            }
        }
        System.out.println("noFreshwaterVisits: " + noFreshwaterVisits);
        System.out.println("noGamlaStanTVisits: " + noGamlaStanTVisits);
        System.out.println("noJossesDaVisits:   " + noJossesDaVisits);
        System.out.println("noPuffaVisits':     " + noPuffaVisits);
        System.out.println("noBetweenVisits:    " + noBetweenVisits);
        for (AssistData visit : assists) {
            if (visit.getZone().getId() == 1747) {
                noFreshwaterVisits += 1;
            } else if (visit.getZone().getId() == 21667) {
                noGamlaStanTVisits += 1;
            }
            if (visit.getUser().getId() == 113416) {
                noJossesDaVisits += 1;
            } else if (visit.getUser().getId() == 414549) {
                noPuffaVisits += 1;
            }
            if (visit.getTime().compareTo(from) >= 0 && visit.getTime().compareTo(to) <= 0) {
                noBetweenVisits += 1;
            }
        }
        System.out.println("noFreshwaterVisits: " + noFreshwaterVisits);
        System.out.println("noGamlaStanTVisits: " + noGamlaStanTVisits);
        System.out.println("noJossesDaVisits:   " + noJossesDaVisits);
        System.out.println("noPuffaVisits':     " + noPuffaVisits);
        System.out.println("noBetweenVisits:    " + noBetweenVisits);
    }

    @Test
    public void testGetVisits() {
        readFeedResource(getEntityManager());
        assertEquals(4, getEntityManager().getVisits(new ZoneData(1747, "Freshwater")).size());
        assertEquals(2, getEntityManager().getVisits(new ZoneData(21667, "GamlaStanT")).size());
        assertEquals(16, getEntityManager().getVisits(new UserData(113416, "jösses...då")).size());
        assertEquals(15, getEntityManager().getVisits(new UserData(414549, "puffa")).size());
        Instant from = InstantUtil.toInstant("2024-06-17T10:20:50");
        Instant to = InstantUtil.toInstant("2024-06-17T10:24:10");
        assertEquals(218, getEntityManager().getVisits(from, to).size());
    }
}
