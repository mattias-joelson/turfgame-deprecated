package org.joelson.mattias.turfgame.application.model;

import org.joelson.mattias.turfgame.application.db.DatabaseEntityManager;

import java.nio.file.Path;
import java.util.Map;

public class ApplicationData {
    
    private ZoneData zones;
    private Path database;
    private DatabaseEntityManager databaseManager;
    
    public ApplicationData() {
    }
    
    public void setDatabase(String unit, Map<String, String> properties, Path databasePath) throws RuntimeException {
        DatabaseEntityManager newDatabaseManager = new DatabaseEntityManager(unit, properties);
        closeDatabase();
        databaseManager = newDatabaseManager;
        database = databasePath;
        zones = new ZoneData(databaseManager);
    }
    
    public void closeDatabase() {
        if (databaseManager != null) {
            databaseManager.close();
            databaseManager = null;
            database = null;
            zones = null;
        }
    }
    
    public ZoneData getZones() {
        return zones;
    }
    
    public String getStatus() {
        return String.format("Database %s, User %s, Zones %s",
                (database != null) ? database : "<no database>",
                "<no user>",
                (getZones() != null) ? getZones().getZones().size() : "<no zones>");
    }
}
