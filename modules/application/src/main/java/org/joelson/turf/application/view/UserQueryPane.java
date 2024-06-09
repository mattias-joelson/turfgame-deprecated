package org.joelson.mattias.turfgame.application.view;

/*import org.joelson.mattias.turfgame.application.controller.UserActions;

import java.awt.Container;
import java.awt.event.ActionEvent;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
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
        GroupLayout layout = new GroupLayout(pane);
        pane.setLayout(layout);
        JLabel usernameLabel = new JLabel("Username");
        usernameField = new JTextField();
        usernameField.addActionListener(this::selectUser);
        JLabel userIDLabel = new JLabel("User ID");
        userIDField = new JTextField();
        userIDField.addActionListener(this::selectUser);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this::cancelUserSelection);
        JButton okButton = new JButton("OK");
        okButton.addActionListener(this::selectUser);
        
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(usernameLabel).addComponent(userIDLabel))
                        .addGroup(layout.createParallelGroup(Alignment.TRAILING).addComponent(usernameField).addComponent(userIDField)
                                .addGroup(layout.createSequentialGroup().addComponent(cancelButton).addComponent(okButton)))
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(usernameLabel).addComponent(usernameField))
                        .addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(userIDLabel).addComponent(userIDField))
                        .addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(cancelButton).addComponent(okButton))
        );
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
}*/
