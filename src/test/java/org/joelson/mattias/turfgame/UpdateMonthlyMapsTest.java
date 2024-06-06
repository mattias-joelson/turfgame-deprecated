package org.joelson.mattias.turfgame;

import org.joelson.mattias.turfgame.warded.HeatmapTest;
import org.joelson.mattias.turfgame.warded.TakeDistributionTest;
import org.joelson.mattias.turfgame.warded.UntakenStockholmZoneTest;
import org.joelson.mattias.turfgame.zundin.MonthlyVisitTest;
import org.junit.jupiter.api.Test;

public class UpdateMonthlyMapsTest {

    @Test
    public void updateMonthlyMapsTest() throws Exception {
        new UntakenStockholmZoneTest().generateStockholmTakeMap();
        new MonthlyVisitTest().combinedCircleVisitHeatmapTest();
        new TakeDistributionTest().circleTakeDistributionTest();
        new HeatmapTest().circleHeatmap();
    }
}
