package org.joelson.turf.application;

import org.joelson.turf.application.controller.ApplicationActions;
import org.joelson.turf.application.model.ApplicationData;
import org.joelson.turf.application.view.ApplicationUI;

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

    public static void main(String... args) {
        new Application().show();
    }

    public void show() {
        applicationUI.show();
    }
}
