package org.joelson.mattias.turfgame.application.controller;

import org.joelson.mattias.turfgame.application.db.DatabaseEntityManager;
import org.joelson.mattias.turfgame.application.model.ApplicationData;
import org.joelson.mattias.turfgame.application.view.ActionBuilder;
import org.joelson.mattias.turfgame.application.view.ApplicationUI;
import org.joelson.mattias.turfgame.application.view.SwingWorkerBuilder;

import java.beans.PropertyChangeEvent;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.swing.Action;

public final class ImportDatabaseAction {
    
    private ImportDatabaseAction() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }
    
    public static Action create(ApplicationUI applicationUI, ApplicationData applicationData) {
        Action action = new ActionBuilder(actionEvent -> importDatabase(applicationUI, applicationData))
                .withName("Import DB ...")
                .build();
        applicationData.addPropertyChangeListener(ApplicationData.HAS_DATABASE, evt -> propertyChange(evt, action));
        return action;
    }
    
    private static void propertyChange(PropertyChangeEvent evt, Action action) {
        if (ApplicationData.HAS_DATABASE.equals(evt.getPropertyName())) {
            action.setEnabled(Boolean.FALSE.equals(evt.getNewValue()));
        }
    }
    
    private static void importDatabase(ApplicationUI applicationUI, ApplicationData applicationData) {
        Path loadFile = applicationUI.openFileDialog(null);
        if (loadFile == null) {
            return;
        }
        Path directoryPath = applicationUI.openDatabaseDialog();
        if (directoryPath == null) {
            return;
        }
        applicationUI.setStatus(String.format("Importing database from file %s to database in directory %s ...", loadFile, directoryPath));
        new SwingWorkerBuilder<Void, Void>(() -> importDatabaseInBackground(applicationData, loadFile, directoryPath))
                .withDone(future -> done(future, applicationUI, applicationData, loadFile, directoryPath))
                .build().execute();
    }
    
    private static Void importDatabaseInBackground(ApplicationData applicationData, Path loadFile, Path directoryPath) throws SQLException {
        applicationData.importDatabase(loadFile, DatabaseEntityManager.PERSISTANCE_NAME, directoryPath,
                DatabaseEntityManager.createPersistancePropertyMap(directoryPath, false, false));
        return null;
    }
    
    private static void done(Future<Void> finishedWorker, ApplicationUI applicationUI, ApplicationData applicationData, Path loadFile, Path directoryPath) {
        try {
            finishedWorker.get();
        } catch (ExecutionException e) {
            applicationUI.showErrorDialog("Error Importing Database",
                    String.format("Unable to import file %s into directory %s\n%s", loadFile, directoryPath, e.getCause()));
        } catch (InterruptedException e) {
            applicationUI.showErrorDialog("Unexpected Interrupted Exception", e.getMessage());
            e.printStackTrace();
        } finally {
            applicationUI.setStatus(applicationData.getStatus());
        }
    }
}
