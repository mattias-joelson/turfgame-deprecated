package org.joelson.mattias.turfgame.application.controller;

import org.joelson.mattias.turfgame.apiv4.Zone;
import org.joelson.mattias.turfgame.apiv4.Zones;
import org.joelson.mattias.turfgame.application.db.DatabaseEntityManager;
import org.joelson.mattias.turfgame.application.model.ApplicationData;
import org.joelson.mattias.turfgame.application.view.ApplicationUI;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;

public class ApplicationActions {
    
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
            protected Void doInBackground() throws IOException {
                List<Zone> zones = Zones.readAllZones();
                applicationData.getZones().updateZones(Instant.now(), zones);
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    get();
                    applicationUI.setStatus(applicationData.getStatus());
                } catch (ExecutionException e) {
                    applicationUI.setStatus(applicationData.getStatus());
                    applicationUI.showErrorDialog("Error reading zones",
                            "Unable to read zones through API V4 and update database.\n" + e.getCause());
                } catch (InterruptedException e) {
                    applicationUI.showErrorDialog("Unexpected Interrupted Exception", e.getMessage());
                    e.printStackTrace();
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
                        "javax.persistence.jdbc.url", createJdbcURL(directoryPath, true),
                        "javax.persistence.schema-generation.database.action","none");
                applicationData.setDatabase(DatabaseEntityManager.PERSISTANCE_H2, persistenceMap, directoryPath);
                return null;
            }
    
            @Override
            protected void done() {
                try {
                    get();
                    applicationUI.setStatus(applicationData.getStatus());
                } catch (ExecutionException e) {
                    applicationUI.setStatus(applicationData.getStatus());
                    Boolean createDatebase = applicationUI.showYesNoDialog("Create Database?",
                            String.format("No database could be found in directory %s.\nDo you want to create a new database here?\n%s",
                                    directoryPath, e.getCause()));
                    if (createDatebase.equals(Boolean.TRUE)) {
                        createDatabase(directoryPath);
                    } else {
                        applicationUI.setStatus(applicationData.getStatus());
                    }
                } catch (InterruptedException e) {
                    applicationUI.showErrorDialog("Unexpected Interrupted Exception", e.getMessage());
                    e.printStackTrace();
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
                        "javax.persistence.jdbc.url", createJdbcURL(directoryPath, false),
                        "javax.persistence.schema-generation.database.action","drop-and-create");
                applicationData.setDatabase(DatabaseEntityManager.PERSISTANCE_H2, persistenceMap, directoryPath);
                return null;
            }
    
            @Override
            protected void done() {
                try {
                    get();
                    applicationUI.setStatus(applicationData.getStatus());
                } catch (ExecutionException e) {
                    applicationUI.setStatus(applicationData.getStatus());
                    applicationUI.showErrorDialog("Error Creating Database",
                            String.format("Unable to create a database in directory %s\n%s", directoryPath, e.getCause()));
                } catch (InterruptedException e) {
                    applicationUI.showErrorDialog("Unexpected Interrupted Exception", e.getMessage());
                    e.printStackTrace();
                }
            }
        }.execute();
    }
    
    public void closeDatabase() {
        applicationData.closeDatabase();
        applicationUI.setStatus(applicationData.getStatus());
    }
    
    private static String createJdbcURL(Path directoryPath, boolean openExisting) {
        return String.format("jdbc:h2:%s/turfgame_h2;IFEXISTS=%s;",
                directoryPath,
                (openExisting) ? "TRUE" : "FALSE");
    }
}
