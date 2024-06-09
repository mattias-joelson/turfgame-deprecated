package org.joelson.turf.application.view;

import org.joelson.turf.application.model.ApplicationData;

import javax.swing.Action;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

final class ReadZonesActionCreator {

    private ReadZonesActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }

    public static Action create(ApplicationUI applicationUI) {
        Action action = new ActionBuilder(evt -> readZones(applicationUI)).withName("Read Zones").build();
        action.setEnabled(false);
        ActionUtil.addEnabledPropertyChangeListener(applicationUI.getApplicationData(), action,
                ApplicationData.HAS_DATABASE, true);
        return action;
    }

    private static void readZones(ApplicationUI applicationUI) {
        applicationUI.setStatus("Reading zones from Turfgame...");
        new SwingWorkerBuilder<Void, Void>(() -> applicationUI.getApplicationActions().readZones())
                .withDone(finishedWorker -> done(finishedWorker, applicationUI)).build().execute();
    }

    private static void done(Future<Void> finishedWorker, ApplicationUI applicationUI) {
        try {
            finishedWorker.get();
        } catch (ExecutionException e) {
            applicationUI.showErrorDialog("Error Reading Zones",
                    String.format("Unable to read zones through API V4 and update database.\n%s", e.getCause()));
        } catch (InterruptedException e) {
            applicationUI.showUnexpectedInteruptionError(e);
        } finally {
            applicationUI.setApplicationDataStatus();
        }
    }
}
