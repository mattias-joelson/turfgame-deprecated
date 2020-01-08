package org.joelson.mattias.turfgame.application.controller;

import org.joelson.mattias.turfgame.apiv4.Zone;
import org.joelson.mattias.turfgame.apiv4.Zones;
import org.joelson.mattias.turfgame.application.db.DatabaseEntityManager;
import org.joelson.mattias.turfgame.application.model.ApplicationData;
import org.joelson.mattias.turfgame.application.view.ApplicationUI;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.persistence.PersistenceException;

public class ApplicationActions {
    
    private ApplicationUI applicationUI;
    private ApplicationData applicationData;
    
    public ApplicationActions(ApplicationData applicationData) {
        this.applicationData = applicationData;
    }
    
    public void setApplicationUI(ApplicationUI applicationUI) {
        this.applicationUI = applicationUI;
    }
    
    ApplicationData getApplicationData() {
        return applicationData;
    }
    
    ApplicationUI getApplicationUI() {
        return applicationUI;
    }
    
    public void closeApplication() {
        applicationData.closeDatabase();
        applicationUI.dispose();
        System.exit(0);
    }
    
    public Void readZones() throws IOException, ParseException {
        List<Zone> zones = Zones.readAllZones();
        applicationData.getZones().updateZones(Instant.now().truncatedTo(ChronoUnit.SECONDS), zones);
        return null;
    }
    
    public Void readZonesFromFile(ApplicationUI applicationUI, Path zonesFile) throws IOException, ParseException {
        List<Zone> zones = Zones.fromJSON(Files.readString(zonesFile));
        Instant instant = null;
        try {
            String filename = zonesFile.getFileName().toString();
            int year = Integer.parseInt(filename.substring(6, 10));
            int month = Integer.parseInt(filename.substring(11, 13));
            int day = Integer.parseInt(filename.substring(14, 16));
            int hour = Integer.parseInt(filename.substring(17, 19));
            int minutes = Integer.parseInt(filename.substring(20, 22));
            LocalDateTime localDateTime = LocalDateTime.of(year, month, day, hour, minutes);
            ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
            instant = Instant.from(zonedDateTime);
        } catch (NumberFormatException e) {
            // ignore
        }
        if (instant == null) {
            instant = Instant.now();
        }
        String input = applicationUI.showInputDialog("Instant of Zone Data",
                String.format("Input instant when zone data in file %s was retrieved:", zonesFile.getFileName()), String.valueOf(instant));
        if (input != null) {
            applicationData.getZones().updateZones(Instant.parse(input), zones);
        }
        return null;
    }

    public Void openDatabase(Path directoryPath) throws PersistenceException {
        applicationData.openDatabase(DatabaseEntityManager.PERSISTANCE_NAME, directoryPath,
                DatabaseEntityManager.createPersistancePropertyMap(directoryPath, true, false));
        return null;
    }

    public Void createDatabase(Path directoryPath) throws PersistenceException {
        applicationData.openDatabase(DatabaseEntityManager.PERSISTANCE_NAME, directoryPath,
                DatabaseEntityManager.createPersistancePropertyMap(directoryPath, false, true));
        return null;
    }
    
    public void closeDatabase() {
        applicationData.closeDatabase();
        applicationUI.setStatus(applicationData.getStatus());
    }
    
    public Void importDatabase(Path loadFile, Path directoryPath) throws SQLException {
        applicationData.importDatabase(loadFile, DatabaseEntityManager.PERSISTANCE_NAME, directoryPath,
                DatabaseEntityManager.createPersistancePropertyMap(directoryPath, false, false));
        return null;
    }
    
    public Void exportDatabase(Path saveFile) throws SQLException {
        applicationData.exportDatabase(saveFile);
        return null;
    }
}
