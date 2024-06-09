package org.joelson.turf.application.view;

import javax.swing.Action;
import java.awt.event.KeyEvent;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

final class OpenDatabaseActionCreator {

    private OpenDatabaseActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }

    public static Action create(ApplicationUI applicationUI) {
        return new ActionBuilder(actionEvent -> openDatabase(applicationUI)).withName("Open DB...").withMnemonicKey('O')
                .withAcceleratorKey(KeyEvent.VK_O).build();
    }

    private static void openDatabase(ApplicationUI applicationUI) {
        Path directoryPath = applicationUI.openDatabaseDialog();
        if (directoryPath != null) {
            applicationUI.setStatus(String.format("Opening database in directory %s  ...", directoryPath));
            new SwingWorkerBuilder<Void, Void>(
                    () -> applicationUI.getApplicationActions().openDatabase(directoryPath))
                    .withDone(finishedWorker -> doneOpen(finishedWorker, applicationUI, directoryPath)).build()
                    .execute();
        }
    }

    private static void doneOpen(Future<Void> finishedWorker, ApplicationUI applicationUI, Path directoryPath) {
        try {
            finishedWorker.get();
        } catch (ExecutionException e) {
            Boolean createDatebase = applicationUI.showYesNoDialog("Create Database?", String.format(
                    "No database could be found in directory %s.\nDo you want to create a new database here?\n%s",
                    directoryPath, e.getCause()));
            if (createDatebase.equals(Boolean.TRUE)) {
                createDatabase(applicationUI, directoryPath);
            }
        } catch (InterruptedException e) {
            applicationUI.showUnexpectedInteruptionError(e);
        } finally {
            applicationUI.setApplicationDataStatus();
        }
    }

    private static void createDatabase(ApplicationUI applicationUI, Path directoryPath) {
        applicationUI.setStatus(String.format("Creating database in directory %s ...", directoryPath));
        new SwingWorkerBuilder<Void, Void>(
                () -> applicationUI.getApplicationActions().createDatabase(directoryPath))
                .withDone(finishedWorker -> doneCreate(finishedWorker, applicationUI, directoryPath)).build().execute();
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
