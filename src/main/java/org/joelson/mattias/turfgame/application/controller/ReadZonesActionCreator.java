package org.joelson.mattias.turfgame.application.controller;

import org.joelson.mattias.turfgame.apiv4.Zone;
import org.joelson.mattias.turfgame.apiv4.Zones;
import org.joelson.mattias.turfgame.application.model.ApplicationData;
import org.joelson.mattias.turfgame.application.view.ActionBuilder;
import org.joelson.mattias.turfgame.application.view.ApplicationUI;
import org.joelson.mattias.turfgame.application.view.SwingWorkerBuilder;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.swing.Action;

final class ReadZonesActionCreator {
    
    private ReadZonesActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }
    
    public static Action create(ApplicationActions applicationActions) {
        Action action = new ActionBuilder(evt -> readZones(evt, applicationActions.getApplicationUI(), applicationActions.getApplicationData()))
                .withName("Read Zones")
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

    private static void readZones(ActionEvent evt, ApplicationUI applicationUI, ApplicationData applicationData) {
        applicationUI.setStatus("Reading zones from Turfgame...");
        new SwingWorkerBuilder<Void, Void>(() -> readZonesInBackground(applicationData))
                .withDone(finishedWorker -> done(finishedWorker, applicationUI))
                .build()
                .execute();
    }
    
    private static Void readZonesInBackground(ApplicationData applicationData) throws IOException, ParseException {
        List<Zone> zones = Zones.readAllZones();
        applicationData.getZones().updateZones(Instant.now().truncatedTo(ChronoUnit.SECONDS), zones);
        return null;
    }
    
    private static void done(Future<Void> finishedWorker, ApplicationUI applicationUI) {
        try {
            finishedWorker.get();
        } catch (ExecutionException e) {
            applicationUI.showErrorDialog("Error reading zones",
                    "Unable to read zones through API V4 and update database.\n" + e.getCause());
        } catch (InterruptedException e) {
            applicationUI.showUnexpectedInteruptionError(e);
        } finally {
            applicationUI.setApplicationDataStatus();
        }
    }
}
