package org.joelson.turf.application.view;

import org.joelson.turf.application.model.ApplicationData;

import javax.swing.Action;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

final class ImportDatabaseActionCreator {

    private ImportDatabaseActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }

    public static Action create(ApplicationUI applicationUI) {
        Action action = new ActionBuilder(actionEvent -> importDatabase(applicationUI)).withName("Import DB ...")
                .build();
        ActionUtil.addEnabledPropertyChangeListener(applicationUI.getApplicationData(), action,
                ApplicationData.HAS_DATABASE, false);
        return action;
    }

    private static void importDatabase(ApplicationUI applicationUI) {
        Path loadFile;
        Path directoryPath;
        if ((loadFile = applicationUI.openFileDialog()) != null
                && (directoryPath = applicationUI.openDatabaseDialog()) != null) {
            applicationUI.setStatus(
                    String.format("Importing database from file %s to database in directory %s ...",
                            loadFile, directoryPath));
            new SwingWorkerBuilder<Void, Void>(() -> applicationUI.getApplicationActions().importDatabase(loadFile, directoryPath))
                    .withDone(future -> done(future, applicationUI, loadFile, directoryPath)).build().execute();
        }
    }

    private static void done(
            Future<Void> finishedWorker, ApplicationUI applicationUI, Path loadFile, Path directoryPath) {
        try {
            finishedWorker.get();
        } catch (ExecutionException e) {
            applicationUI.showErrorDialog("Error Importing Database",
                    String.format("Unable to import file %s into directory %s\n%s",
                            loadFile, directoryPath, e.getCause()));
        } catch (InterruptedException e) {
            applicationUI.showUnexpectedInterruptedException(e);
        } finally {
            applicationUI.setApplicationDataStatus();
        }
    }
}
