package org.joelson.turf.application.view;

import org.joelson.turf.application.controller.ApplicationActions;
import org.joelson.turf.application.model.ApplicationData;

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.desktop.QuitStrategy;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.Serial;
import java.nio.file.Path;

public class ApplicationUI {

    private static final String OPEN_DATABASE_TEXT = "Open Database";
    private static final String APPLICATION_TITLE = "Turf Statistics";
    private final ApplicationActions applicationActions;
    private final ApplicationData applicationData;

    private final JFrame applicationFrame;
    private final JLabel statusLabel;
    private Container currentContent;
    private JFileChooser directoryChooser;
    private JFileChooser fileChooser;

    public ApplicationUI(ApplicationActions applicationActions, ApplicationData applicationData) {
        this.applicationActions = applicationActions;
        this.applicationData = applicationData;
        applicationFrame = new JFrame(APPLICATION_TITLE);
        statusLabel = new JLabel("<no user>");
    }

    public void initUI() {
        initApplicationFrame();
        createContent(applicationFrame.getContentPane());
        clearPane();
    }

    ApplicationActions getApplicationActions() {
        return applicationActions;
    }

    ApplicationData getApplicationData() {
        return applicationData;
    }

    Action readZonesAction() {
        return ReadZonesActionCreator.create(this);
    }

    Action readZonesFromFileAction() {
        return ReadZonesFromFileActionCreator.create(this);
    }

    Action regionTableAction() {
        return RegionTableActionCreator.create(this);
    }

    Action regionHistoryTableAction() {
        return RegionHistoryTableActionCreator.create(this);
    }

    Action zoneTableAction() {
        return ZoneTableActionCreator.create(this);
    }

    Action zoneHistoryTableAction() {
        return ZoneHistoryTableActionCreator.create(this);
    }

    Action zonePointsHistoryTableAction() {
        return ZonePointsHistoryTableActionCreator.create(this);
    }

    Action userTableAction() {
        return UserTableActionCreator.create(this);
    }

    Action visitTableAction() {
        return VisitTableActionCreator.create(this);
    }

    Action sessionTableAction() {
        return SessionTableActionCreator.create(this);
    }

    Action statisticsAction() {
        return StatisticsActionCreator.create(this);
    }

    Action zoneOwnershipGraphAction() {
        return ZoneOwnershipGraphActionCreator.create(this);
    }

    Action zoneTakeGraphAction() {
        return ZoneTakeGraphActionCreator.create(this);
    }

    Action uniqueRoundZoneGraphAction() {
        return UniqueRoundZoneGraphActionCreator.create(this);
    }

    Action readTodayFromFileAction() {
        return ReadTodayFromFileActionCreator.create(this);
    }

    Action readWardedFromFileAction() {
        return ReadWardedFromFileActionCreator.create(this);
    }

    Action zoneVisitTableAction() {
        return ZoneVisitTableActionCreator.create(this);
    }

    Action municipalityVisitAction() {
        return MunicipalityVisitTableActionCreator.create(this);
    }

    Action readMunicipalityFromFileAction() {
        return ReadMunicipalityFromFileActionCreator.create(this);
    }

    Action openDatabaseAction() {
        return OpenDatabaseActionCreator.create(this);
    }

    public void closeDatabase() {
        applicationActions.closeDatabase();
        clearPane();
    }

    Action exportDatabaseAction() {
        return ExportDatabaseActionCreator.create(this);
    }

    Action importDatabaseAction() {
        return ImportDatabaseActionCreator.create(this);
    }

