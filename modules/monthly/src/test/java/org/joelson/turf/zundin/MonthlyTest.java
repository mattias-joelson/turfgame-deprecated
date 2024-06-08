package org.joelson.turf.zundin;

import org.joelson.turf.util.URLReaderTest;

public class MonthlyTest {

    private static final String OBEROFF = "Oberoff";
    private static final int ROUND = 113;

    public static Monthly getMonthly() throws Exception {
        return URLReaderTest.readProperties("monthly_0beroff_round168.html", s -> Monthly.fromHTML(OBEROFF, ROUND, s));
    }
}

