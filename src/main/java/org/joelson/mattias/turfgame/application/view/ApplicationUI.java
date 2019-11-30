package org.joelson.mattias.turfgame.application.view;

import org.joelson.mattias.turfgame.application.controller.ApplicationActions;
import org.joelson.mattias.turfgame.application.controller.UserActions;
import org.joelson.mattias.turfgame.application.model.ApplicationData;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.function.Consumer;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class ApplicationUI {
    
    private final ApplicationActions applicationActions;
    private final ApplicationData applicationData;
    
    private final JFrame appplicationFrame;
    private final JLabel statusLabel;
    private Container currentContent;
    
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
        frame.setJMenuBar(MenuBuilder.createApplicationMenu(applicationActions));
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(createFrameWindowListener());
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

    public void clearPane() {
        appplicationFrame.getContentPane().remove(currentContent);
        appplicationFrame.getContentPane().add(currentContent = new Container(), BorderLayout.CENTER);
        appplicationFrame.getContentPane().validate();
    }
    
    public void showUserQueryPane(UserActions userActions) {
        appplicationFrame.getContentPane().remove(currentContent);
        UserQueryPane pane = new UserQueryPane(userActions);
        appplicationFrame.getContentPane().add(currentContent = pane.getPane(), BorderLayout.CENTER);
        appplicationFrame.getContentPane().validate();
        pane.requestFocus();
    }
    
    public void setStatus(String status) {
        statusLabel.setText(status);
    }
    
    public void showMessageDialog(String message, String title) {
        JOptionPane.showMessageDialog(appplicationFrame, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void showErrorDialog(String message, String title) {
        JOptionPane.showMessageDialog(appplicationFrame, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
