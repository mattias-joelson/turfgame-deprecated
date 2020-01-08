package org.joelson.mattias.turfgame.application.view;

import org.joelson.mattias.turfgame.application.model.ApplicationData;

import java.awt.event.KeyEvent;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.swing.Action;

final class ReadZonesFromFileActionCreator {
    
    private ReadZonesFromFileActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }
    
    public static Action create(ApplicationUI applicationUI) {
        Action action = new ActionBuilder(actionEvent -> readZonesFromFile(applicationUI))
                .withName("Read zones from JSON file ...")
                .withMnemonicKey(KeyEvent.VK_J)
                .withAcceleratorKey(KeyEvent.VK_J)
                .build();
        action.setEnabled(false);
        ActionUtil.addEnabledPropertyChangeListener(applicationUI.getApplicationData(), action, ApplicationData.HAS_DATABASE, true);
        return action;
    }
    
    private static void readZonesFromFile(ApplicationUI applicationUI) {
        Path zonesFile = applicationUI.openFileDialog(null);
        if (zonesFile != null) {
            applicationUI.setStatus(String.format("Reading zones from file %s ...", zonesFile));
            new SwingWorkerBuilder<Void, Void>(() -> applicationUI.getApplicationActions().readZonesFromFile(applicationUI, zonesFile))
                    .withDone(finishedWorker -> done(finishedWorker, applicationUI, zonesFile))
                    .build()
                    .execute();
        }
    }
    
    private static void done(Future<Void> finishedWorker, ApplicationUI applicationUI, Path zonesFile) {
        try {
            finishedWorker.get();
        } catch (ExecutionException e) {
            applicationUI.showErrorDialog("Error Updating Zones",
                    String.format("There was an error updating the zones with file %s\n%s", zonesFile, e.getCause()));
        } catch (InterruptedException e) {
            applicationUI.showUnexpectedInteruptionError(e);
        } finally {
            applicationUI.setApplicationDataStatus();
        }
    }
}
