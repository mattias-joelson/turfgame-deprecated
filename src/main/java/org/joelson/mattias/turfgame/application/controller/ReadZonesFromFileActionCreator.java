package org.joelson.mattias.turfgame.application.controller;

import org.joelson.mattias.turfgame.apiv4.Zone;
import org.joelson.mattias.turfgame.apiv4.Zones;
import org.joelson.mattias.turfgame.application.model.ApplicationData;
import org.joelson.mattias.turfgame.application.view.ActionBuilder;
import org.joelson.mattias.turfgame.application.view.ApplicationUI;
import org.joelson.mattias.turfgame.application.view.SwingWorkerBuilder;

import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.swing.Action;

final class ReadZonesFromFileActionCreator {
    
    private ReadZonesFromFileActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }
    
    public static Action create(ApplicationActions applicationActions) {
        Action action = new ActionBuilder(actionEvent -> readZonesFromFile(applicationActions.getApplicationUI(), applicationActions.getApplicationData()))
                .withName("Read zones from JSON file ...")
                .withMnemonicKey(KeyEvent.VK_J)
                .withAcceleratorKey(KeyEvent.VK_J)
                .build();
        action.setEnabled(false);
        applicationActions.getApplicationData().addPropertyChangeListener(ApplicationData.HAS_DATABASE, evt -> propertyChange(evt, action));
        return action;
    }
    
    private static void propertyChange(PropertyChangeEvent evt, Action action) {
        if (Objects.equals(evt.getPropertyName(), ApplicationData.HAS_DATABASE)) {
            action.setEnabled(Objects.equals(evt.getNewValue(), true));
        }
    }
    
    private static void readZonesFromFile(ApplicationUI applicationUI, ApplicationData applicationData) {
        Path zonesFile = applicationUI.openFileDialog(null);
        if (zonesFile != null) {
            applicationUI.setStatus(String.format("Reading zones from file %s ...", zonesFile));
            new SwingWorkerBuilder<Void, Void>(() -> readZonesFromFileInBackground(applicationUI, applicationData, zonesFile))
                    .withDone(finishedWorker -> done(finishedWorker, applicationUI, zonesFile))
                    .build()
                    .execute();
        }
    }
    
    private static Void readZonesFromFileInBackground(ApplicationUI applicationUI, ApplicationData applicationData, Path zonesFile)
            throws IOException, ParseException {
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
    
    private static void done(Future<Void> finishedWorker, ApplicationUI applicationUI, Path zonesFile) {
        try {
            finishedWorker.get();
        } catch (ExecutionException e) {
            applicationUI.showErrorDialog("Error Updating Zones",
                    String.format("There was an error updating the zones with file %s\n%s", zonesFile, e.getCause()));
        } catch (InterruptedException e) {
            applicationUI.showUnexpectedInteruptionError(e);
        } finally {
            applicationUI.setApplicationDataStatus();
        }
    }
}
/*
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
                    applicationUI.showUnexpectedInteruptionError(e);
                } finally {
                    applicationUI.setApplicationDataStatus();
                }
            }
        }.execute();
    }
 */