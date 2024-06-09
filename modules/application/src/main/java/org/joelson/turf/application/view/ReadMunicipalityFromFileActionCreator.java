package org.joelson.turf.application.view;

import org.joelson.turf.application.model.ApplicationData;

import javax.swing.Action;
import java.awt.event.KeyEvent;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

final class ReadMunicipalityFromFileActionCreator {

    private ReadMunicipalityFromFileActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated"); //NON-NLS
    }

    public static Action create(ApplicationUI applicationUI) {
        Action action = new ActionBuilder(actionEvent -> readMunicipalityFromFile(applicationUI))
                .withName("Read Municipality from HTML File...").withMnemonicKey(KeyEvent.VK_M)
                .withAcceleratorKey(KeyEvent.VK_M).build();
        action.setEnabled(false);
        ActionUtil.addEnabledPropertyChangeListener(applicationUI.getApplicationData(), action,
                ApplicationData.HAS_DATABASE, true);
        return action;
    }

    private static void readMunicipalityFromFile(ApplicationUI applicationUI) {
        Path municipalityFile = applicationUI.openFileDialog(null);
        if (municipalityFile != null) {
            applicationUI.setStatus(String.format("Reading municipality from file %s ...", municipalityFile));
            new SwingWorkerBuilder<Void, Void>(
                    () -> applicationUI.getApplicationActions().readMunicipalityFromFile(municipalityFile))
                    .withDone(finishedWorker -> done(finishedWorker, applicationUI, municipalityFile)).build()
                    .execute();
        }
    }

    private static void done(Future<Void> finishedWorker, ApplicationUI applicationUI, Path municipalityFile) {
        try {
            finishedWorker.get();
        } catch (ExecutionException e) {
            applicationUI.showErrorDialog("Error Reading Municipality File",
                    String.format("There was an error reading municipality file %s\n%s", municipalityFile,
                            e.getCause()));
            e.printStackTrace();
        } catch (InterruptedException e) {
            applicationUI.showUnexpectedInteruptionError(e);
        } finally {
            applicationUI.setApplicationDataStatus();
        }
    }
}
