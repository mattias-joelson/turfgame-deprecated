package org.joelson.mattias.turfgame.zundin;

import org.joelson.mattias.turfgame.statistics.Statistics;
import org.joelson.mattias.turfgame.statistics.StatisticsInitializer;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MonthlyTest {

    private static final String OBEROFF = "Oberoff";
    public static final int ROUND = 96;

    @Test
    public void parseStatisticsPartFile() throws IOException {
        Monthly monthly = getPartMonthly();
        assertNotNull(monthly);
        assertEquals(OBEROFF, monthly.getUserName());
        assertEquals(ROUND, monthly.getRound());
        assertEquals(343, monthly.getZones().size());
    }

    @Test
    public void testAddPartToStatistics() throws  IOException {
        Monthly monthly = getPartMonthly();
        Statistics statistics = StatisticsInitializer.initialize();
        Monthly.addToStatistics(monthly, statistics);
        System.out.println(statistics);
    }

    private Monthly getPartMonthly() throws IOException {
        File file = new File(getClass().getResource("/monthly_oberoff_round96_part.html").getFile());
        FileInputStream input = new FileInputStream(file);
        Monthly monthly = Monthly.fromHTMLStream(OBEROFF, ROUND, input);
        input.close();
        return monthly;
    }

}

