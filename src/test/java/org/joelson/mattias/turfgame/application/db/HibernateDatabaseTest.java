package org.joelson.mattias.turfgame.application.db;

import org.joelson.mattias.turfgame.apiv4.RegionsTest;
import org.joelson.mattias.turfgame.application.model.RegionData;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class HibernateDatabaseTest {

    public static final String PERSISTANCE_DERBY = "turfgame-test-derby";
    public static final String PERSISTANCE_H2 = "turfgame-test-h2";
    
    @Test
    public void hibernateDerbyTest() throws SQLException, IOException {
        DatabaseEntityManager databaseEntityManager = new DatabaseEntityManager(PERSISTANCE_DERBY);
        testData(databaseEntityManager);
    }
    
    @Test
    public void hibernateH2Test() throws SQLException, IOException {
        DatabaseEntityManager databaseEntityManager = new DatabaseEntityManager(PERSISTANCE_H2);
        testData(databaseEntityManager);
    }
    
    private void testData(DatabaseEntityManager databaseEntityManager) throws IOException {
        System.out.println("getRegions");
        new RegionData(databaseEntityManager).updateRegions(RegionsTest.getRegions());
        System.out.println("current regions: " + databaseEntityManager.getRegions());
        System.out.println("getAllRegions");
        new RegionData(databaseEntityManager).updateRegions(RegionsTest.getAllRegions());
        System.out.println("current regions: " + databaseEntityManager.getRegions());
        
        assertEquals(388, new RegionData(databaseEntityManager).getRegion("Maryland").getId());
    }
}