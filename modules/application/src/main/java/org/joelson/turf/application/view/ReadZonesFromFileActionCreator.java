package org.joelson.turf.application.view;

import org.joelson.turf.application.model.ApplicationData;

import javax.swing.Action;
import java.awt.event.KeyEvent;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

final class ReadZonesFromFileActionCreator {

    private ReadZonesFromFileActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }

    public static Action create(ApplicationUI applicationUI) {
        Action action = new ActionBuilder(actionEvent -> readZonesFromFile(applicationUI))
                .withName("Read zones from JSON file ...").withMnemonicKey(KeyEvent.VK_J)
                .withAcceleratorKey(KeyEvent.VK_J).build();
        action.setEnabled(false);
        ActionUtil.addEnabledPropertyChangeListener(applicationUI.getApplicationData(), action,
                ApplicationData.HAS_DATABASE, true);
        return action;
    }

    private static void readZonesFromFile(ApplicationUI applicationUI) {
        Path zonesFile = applicationUI.openFileDialog(null);
        if (zonesFile != null) {
            Instant instant = Instant.now();
            try {
                LocalDateTime localDateTime = getLocalDateTime(zonesFile);
                ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
                instant = Instant.from(zonedDateTime);
            } catch (NumberFormatException e) {
                // ignore
            }
            String input = applicationUI.showInputDialog("Instant of Zone Data",
                    String.format("Input instant when zone data in file %s was retrieved:", zonesFile.getFileName()),
                    String.valueOf(instant));
            if (input != null) {
                applicationUI.setStatus(String.format("Reading zones from file %s ...", zonesFile));
                Instant finalInstant = instant;
                new SwingWorkerBuilder<Void, Void>(() -> applicationUI.getApplicationActions()
                        .readZonesFromFile(zonesFile, finalInstant))
                        .withDone(finishedWorker -> done(finishedWorker, applicationUI, zonesFile)).build().execute();
            }

        }
    }

    private static LocalDateTime getLocalDateTime(Path zonesFile) {
        String filename = zonesFile.getFileName().toString();
        int year = Integer.parseInt(filename.substring(6, 10));
        int month = Integer.parseInt(filename.substring(11, 13));
        int day = Integer.parseInt(filename.substring(14, 16));
        int hour = Integer.parseInt(filename.substring(17, 19));
        int minutes = Integer.parseInt(filename.substring(20, 22));
        return LocalDateTime.of(year, month, day, hour, minutes);
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