    private void initApplicationFrame() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        applicationFrame.setSize(screenSize.width / 2, screenSize.height / 2);
        applicationFrame.setLocation(screenSize.width / 4, screenSize.height / 4);
        applicationFrame.setExtendedState(applicationFrame.getExtendedState() | Frame.MAXIMIZED_BOTH);
        applicationFrame.setJMenuBar(MenuBuilder.createApplicationMenu(applicationActions, this));
        applicationFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        applicationFrame.addWindowListener(createFrameWindowListener());
        Desktop.getDesktop().disableSuddenTermination();
        try {
            Desktop.getDesktop().setQuitStrategy(QuitStrategy.CLOSE_ALL_WINDOWS);
        } catch (UnsupportedOperationException e) {
            // ignore, not available on all platforms
        }
    }

    private WindowListener createFrameWindowListener() {
        return new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                applicationActions.closeApplication();
            }
        };
    }

    private JLabel createContent(Container contentPane) {
        contentPane.setLayout(new BorderLayout());
        Container emptyContainer = new Container();
        contentPane.add(emptyContainer, BorderLayout.CENTER);
        currentContent = emptyContainer;
        contentPane.add(statusLabel, BorderLayout.PAGE_END);
        return statusLabel;
    }

    public void show() {
        applicationFrame.setVisible(true);
    }

    public void dispose() {
        applicationFrame.dispose();
    }

    private JFileChooser getDirectoryChooser() {
        if (directoryChooser == null) {
            directoryChooser = new JFileChooser();
            directoryChooser.setAcceptAllFileFilterUsed(false);
            directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            directoryChooser.setMultiSelectionEnabled(false);
        }
        return directoryChooser;
    }

    Path openDatabaseDialog() {
        getDirectoryChooser().setDialogType(JFileChooser.SAVE_DIALOG);
        int status = getDirectoryChooser().showDialog(applicationFrame, OPEN_DATABASE_TEXT);
        if (status == JFileChooser.APPROVE_OPTION) {
            return getDirectoryChooser().getSelectedFile().toPath();
        }
        return null;
    }

    private JFileChooser getFileChooser() {
        if (fileChooser == null) {
            fileChooser = new ApplicationJFileChooser();
            fileChooser.setMultiSelectionEnabled(false);
        }
        return fileChooser;
    }

    Path openFileDialog() {
        return showFileDialog(JFileChooser.OPEN_DIALOG, null);
    }

    Path openFileDialog(String approveButtonText) {
        return showFileDialog(JFileChooser.OPEN_DIALOG, approveButtonText);
    }

    Path saveFileDialog(String approveButtonText) {
        return showFileDialog(JFileChooser.SAVE_DIALOG, approveButtonText);
    }

    private Path showFileDialog(int dialogType, String approveButtonText) {
        JFileChooser fileChooser = getFileChooser();
        fileChooser.setDialogType(dialogType);
        int status = fileChooser.showDialog(applicationFrame, approveButtonText);
        if (status == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().toPath();
        }
        return null;
    }

    private void clearPane() {
        setPane(new Container());
    }

    void setPane(Container container) {
        applicationFrame.getContentPane().remove(currentContent);
        applicationFrame.getContentPane().add(currentContent = container, BorderLayout.CENTER);
        applicationFrame.getContentPane().validate();
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
    }

    void setApplicationDataStatus() {
        setStatus(applicationData.getStatus());
    }

    void showUnexpectedInterruptedException(InterruptedException e) {
        showErrorDialog("Unexpected Interrupted Exception", String.valueOf(e));
        e.printStackTrace();
    }

    public void showMessageDialog(String title, String message) {
        JOptionPane.showMessageDialog(applicationFrame, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    void showErrorDialog(String title, String message) {
        JOptionPane.showMessageDialog(applicationFrame, message, title, JOptionPane.ERROR_MESSAGE);
    }

    Boolean showYesNoDialog(String title, String message) {
        int val = JOptionPane.showConfirmDialog(applicationFrame, message, title, JOptionPane.YES_NO_OPTION);
        return switch (val) {
            case JOptionPane.YES_OPTION -> Boolean.TRUE;
            case JOptionPane.NO_OPTION -> Boolean.FALSE;
            case JOptionPane.CLOSED_OPTION -> null;
            default -> {
                try {
                    throw new NullPointerException("Unknown return type " + val);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                yield null;
            }
        };
    }

    public String showInputDialog(String title, String message, String initialValue) {
        if (initialValue == null) {
            initialValue = "";
        }
        Object result = JOptionPane.showInputDialog(applicationFrame, message, title, JOptionPane.PLAIN_MESSAGE, null,
                null, initialValue);
        if (result == null) {
            return null;
        }
        return result.toString();
    }

    private static class ApplicationJFileChooser extends JFileChooser {

        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public void approveSelection() {
            if (getDialogType() == JFileChooser.SAVE_DIALOG && getSelectedFile().exists()) {
                int option = JOptionPane.showConfirmDialog(this,
                        String.format("The file %s already exists. Do you want to overwrite?", getSelectedFile()),
                        "Overwrite Existing File?", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.NO_OPTION || option == JOptionPane.CLOSED_OPTION) {
                    return;
                }
            }
            super.approveSelection();
        }
    }
}
