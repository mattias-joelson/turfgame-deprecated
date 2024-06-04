package org.joelson.mattias.turfgame.application;

import org.joelson.mattias.turfgame.application.controller.ApplicationActions;
import org.joelson.mattias.turfgame.application.model.ApplicationData;
import org.joelson.mattias.turfgame.application.view.ApplicationUI;

public class Application {
    
    private final ApplicationData applicationData;
    private final ApplicationActions applicationActions;
    private final ApplicationUI applicationUI;
    
    public Application() {
        applicationData = new ApplicationData();
        applicationActions = new ApplicationActions(applicationData);
        applicationUI = new ApplicationUI(applicationActions, applicationData);
        applicationUI.initUI();
        applicationActions.setApplicationUI(applicationUI);
    }
    
    public void show() {
        applicationUI.show();
    }
}
