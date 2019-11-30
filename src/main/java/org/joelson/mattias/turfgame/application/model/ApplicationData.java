package org.joelson.mattias.turfgame.application.model;

import javax.management.NotificationListener;

public class ApplicationData {
    
    private UserData currentUser;
    
    public ApplicationData() {
        this(null);
    }
    
    public ApplicationData(UserData currentUser) {
        this.currentUser = currentUser;
    }
    
    public UserData getCurrentUser() {
        return currentUser;
    }
    
    public void setCurrentUser(UserData userData) {
        currentUser = userData;
    }
}
