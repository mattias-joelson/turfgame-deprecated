package org.joelson.mattias.turfgame.application.controller;

import org.joelson.mattias.turfgame.application.db.DatabaseEntityManager;
import org.joelson.mattias.turfgame.application.model.ApplicationData;
import org.joelson.mattias.turfgame.application.view.ActionBuilder;
import org.joelson.mattias.turfgame.application.view.ApplicationUI;
import org.joelson.mattias.turfgame.application.view.SwingWorkerBuilder;

import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.swing.Action;
import javax.swing.SwingWorker;

final class OpenDatabaseActionCreator {
    
    private OpenDatabaseActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }
    
    public static Action create(ApplicationActions applicationActions) {
        return new ActionBuilder(actionEvent -> openDatabase(applicationActions.getApplicationUI(), applicationActions.getApplicationData()))
                .withName("Open DB...")
                .withMnemonicKey('O')
                .withAcceleratorKey(KeyEvent.VK_O)
                .build();
    }
    
    private static void openDatabase(ApplicationUI applicationUI, ApplicationData applicationData) {
        Path directoryPath = applicationUI.openDatabaseDialog();
        if (directoryPath != null) {
            applicationUI.setStatus(String.format("Opening database in directory %s  ...", directoryPath));
            new SwingWorkerBuilder<Void, Void>(() -> openDatabaseInBackground(applicationData, directoryPath))
                    .withDone(finishedWorker -> doneOpen(finishedWorker, applicationUI, applicationData, directoryPath))
                    .build()
                    .execute();
        }
    }
    
    private static Void openDatabaseInBackground(ApplicationData applicationData, Path directoryPath) {
        applicationData.openDatabase(DatabaseEntityManager.PERSISTANCE_NAME, directoryPath,
                DatabaseEntityManager.createPersistancePropertyMap(directoryPath, true, false));
        return null;
    }
    
    private static void doneOpen(Future<Void> finishedWorker, ApplicationUI applicationUI, ApplicationData applicationData, Path directoryPath) {
        try {
            finishedWorker.get();
        } catch (ExecutionException e) {
            Boolean createDatebase = applicationUI.showYesNoDialog("Create Database?",
                    String.format("No database could be found in directory %s.\nDo you want to create a new database here?\n%s",
                            directoryPath, e.getCause()));
            if (createDatebase.equals(Boolean.TRUE)) {
                createDatabase(applicationUI, applicationData, directoryPath);
            }
        } catch (InterruptedException e) {
            applicationUI.showUnexpectedInteruptionError(e);
        } finally {
            applicationUI.setApplicationDataStatus();
        }
    }
    private static void createDatabase(ApplicationUI applicationUI, ApplicationData applicationData, Path directoryPath) {
        applicationUI.setStatus(String.format("Creating database in directory %s ...", directoryPath));
        new SwingWorkerBuilder<Void, Void>(() -> createDatabaseInBackground(applicationData, directoryPath))
                .withDone(finishedWorker -> doneCreate(finishedWorker, applicationUI, directoryPath))
                .build()
                .execute();
    }
    
    private static Void createDatabaseInBackground(ApplicationData applicationData, Path directoryPath) {
        applicationData.openDatabase(DatabaseEntityManager.PERSISTANCE_NAME, directoryPath,
                DatabaseEntityManager.createPersistancePropertyMap(directoryPath, false, true));
        return null;
    }
    
    private static void doneCreate(Future<Void> finishedWorker, ApplicationUI applicationUI, Path directoryPath) {
        try {
            finishedWorker.get();
        } catch (ExecutionException e) {
            applicationUI.showErrorDialog("Error Creating Database",
                    String.format("Unable to create a database in directory %s\n%s", directoryPath, e.getCause()));
        } catch (InterruptedException e) {
            applicationUI.showUnexpectedInteruptionError(e);
        } finally {
            applicationUI.setApplicationDataStatus();
        }
    }
}
