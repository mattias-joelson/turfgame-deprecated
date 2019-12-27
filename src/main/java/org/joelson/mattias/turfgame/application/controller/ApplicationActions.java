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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;

public class ApplicationActions {
    
    private static final String JAVAX_PERSISTENCE_JDBC_URL = "javax.persistence.jdbc.url"; //NON-NLS
    private static final String JAVAX_PERSISTENCE_SCHEMA_GENERATION_DATABASE_ACTION = "javax.persistence.schema-generation.database.action"; //NON-NLS

    private ApplicationUI applicationUI;
    private ApplicationData applicationData;
    
    public ApplicationActions(ApplicationData applicationData) {
        this.applicationData = applicationData;
    }
    
    public void setApplicationUI(ApplicationUI applicationUI) {
        this.applicationUI = applicationUI;
    }
    
    public void closeApplication() {
        applicationData.closeDatabase();
        applicationUI.dispose();
        System.exit(0);
    }
    
    public void readZones() {
        applicationUI.setStatus("Reading zones from Turfgame...");
        new SwingWorker<Void, Void>() {
    
            @Override
            protected Void doInBackground() throws IOException, ParseException {
                List<Zone> zones = Zones.readAllZones();
                applicationData.getZones().updateZones(Instant.now(), zones);
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    get();
                } catch (ExecutionException e) {
                    applicationUI.showErrorDialog("Error reading zones",
                            "Unable to read zones through API V4 and update database.\n" + e.getCause());
                } catch (InterruptedException e) {
                    showUnexpectedInteruptionError(e);
                } finally {
                    setApplicationDataStatus();
                }
            }
        }.execute();
    }
    
    public void readZonesFromFile() {
        Path zonesFile = applicationUI.openFileDialog(null);
        if (zonesFile == null) {
            return;
        }
        applicationUI.setStatus("Reading zones from file " + zonesFile + " ...");
        new SwingWorker<Void, Void>() {
    
            @Override
            protected Void doInBackground() throws IOException, ParseException {
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
                        "Input instant zone data in file " + zonesFile.getFileName() + " was retrieved:", String.valueOf(instant));
                if (input == null) {
                    return null;
                }
                instant = Instant.parse(input);
                applicationData.getZones().updateZones(instant, zones);
                return null;
            }
    
            @Override
            protected void done() {
                try {
                    get();
                } catch (ExecutionException e) {
                    applicationUI.showErrorDialog("Error Updating Zones",
                            String.format("There was an error updating the zones with file %s\n%s", zonesFile, e.getCause()));
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    showUnexpectedInteruptionError(e);
                } finally {
                    setApplicationDataStatus();
                }
            }
        }.execute();
    }

    public void openDatabase() {
        Path directoryPath = applicationUI.openDatabaseDialog();
        if (directoryPath == null) {
            return;
        }
        applicationUI.setStatus("Opening database in directory " + directoryPath + " ...");
        new SwingWorker<Void, Void>() {
    
            @Override
            protected Void doInBackground() throws RuntimeException {
                Map<String, String> persistenceMap = Map.of(
                        JAVAX_PERSISTENCE_JDBC_URL, createJdbcURL(directoryPath, true),
                        JAVAX_PERSISTENCE_SCHEMA_GENERATION_DATABASE_ACTION,"none"); //NON-NLS
                applicationData.setDatabase(DatabaseEntityManager.PERSISTANCE_H2, persistenceMap, directoryPath);
                return null;
            }
    
            @Override
            protected void done() {
                try {
                    get();
                } catch (ExecutionException e) {
                    Boolean createDatebase = applicationUI.showYesNoDialog("Create Database?",
                            String.format("No database could be found in directory %s.\nDo you want to create a new database here?\n%s",
                                    directoryPath, e.getCause()));
                    if (createDatebase.equals(Boolean.TRUE)) {
                        createDatabase(directoryPath);
                    }
                } catch (InterruptedException e) {
                    showUnexpectedInteruptionError(e);
                } finally {
                    setApplicationDataStatus();
                }
            }
        }.execute();
    }

    private void createDatabase(Path directoryPath) {
        applicationUI.setStatus("Creating database in directory " + directoryPath + " ...");
        new SwingWorker<Void, Void>() {
    
            @Override
            protected Void doInBackground() throws RuntimeException {
                Map<String, String> persistenceMap = Map.of(
                        JAVAX_PERSISTENCE_JDBC_URL, createJdbcURL(directoryPath, false),
                        JAVAX_PERSISTENCE_SCHEMA_GENERATION_DATABASE_ACTION,"drop-and-create"); //NON-NLS
                applicationData.setDatabase(DatabaseEntityManager.PERSISTANCE_H2, persistenceMap, directoryPath);
                return null;
            }
    
            @Override
            protected void done() {
                try {
                    get();
                } catch (ExecutionException e) {
                    applicationUI.showErrorDialog("Error Creating Database",
                            String.format("Unable to create a database in directory %s\n%s", directoryPath, e.getCause()));
                } catch (InterruptedException e) {
                    showUnexpectedInteruptionError(e);
                } finally {
                    setApplicationDataStatus();
                }
            }
        }.execute();
    }
    
    public void closeDatabase() {
        applicationData.closeDatabase();
        applicationUI.setStatus(applicationData.getStatus());
    }
    
    public void exportDatabase() {
        Path saveFile = applicationUI.saveFileDialog(null);
        if (saveFile == null) {
            return;
        }
        applicationUI.setStatus("Exporting database to file " + saveFile + " ...");
        new SwingWorker<Void, Void>() {
    
            @Override
            protected Void doInBackground() throws SQLException {
                applicationData.exportDatabase(saveFile);
                return null;
            }
    
            @Override
            protected void done() {
                try {
                    get();
                } catch (ExecutionException e) {
                    applicationUI.showErrorDialog("Error Exporting Database",
                            String.format("Unable to export database to file %s\n%s", saveFile, e.getCause()));
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    showUnexpectedInteruptionError(e);
                } finally {
                    setApplicationDataStatus();
                }
            }
        }.execute();
    }
    
    public void importDatabase() {
        Path loadFile = applicationUI.openFileDialog(null);
        if (loadFile == null) {
            return;
        }
        Path directoryPath = applicationUI.openDatabaseDialog();
        if (directoryPath == null) {
            return;
        }
        applicationUI.setStatus("Importing database from file " + loadFile + " to database in directory " + directoryPath + " ...");
        new SwingWorker<Void, Void>() {
    
            @Override
            protected Void doInBackground() throws SQLException {
                Map<String, String> persistenceMap = Map.of(
                        JAVAX_PERSISTENCE_JDBC_URL, createJdbcURL(directoryPath, false),
                        JAVAX_PERSISTENCE_SCHEMA_GENERATION_DATABASE_ACTION,"none"); //NON-NLS
                applicationData.importDatabase(loadFile, DatabaseEntityManager.PERSISTANCE_H2, persistenceMap, directoryPath);
                return null;
            }
    
            @Override
            protected void done() {
                try {
                    get();
                } catch (ExecutionException e) {
                    applicationUI.showErrorDialog("Error Importing Database",
                            String.format("Unable to import file %s into directory %s\n%s", loadFile, directoryPath, e.getCause()));
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    showUnexpectedInteruptionError(e);
                } finally {
                    setApplicationDataStatus();
                }
            }
        }.execute();
    }
    
    private void setApplicationDataStatus() {
        applicationUI.setStatus(applicationData.getStatus());
    }
    
    private void showUnexpectedInteruptionError(InterruptedException e) {
        applicationUI.showErrorDialog("Unexpected Interrupted Exception", e.getMessage());
        e.printStackTrace();
    }

    private static String createJdbcURL(Path directoryPath, boolean openExisting) {
        return String.format("jdbc:h2:%s/turfgame_h2;IFEXISTS=%s;", //NON-NLS
                directoryPath,
                (openExisting) ? "TRUE" : "FALSE"); //NON-NLS
    }
}
