package org.joelson.turf.application.view;

import org.joelson.turf.application.model.ApplicationData;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.event.KeyEvent;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

final class ReadTodayFromFileActionCreator {

    private ReadTodayFromFileActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }

    public static Action create(ApplicationUI applicationUI) {
        Action action = new ActionBuilder(actionEvent -> readTodayFromFile(applicationUI)).withName(
                        "Read today from HTML file ...").withMnemonicKey(KeyEvent.VK_T).withAcceleratorKey(KeyEvent.VK_T)
                .build();
        action.setEnabled(false);
        ActionUtil.addEnabledPropertyChangeListener(applicationUI.getApplicationData(), action,
                ApplicationData.HAS_DATABASE, true);
        return action;
    }

    private static void readTodayFromFile(ApplicationUI applicationUI) {
        Path todayFile = applicationUI.openFileDialog(null);
        if (todayFile != null) {

            String filename = todayFile.getFileName().toString();
            String user = "";
            String date = LocalDate.now().toString();
            int firstUnderscore = filename.indexOf('_');
            int secondUnderscore = filename.indexOf('_', firstUnderscore + 1);
            if (firstUnderscore > 0 || secondUnderscore > firstUnderscore + 2) {
                user = filename.substring(firstUnderscore + 1, secondUnderscore);
            }
            int period = filename.indexOf('.', secondUnderscore + 1);
            if (secondUnderscore > 0 && period == secondUnderscore + 11) {
                date = filename.substring(secondUnderscore + 1, period);
            }

            JTextField userField = new JTextField(user);
            JTextField dateField = new JTextField(date);

            JPanel panel = new JPanel();
            panel.add(new JLabel("User"));
            panel.add(userField);
            panel.add(Box.createHorizontalStrut(15));
            panel.add(new JLabel("Date"));
            panel.add(dateField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Please enter user name and date",
                    JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                applicationUI.setStatus(String.format("Reading today from file %s ...", todayFile));
                new SwingWorkerBuilder<Void, Void>(() -> applicationUI.getApplicationActions()
                        .readTodayFromFile(todayFile, userField.getText(), dateField.getText()))
                        .withDone(finishedWorker -> done(finishedWorker, applicationUI, todayFile)).build().execute();
            }
        }
    }

    private static void done(Future<Void> finishedWorker, ApplicationUI applicationUI, Path todayFile) {
        try {
            finishedWorker.get();
        } catch (ExecutionException e) {
            applicationUI.showErrorDialog("Error Reading Today File",
                    String.format("There was an error reading the today file %s\n%s", todayFile, e.getCause()));
            e.printStackTrace();
        } catch (InterruptedException e) {
            applicationUI.showUnexpectedInteruptionError(e);
        } finally {
            applicationUI.setApplicationDataStatus();
        }
    }
}
