package org.joelson.mattias.turfgame.application.controller;

import org.joelson.mattias.turfgame.application.model.ApplicationData;
import org.joelson.mattias.turfgame.application.view.ApplicationUI;

import javax.swing.Action;

public class ApplicationActions {
    
    private ApplicationUI applicationUI;
    private ApplicationData applicationData;
    
    public ApplicationActions(ApplicationData applicationData) {
        this.applicationData = applicationData;
    }
    
    public void setApplicationUI(ApplicationUI applicationUI) {
        this.applicationUI = applicationUI;
    }
    
    ApplicationData getApplicationData() {
        return applicationData;
    }
    
    ApplicationUI getApplicationUI() {
        return applicationUI;
    }
    
    public void closeApplication() {
        applicationData.closeDatabase();
        applicationUI.dispose();
        System.exit(0);
    }
    
    public Action readZonesAction() {
        return ReadZonesActionCreator.create(this);
    }
    
    public Action readZonesFromFileAction() {
        return ReadZonesFromFileActionCreator.create(this);
    }

    public Action openDatabaseAction() {
        return OpenDatabaseActionCreator.create(this);
    }
    
    public void closeDatabase() {
        applicationData.closeDatabase();
        applicationUI.setStatus(applicationData.getStatus());
    }
    
    public Action importDatabaseAction() {
        return ImportDatabaseActionCreator.create(this);
    }
    
    
    public Action exportDatabaseAction() {
        return ExportDatabaseActionCreator.create(this);
    }
}
