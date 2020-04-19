package org.joelson.mattias.turfgame.application.view;

import org.joelson.mattias.turfgame.application.controller.ApplicationActions;
import org.joelson.mattias.turfgame.application.model.ApplicationData;

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
import java.nio.file.Path;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class ApplicationUI {
    
    private static final String OPEN_DATABASE_TEXT = "Open Database";
    private static final String APPLICATION_TITLE = "Turf Statistics";
    private final ApplicationActions applicationActions;
    private final ApplicationData applicationData;
    
    private final JFrame appplicationFrame;
    private final JLabel statusLabel;
    private Container currentContent;
    private JFileChooser directoryChooser;
    private JFileChooser fileChooser;
    
    public ApplicationUI(ApplicationActions applicationActions, ApplicationData applicationData) {
        this.applicationActions = applicationActions;
        this.applicationData = applicationData;

        appplicationFrame = createApplicationFrame();
        statusLabel = createContent(appplicationFrame.getContentPane());
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

    Action readTodayFromFileAction() {
        return ReadTodayFromFileActionCreator.create(this);
    }

    Action readWardedFromFileAction() {
        return ReadWardedFromFileActionCreator.create(this);
    }

    Action zoneVisitTableAction() {
        return ZoneVisitTableActionCreator.create(this);
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
    
    private JFrame createApplicationFrame() {
        JFrame frame = new JFrame(APPLICATION_TITLE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width / 2, screenSize.height /2);
        frame.setLocation(screenSize.width / 4, screenSize.height / 4);
        frame.setExtendedState(frame.getExtendedState() | Frame.MAXIMIZED_BOTH);
        frame.setJMenuBar(MenuBuilder.createApplicationMenu(applicationActions, this));
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(createFrameWindowListener());
        Desktop.getDesktop().disableSuddenTermination();
        try {
            Desktop.getDesktop().setQuitStrategy(QuitStrategy.CLOSE_ALL_WINDOWS);
        } catch (UnsupportedOperationException e) {
            // ignore, not available on all platforms
        }
        return frame;
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
        JLabel statusLabel = new JLabel("<no user>"); //NON-NLS
        contentPane.add(statusLabel, BorderLayout.PAGE_END);
        return statusLabel;
    }
    
    public void show() {
        appplicationFrame.setVisible(true);
    }
    
    public void dispose() {
        appplicationFrame.dispose();
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
        int status = getDirectoryChooser().showDialog(appplicationFrame, OPEN_DATABASE_TEXT);
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
        int status = fileChooser.showDialog(appplicationFrame, approveButtonText);
        if (status == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().toPath();
        }
        return null;
    }
    
    private void clearPane() {
        setPane(new Container());
    }
    
    void setPane(Container container) {
        appplicationFrame.getContentPane().remove(currentContent);
        appplicationFrame.getContentPane().add(currentContent = container, BorderLayout.CENTER);
        appplicationFrame.getContentPane().validate();
    }
    
    public void setStatus(String status) {
        statusLabel.setText(status);
    }
    
    void setApplicationDataStatus() {
        setStatus(applicationData.getStatus());
    }
    
    void showUnexpectedInteruptionError(InterruptedException e) {
        showErrorDialog("Unexpected Interrupted Exception", String.valueOf(e));
        e.printStackTrace();
    }
    
    public void showMessageDialog(String title, String message) {
        JOptionPane.showMessageDialog(appplicationFrame, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    void showErrorDialog(String title, String message) {
        JOptionPane.showMessageDialog(appplicationFrame, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    Boolean showYesNoDialog(String title, String message) {
        int val = JOptionPane.showConfirmDialog(appplicationFrame, message, title, JOptionPane.YES_NO_OPTION);
        switch (val) {
            case JOptionPane.YES_OPTION:
                return Boolean.TRUE;
            case JOptionPane.NO_OPTION:
                return Boolean.FALSE;
            case JOptionPane.CLOSED_OPTION:
                return null;
            default:
                try {
                    throw new NullPointerException("Unknown return type " + val);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                return null;
        }
    }
    
    public String showInputDialog(String title, String message, String initialValue) {
        if (initialValue == null) {
            initialValue = "";
        }
        Object result = JOptionPane.showInputDialog(appplicationFrame, message, title, JOptionPane.PLAIN_MESSAGE, null, null, initialValue);
        if (result == null) {
            return null;
        }
        return result.toString();
    }
    
    private static class ApplicationJFileChooser extends JFileChooser {
        
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
