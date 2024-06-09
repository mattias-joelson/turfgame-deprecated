package org.joelson.turf.application.view;

import org.joelson.turf.application.model.ApplicationData;

import javax.swing.Action;
import java.awt.event.KeyEvent;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

final class ReadWardedFromFileActionCreator {

    private ReadWardedFromFileActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!"); //NON_NLS
    }

    public static Action create(ApplicationUI applicationUI) {
        Action action = new ActionBuilder(actionEvent -> readWardedFromFile(applicationUI))
                .withName("Read Warded from file ...").withMnemonicKey(KeyEvent.VK_W).withAcceleratorKey(KeyEvent.VK_W)
                .build();
        action.setEnabled(false);
        ActionUtil.addEnabledPropertyChangeListener(applicationUI.getApplicationData(), action,
                ApplicationData.HAS_DATABASE, true);
        return action;
    }

    private static void readWardedFromFile(ApplicationUI applicationUI) {
        Path wardedFile = applicationUI.openFileDialog(null);
        if (wardedFile != null) {
            applicationUI.setStatus(String.format("Reading Warded from file %s ...", wardedFile));
            new SwingWorkerBuilder<Void, Void>(
                    () -> applicationUI.getApplicationActions().readWardedFromFile(wardedFile))
                    .withDone(finishedWorker -> done(finishedWorker, applicationUI, wardedFile)).build().execute();
        }
    }

    private static void done(Future<Void> finishedWorker, ApplicationUI applicationUI, Path wardedFile) {
        try {
            finishedWorker.get();
        } catch (ExecutionException e) {
            applicationUI.showErrorDialog("Error Reading Warded File",
                    String.format("There was an error reading warded file %s\n%s", wardedFile, e.getCause()));
            e.printStackTrace();
        } catch (InterruptedException e) {
            applicationUI.showUnexpectedInterruptedException(e);
        } finally {
            applicationUI.setApplicationDataStatus();
        }
    }
}
