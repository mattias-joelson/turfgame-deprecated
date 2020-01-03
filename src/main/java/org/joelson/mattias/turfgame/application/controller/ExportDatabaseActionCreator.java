package org.joelson.mattias.turfgame.application.controller;

import org.joelson.mattias.turfgame.application.model.ApplicationData;
import org.joelson.mattias.turfgame.application.view.ActionBuilder;
import org.joelson.mattias.turfgame.application.view.ApplicationUI;
import org.joelson.mattias.turfgame.application.view.SwingWorkerBuilder;

import java.beans.PropertyChangeEvent;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.swing.Action;

final class ExportDatabaseActionCreator {
    
    private ExportDatabaseActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }
    
    public static Action create(ApplicationActions applicationActions) {
        Action action = new ActionBuilder(actionEvent -> exportDatabase(applicationActions.getApplicationUI(), applicationActions.getApplicationData()))
                .withName("Export DB ...")
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
    
    private static void exportDatabase(ApplicationUI applicationUI, ApplicationData applicationData) {
        Path saveFile = applicationUI.saveFileDialog(null);
        if (saveFile != null) {
            applicationUI.setStatus(String.format("Exporting database to file %s ...", saveFile));
            new SwingWorkerBuilder<Void, Void>(() -> exportDatabaseInBackground(applicationData, saveFile))
                    .withDone(finishedWorker -> done(finishedWorker, applicationUI, saveFile))
                    .build()
                    .execute();
        }
    }
    
    private static Void exportDatabaseInBackground(ApplicationData applicationData, Path saveFile) throws SQLException {
        applicationData.exportDatabase(saveFile);
        return null;
    }
    
    private static void done(Future<Void> finishedWorker, ApplicationUI applicationUI, Path saveFile) {
        try {
            finishedWorker.get();
        } catch (ExecutionException e) {
            applicationUI.showErrorDialog("Error Exporting Database",
                    String.format("Unable to export database to file %s\n%s", saveFile, e.getCause()));
        } catch (InterruptedException e) {
            applicationUI.showUnexpectedInteruptionError(e);
        } finally {
            applicationUI.setApplicationDataStatus();
        }
    }
}
