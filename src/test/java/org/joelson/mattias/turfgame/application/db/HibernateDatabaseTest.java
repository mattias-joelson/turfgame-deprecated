package org.joelson.mattias.turfgame.application.db;

import org.joelson.mattias.turfgame.apiv4.RegionsTest;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class HibernateDatabaseTest {

    public static final String PERSISTANCE_DERBY = "turfgame-test-derby";
    public static final String PERSISTANCE_H2 = "turfgame-test-h2";
    
    @Test
    public void hibernateDerbyTest() throws SQLException, IOException {
        ApplicationDatabase db = new ApplicationDatabase(ApplicationDatabaseTest.CONNECTION_URL_DERBY);
        HibernateDatabase hdb = new HibernateDatabase(HibernateDatabaseTest.PERSISTANCE_DERBY);
        testData(hdb);
    }
    
    @Test
    public void hibernateH2Test() throws SQLException, IOException {
        ApplicationDatabase db = new ApplicationDatabase(ApplicationDatabaseTest.CONNECTION_URL_H2);
        HibernateDatabase hdb = new HibernateDatabase(HibernateDatabaseTest.PERSISTANCE_H2);
        testData(hdb);
    }
    
    private void testData(HibernateDatabase hdb) throws IOException {
        System.out.println("Point (1): " + hdb.findPoint(1));
        System.out.println("Points: " + hdb.getPoints());
        
        System.out.println("getRegions");
        hdb.updateRegions(RegionsTest.getRegions());
        System.out.println("current regions: " + hdb.getRegions());
        System.out.println("getAllRegions");
        hdb.updateRegions(RegionsTest.getAllRegions());
        System.out.println("current regions: " + hdb.getRegions());
    }
}