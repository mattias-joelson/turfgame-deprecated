package org.joelson.mattias.turfgame.application.view;

import org.joelson.mattias.turfgame.application.controller.ApplicationActions;
import org.joelson.mattias.turfgame.application.model.ApplicationData;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.desktop.QuitStrategy;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.nio.file.Path;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class ApplicationUI {
    
    private static final String OPEN_DATABASE_TEXT = "Open Database";
    private final ApplicationActions applicationActions;
    private final ApplicationData applicationData;
    
    private final JFrame appplicationFrame;
    private final JLabel statusLabel;
    private Container currentContent;
    private JFileChooser directoryChooser;
    
    public ApplicationUI(ApplicationActions applicationActions, ApplicationData applicationData) {
        this.applicationActions = applicationActions;
        this.applicationData = applicationData;

        appplicationFrame = createApplicationFrame();
        statusLabel = createContent(appplicationFrame.getContentPane());
        clearPane();
    }
    
    private JFrame createApplicationFrame() {
        JFrame frame = new JFrame("Turf Statistics");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width / 2, screenSize.height /2);
        frame.setLocation(screenSize.width / 4, screenSize.height / 4);
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        frame.setJMenuBar(MenuBuilder.createApplicationMenu(applicationActions));
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
        contentPane.add(currentContent = emptyContainer, BorderLayout.CENTER);
        JLabel statusLabel = new JLabel("<no user>");
        contentPane.add(statusLabel, BorderLayout.SOUTH);
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
    
    public Path openDatabaseDialog() {
        getDirectoryChooser().setDialogType(JFileChooser.SAVE_DIALOG);
        int status = getDirectoryChooser().showDialog(appplicationFrame, OPEN_DATABASE_TEXT);
        if (status == JFileChooser.APPROVE_OPTION) {
            return getDirectoryChooser().getSelectedFile().toPath();
        }
        return null;
    }

    public void clearPane() {
        appplicationFrame.getContentPane().remove(currentContent);
        appplicationFrame.getContentPane().add(currentContent = new Container(), BorderLayout.CENTER);
        appplicationFrame.getContentPane().validate();
    }
    
    public void setStatus(String status) {
        statusLabel.setText(status);
    }
    
    public void showMessageDialog(String title, String message) {
        JOptionPane.showMessageDialog(appplicationFrame, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void showErrorDialog(String title, String message) {
        JOptionPane.showMessageDialog(appplicationFrame, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    public Boolean showYesNoDialog(String title, String message) {
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
                    throw new NullPointerException("Unknowm return type " + val);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                return null;
        }
    }
}
