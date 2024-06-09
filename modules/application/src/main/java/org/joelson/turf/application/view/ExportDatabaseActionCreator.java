package org.joelson.turf.application.view;

import org.joelson.turf.application.model.ApplicationData;

import javax.swing.Action;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

final class ExportDatabaseActionCreator {

    private ExportDatabaseActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }

    public static Action create(ApplicationUI applicationUI) {
        Action action = new ActionBuilder(actionEvent -> exportDatabase(applicationUI)).withName("Export DB ...")
                .build();
        action.setEnabled(false);
        ActionUtil.addEnabledPropertyChangeListener(applicationUI.getApplicationData(), action,
                ApplicationData.HAS_DATABASE, true);
        return action;
    }

    private static void exportDatabase(ApplicationUI applicationUI) {
        Path saveFile = applicationUI.saveFileDialog(null);
        if (saveFile != null) {
            applicationUI.setStatus(String.format("Exporting database to file %s ...", saveFile));
            new SwingWorkerBuilder<Void, Void>(() -> applicationUI.getApplicationActions().exportDatabase(saveFile))
                    .withDone(finishedWorker -> done(finishedWorker, applicationUI, saveFile)).build().execute();
        }
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
