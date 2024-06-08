package org.joelson.mattias.turfgame.application.model;

import jakarta.persistence.PersistenceException;
import org.joelson.mattias.turfgame.application.db.DatabaseEntityManager;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Map;

public final class ApplicationData {
    
    public static final String HAS_DATABASE = "hasDatabase"; //NON-NLS
    
    private final PropertyChangeSupport propertyChangeSupport;

    private MunicipalityCollection municipalities;
    private RegionCollection regions;
    private UserCollection users;
    private VisitCollection visits;
    private ZoneCollection zones;
    private ZoneVisitCollection zoneVisits;
    private Path database;
    private DatabaseEntityManager databaseManager;
    
    public ApplicationData() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }
    
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }
    
    public Object getValue(String propertyName) {
        if (HAS_DATABASE.equals(propertyName)) {
            return databaseManager != null;
        }
        throw new IllegalArgumentException("Unknown property " + propertyName);
    }
    
    public void openDatabase(String unit, Path databasePath, Map<String, String> properties) throws PersistenceException {
        DatabaseEntityManager newDatabaseManager = new DatabaseEntityManager(unit, properties);
        closeDatabase();
        setDatabase(newDatabaseManager, databasePath);
    }
    
    public void importDatabase(Path importFile, String unit, Path databasePath, Map<String, String> properties) throws SQLException {
        DatabaseEntityManager newDatabaseManager = new DatabaseEntityManager(unit, properties);
        try {
            newDatabaseManager.importDatabase(importFile);
        } catch (SQLException e) {
            newDatabaseManager.close();
            throw e;
        }
        closeDatabase();
        setDatabase(newDatabaseManager, databasePath);
    }
    
    private void setDatabase(DatabaseEntityManager newDatabaseManager, Path databasePath) {
        databaseManager = newDatabaseManager;
        database = databasePath;
        municipalities = new MunicipalityCollection(databaseManager);
        regions = new RegionCollection(databaseManager);
        users = new UserCollection(databaseManager);
        visits = new VisitCollection(databaseManager);
        zones = new ZoneCollection(databaseManager);
        zoneVisits = new ZoneVisitCollection(databaseManager);
        propertyChangeSupport.firePropertyChange(HAS_DATABASE, false, true);
    }
    
    public void exportDatabase(Path exportFile) throws SQLException {
        databaseManager.exportDatabase(exportFile);
    }
    
    public void closeDatabase() {
        if (databaseManager != null) {
            databaseManager.close();
            databaseManager = null;
            database = null;
            municipalities = null;
            regions = null;
            users = null;
            visits = null;
            zones = null;
            propertyChangeSupport.firePropertyChange(HAS_DATABASE, true, false);
        }
    }

    public MunicipalityCollection getMunicipalities() {
        return municipalities;
    }

    public RegionCollection getRegions() {
        return regions;
    }
    
    public UserCollection getUsers() {
        return users;
    }
    
    public VisitCollection getVisits() {
        return visits;
    }
    
    public ZoneCollection getZones() {
        return zones;
    }

    public ZoneVisitCollection getZoneVisits() {
        return zoneVisits;
    }
    
    public String getStatus() {
        return String.format("Database %s, User %s, Zones %s",
                (database != null) ? database : "<no database>",
                (users != null && users.getSelectedUser() != null) ? users.getSelectedUser().getName() : "<no user>",
                (getZones() != null) ? getZones().getZones().size() : "<no zones>");
    }
}
