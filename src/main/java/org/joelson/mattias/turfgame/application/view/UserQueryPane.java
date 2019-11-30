package org.joelson.mattias.turfgame.application.view;

import org.joelson.mattias.turfgame.application.controller.UserActions;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UserQueryPane {
    
    
    private final UserActions userActions;
    private final Container pane;
    private final JTextField usernameField;
    private final JTextField userIDField;
    
    public UserQueryPane(UserActions userActions) {
        this.userActions = userActions;
        
        pane = new JPanel();
        pane.setLayout(new BorderLayout());
        Container fieldPanel = new JPanel();
        pane.add(fieldPanel, BorderLayout.CENTER);
        fieldPanel.setLayout(new GridLayout(2,2));
        fieldPanel.add(new JLabel("Username"));
        fieldPanel.add(usernameField = new JTextField());
        usernameField.addActionListener(this::selectUser);
        fieldPanel.add(new JLabel("User ID"));
        fieldPanel.add(userIDField = new JTextField());
        userIDField.addActionListener(this::selectUser);
        Container buttonPanel = new JPanel();
        pane.add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(cancelButton);
        cancelButton.addActionListener(this::cancelUserSelection);
        JButton okButton = new JButton("OK");
        buttonPanel.add(okButton);
        okButton.addActionListener(this::selectUser);
    }
    
    private void selectUser(ActionEvent actionEvent) {
        userActions.userSelected(usernameField.getText(), userIDField.getText());
    }

    private void cancelUserSelection(ActionEvent actionEvent) {
        userActions.cancelUserSelecton();
    }
    
    public Container getPane() {
        return pane;
    }
    
    public void requestFocus() {
        usernameField.requestFocus();
    }
}
