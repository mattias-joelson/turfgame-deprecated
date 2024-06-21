package org.joelson.turf.idioten.controller;

import org.joelson.turf.idioten.db.DatabaseEntityManager;
import org.joelson.turf.idioten.db.DatabaseEntityManagerTest;
import org.joelson.turf.idioten.model.RevisitData;
import org.joelson.turf.idioten.model.VisitData;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FeedImporterTest extends DatabaseEntityManagerTest {

    public static void readFeedResource(DatabaseEntityManager entityManager) {
        String filename = FeedImporterTest.class.getResource("/feeds_takeover_2024-06-17_10-24-17.json").getFile();
        new FeedImporter(entityManager).addVisits(new File(filename).toPath());
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
}
