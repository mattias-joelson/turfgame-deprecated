package org.joelson.mattias.turfgame.application.controller;

import org.joelson.mattias.turfgame.application.model.ApplicationData;
import org.joelson.mattias.turfgame.application.view.ApplicationUI;

import javax.swing.JOptionPane;

public class ApplicationActions {
    
    private ApplicationUI applicationUI;
    private final ApplicationData applicationData;
    
    public ApplicationActions(ApplicationData applicationData) {
        this.applicationData = applicationData;
    }
    
    public void setApplicationUI(ApplicationUI applicationUI) {
        this.applicationUI = applicationUI;
    }
    
    public void closeApplication() {
        applicationUI.dispose();
        // TODO save data
        System.exit(0);
    }
    
    public void changeUser() {
        applicationUI.showUserQueryPane(new UserActions(applicationUI, applicationData));
    }
    
    public void loadUser() {
        applicationUI.showMessageDialog("Load user not implemented yet.", "Load user");
    }
}
