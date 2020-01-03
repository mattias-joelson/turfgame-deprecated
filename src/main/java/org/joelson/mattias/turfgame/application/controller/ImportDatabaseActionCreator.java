package org.joelson.mattias.turfgame.application.controller;

import org.joelson.mattias.turfgame.application.db.DatabaseEntityManager;
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

final class ImportDatabaseActionCreator {
    
    private ImportDatabaseActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }
    
    public static Action create(ApplicationActions applicationActions) {
        Action action = new ActionBuilder(actionEvent -> importDatabase(applicationActions.getApplicationUI(), applicationActions.getApplicationData()))
                .withName("Import DB ...")
                .build();
        applicationActions.getApplicationData().addPropertyChangeListener(ApplicationData.HAS_DATABASE, evt -> propertyChange(evt, action));
        return action;
    }
    
    private static void propertyChange(PropertyChangeEvent evt, Action action) {
        if (Objects.equals(evt.getPropertyName(), ApplicationData.HAS_DATABASE)) {
            action.setEnabled(Objects.equals(evt.getNewValue(), false));
        }
    }
    
    private static void importDatabase(ApplicationUI applicationUI, ApplicationData applicationData) {
        Path loadFile;
        Path directoryPath;
        if ((loadFile = applicationUI.openFileDialog()) != null && (directoryPath = applicationUI.openDatabaseDialog()) != null) {
            applicationUI.setStatus(String.format("Importing database from file %s to database in directory %s ...", loadFile, directoryPath));
            new SwingWorkerBuilder<Void, Void>(() -> importDatabaseInBackground(applicationData, loadFile, directoryPath))
                    .withDone(future -> done(future, applicationUI, loadFile, directoryPath))
                    .build()
                    .execute();
        }
    }
    
    private static Void importDatabaseInBackground(ApplicationData applicationData, Path loadFile, Path directoryPath) throws SQLException {
        applicationData.importDatabase(loadFile, DatabaseEntityManager.PERSISTANCE_NAME, directoryPath,
                DatabaseEntityManager.createPersistancePropertyMap(directoryPath, false, false));
        return null;
    }
    
    private static void done(Future<Void> finishedWorker, ApplicationUI applicationUI, Path loadFile, Path directoryPath) {
        try {
            finishedWorker.get();
        } catch (ExecutionException e) {
            applicationUI.showErrorDialog("Error Importing Database",
                    String.format("Unable to import file %s into directory %s\n%s", loadFile, directoryPath, e.getCause()));
        } catch (InterruptedException e) {
            applicationUI.showUnexpectedInteruptionError(e);
        } finally {
            applicationUI.setApplicationDataStatus();
        }
    }
}
