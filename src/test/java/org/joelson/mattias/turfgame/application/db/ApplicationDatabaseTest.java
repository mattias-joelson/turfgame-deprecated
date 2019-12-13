package org.joelson.mattias.turfgame.application.db;

import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.assertNotNull;

public class ApplicationDatabaseTest {
    
    public static final String CONNECTION_URL_H2 = "jdbc:h2:./output/turfgame_test_h2";
    public static final String CONNECTION_URL_DERBY = "jdbc:derby:output/turfgame_test_derby;create=true";
    
    @Test
    public void testDerby() throws SQLException, ClassNotFoundException {
        ApplicationDatabase database = new ApplicationDatabase(CONNECTION_URL_DERBY);
        assertNotNull(database);
        System.out.println(Class.forName("org.apache.derby.jdbc.EmbeddedDriver"));
    }
    
    @Test
    public void testH2() throws SQLException, ClassNotFoundException {
        ApplicationDatabase database = new ApplicationDatabase(CONNECTION_URL_H2);
        assertNotNull(database);
        System.out.println(Class.forName("org.h2.Driver"));
    }
}