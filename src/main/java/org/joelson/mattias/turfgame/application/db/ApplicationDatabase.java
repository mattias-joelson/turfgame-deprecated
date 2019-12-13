package org.joelson.mattias.turfgame.application.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ApplicationDatabase {
    
    public static final String CONNECTION_URL_H2 = "jdbc:h2:./turfgame_h2";
    public static final String CONNECTION_URL_DERBY = "jdbc:derby:turfgame_derby;create=true";
    
    private static final String[] DROP_TABLE_NAMES = new String[] {
            "zone_name_history",
            "zone_location_history",
            "zone_points_history",
            "zones",
            "points",
            "regions",
    };
    
    private static final String CREATE_TABLE_POINTS = "CREATE TABLE points ("
            + "id INT NOT NULL , "
            + "tp INT NOT NULL, "
            + "pph INT NOT NULL, "
            + "PRIMARY KEY (id))";
    
    private static final String CREATE_TABLE_REGIONS = "CREATE TABLE regions ("
            + "id INT NOT NULL, "
            + "name VARCHAR(50) NOT NULL, "
            + "country VARCHAR(2), "
            + "PRIMARY KEY (id)"
            + ")";
    
    private static final String CREATE_TABLE_ZONES = "CREATE TABLE zones ("
            + "id INT NOT NULL, "
            + "date_created TIMESTAMP NOT NULL,"
            + "name VARCHAR (15) NOT NULL, "
            + "region_id INT NOT NULL, "
            + "latitude DOUBLE NOT NULL, "
            + "longitude DOUBLE NOT NULL, "
            + "points_id INT NOT NULL,"
            + "FOREIGN KEY (region_id) REFERENCES regions (id), "
            + "FOREIGN KEY (points_id) REFERENCES points (id), "
            + "PRIMARY KEY (id)"
            + ")";

    private static final String CREATE_TABLE_ZONE_NAME_HISTORY = "CREATE TABLE zone_name_history ("
            + "zone_id INT NOT NULL, "
            + "from_timestamp TIMESTAMP NOT NULL, "
            + "name VARCHAR(15) NOT NULL, "
            + "FOREIGN KEY (zone_id) REFERENCES zones (id), "
            + "PRIMARY KEY (zone_id, from_timestamp)"
            + ")";

    private static final String CREATE_TABLE_ZONE_LOCATION_HISTORY = "CREATE TABLE zone_location_history ("
            + "zone_id INT NOT NULL,"
            + "from_timestamp TIMESTAMP NOT NULL, "
            + "latitude DOUBLE NOT NULL, "
            + "longitude DOUBLE NOT NULL, "
            + "FOREIGN KEY (zone_id) REFERENCES zones (id), "
            + "PRIMARY KEY (zone_id, from_timestamp)"
            + ")";
    private static final String CREATE_TABLE_ZONE_POINTS_HISTORY = "CREATE TABLE zone_points_history ("
            + "zone_id INT NOT NULL, "
            + "from_timestamp TIMESTAMP NOT NULL, "
            + "points_id INT NOT NULL, "
            + "FOREIGN KEY (zone_id) REFERENCES zones (id), "
            + "FOREIGN KEY (points_id) REFERENCES points (id), "
            + "PRIMARY KEY (zone_id, from_timestamp)"
            + ")";
    
    private Connection connection;
    
    public ApplicationDatabase(String connectionURL) throws SQLException {
        connection = DriverManager.getConnection(connectionURL);
        System.out.println(connection);
        dropTables();
        initDatabase(connection);
    }
    
    private void dropTables() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            for (String tableName : DROP_TABLE_NAMES) {
                if (hasTable(tableName)) {
                    System.out.println("Droppping table " + tableName);
                    statement.executeUpdate("DROP TABLE " + tableName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private boolean hasTable(String tableName) throws SQLException {
        return connection.getMetaData().getTables(null, null, tableName.toUpperCase(), null).next();
    }
    
    private static void initDatabase(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
        
        
            //Date date = Date.valueOf("2013-03-21 22:42:28");
            //statement.executeUpdate("INSERT INTO zones VALUES (16061,'FäbodaSwim', 1, 63.673851, 22.559149, '2013-03-21 22:42:28')");
            //statement.executeUpdate("INSERT INTO zones VALUES (156973,'FäbodaSwim', 1, 63.673851, 22.559149, '2018-08-20 23:24:50')");
            //statement.executeUpdate("INSERT INTO zones VALUES (12240, 'KrausZone', 1, 59.374753, 18.02911, '2012-12-12 22:44:52')");
            //statement.executeUpdate("INSERT INTO zones VALUES (12241, 'Sjöstugan', 1, 59.37619, 18.027229, '2012-12-12 22:50:25')");
        
            statement.executeUpdate(CREATE_TABLE_POINTS);
            statement.executeUpdate("INSERT INTO points VALUES (0, 250, 0)");
            statement.executeUpdate("INSERT INTO points VALUES (1, 185, 1)");
            statement.executeUpdate("INSERT INTO points VALUES (2, 170, 2)");
            statement.executeUpdate("INSERT INTO points VALUES (3, 155, 3)");
            statement.executeUpdate("INSERT INTO points VALUES (4, 140, 4)");
            statement.executeUpdate("INSERT INTO points VALUES (5, 125, 5)");
            statement.executeUpdate("INSERT INTO points VALUES (6, 110, 6)");
            statement.executeUpdate("INSERT INTO points VALUES (7,  95, 7)");
            statement.executeUpdate("INSERT INTO points VALUES (8,  80, 8)");
            statement.executeUpdate("INSERT INTO points VALUES (9,  65, 9)");
        
            statement.executeUpdate(CREATE_TABLE_REGIONS);
            statement.executeUpdate(CREATE_TABLE_ZONES);
            statement.executeUpdate(CREATE_TABLE_ZONE_NAME_HISTORY);
            statement.executeUpdate(CREATE_TABLE_ZONE_LOCATION_HISTORY);
            statement.executeUpdate(CREATE_TABLE_ZONE_POINTS_HISTORY);
        
            //statement.executeUpdate("CREATE TABLE zone_points (zone_id INT, point_id INT, point_from TIMESTAMP)");
            //statement.executeUpdate("INSERT INTO zone_points VALUES (12240, 2, '2019-12-01 12:00:00')");
            //statement.executeUpdate("INSERT INTO zone_points VALUES (12241, 4, '2019-12-01 12:00:00')");
        
            //statement.executeUpdate("DROP TABLE regions");
            //statement.executeUpdate("CREATE TABLE regions (id INT, name VARCHAR )")
        

        
        /*
            FäbodaSwim - 16061 - 2013-03-21T22:42:28+0000 - Länsi- ja Sisä-Suomi @ 63.673851, 22.559149
            FäbodaSwim - 156973 - 2018-08-20T23:24:50+0000 - Länsi- ja Sisä-Suomi @ 63.673851, 22.559149
            
            {"dateCreated":"2012-12-12T22:44:52+0000","latitude":\
59.374753,"name":"KrausZone","id":12240,"totalTakeovers":672,"region":{"country":"se","name":"Stockholm","id":141},"pointsPerHour":2,"\
longitude":18.02911,"takeoverPoints":170},{"dateCreated":"2012-12-12T22:50:25+0000","latitude":59.37619,"name":"Sjöstugan","id":12241,\
"totalTakeovers":1311,"region":{"country":"se","name":"Stockholm","id":141},"pointsPerHour":4,"longitude":18.027229,"takeoverPoints":1\
40}
         */
    
            ResultSet rs = statement.executeQuery("SELECT * FROM zones");
            while (rs.next()) {
                System.out.printf("%d %s %d %f %f %s\n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("region_id"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude"),
                        rs.getTimestamp("date_created"));
            }
            
            rs = statement.executeQuery("SELECT * FROM points");
            while (rs.next()) {
                System.out.println(String.format("Points id %d, tp %d, pph %d",
                        rs.getInt("id"),
                        rs.getInt("tp"),
                        rs.getInt("pph")));
            }
        }
    }
}
