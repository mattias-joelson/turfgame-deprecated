package org.joelson.turf.application.controller;

import jakarta.persistence.PersistenceException;
import org.joelson.turf.application.db.DatabaseEntityManager;
import org.joelson.turf.application.model.ApplicationData;
import org.joelson.turf.application.model.MunicipalityData;
import org.joelson.turf.application.model.ZoneData;
import org.joelson.turf.application.view.ApplicationUI;
import org.joelson.turf.lundkvist.Municipality;
import org.joelson.turf.turfgame.apiv4.Zone;
import org.joelson.turf.turfgame.apiv4.Zones;
import org.joelson.turf.warded.TakenZones;
import org.joelson.turf.zundin.Today;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ApplicationActions {

    private ApplicationUI applicationUI;
    private final ApplicationData applicationData;

    public ApplicationActions(ApplicationData applicationData) {
        this.applicationData = applicationData;
    }

    private static void addUserToSet(Set<String> usernames, String username) {
        if (!username.isEmpty()) {
            usernames.add(username);
        }
    }

    ApplicationData getApplicationData() {
        return applicationData;
    }

    ApplicationUI getApplicationUI() {
        return applicationUI;
    }

    public void setApplicationUI(ApplicationUI applicationUI) {
        this.applicationUI = applicationUI;
    }

    public void closeApplication() {
        applicationData.closeDatabase();
        applicationUI.dispose();
        System.exit(0);
    }

    public Void readZones() throws IOException {
        List<Zone> zones = Zones.readAllZones();
        applicationData.getZones().updateZones(Instant.now().truncatedTo(ChronoUnit.SECONDS), zones);
        return null;
    }

    public Void readZonesFromFile(Path zonesFile, Instant instant) throws IOException {
        List<Zone> zones = Zones.fromJSON(Files.readString(zonesFile));
        applicationData.getZones().updateZones(instant.truncatedTo(ChronoUnit.SECONDS), zones);
        return null;
    }

    public Void readMunicipalityFromFile(Path municipalityFile) throws IOException {
        String file = Files.readString(municipalityFile);
        String name = Municipality.nameFromHTML(file);
        Map<String, Boolean> zoneMap = Municipality.fromHTML(file);
        Set<String> zoneNames = zoneMap.keySet();
        List<ZoneData> zones = applicationData.getZones().getZones().stream()
                .filter(zoneData -> zoneNames.contains(zoneData.getName())).toList();
        MunicipalityData municipality = new MunicipalityData(zones.get(0).getRegion(), name, zones);
        applicationData.getMunicipalities().updateMunicipality(municipality);
        return null;
    }

    public Void readTodayFromFile(Path todayFile, String username, String date) throws IOException {
        String file = Files.readString(todayFile);
        Today today = Today.fromHTML(username, date, file);
        Set<String> usernames = new HashSet<>(today.getZones().size() + 1);
        usernames.add(today.getUserName());
        today.getZones().forEach(todayZone -> addUserToSet(usernames, todayZone.getUserId()));
        applicationData.getUsers().updateUsers(usernames);
        applicationData.getVisits().updateVisits(today);
        return null;
    }

    public Void readWardedFromFile(Path wardedFile) throws IOException {
        String file = Files.readString(wardedFile);
        String userName = TakenZones.getUserNameFromHTML(file);
        Map<String, Integer> visits = TakenZones.fromHTML(file);
        applicationData.getZoneVisits().updateZoneVisits(userName, visits);
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
