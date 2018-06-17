package org.joelson.mattias.turfgame.zundin;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MonthlyTest {

    private static final String OBEROFF = "Oberoff";

    @Test
    public void parseStatisticsFile() throws IOException {
        File file = new File(getClass().getResource("/monthly_oberoff_round96.html").getFile());
        FileInputStream input = new FileInputStream(file);
        Monthly monthly = Monthly.fromHTMLStream(OBEROFF, input);
        input.close();
        assertNotNull(monthly);
        assertEquals(OBEROFF, monthly.getUserName());
        assertEquals(343, monthly.getZones().size());
    }

}

