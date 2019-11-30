package org.joelson.mattias.turfgame.application.model;

public class UserData {
    
    // Oberoff 80119
    
    private final String username;
    private final int userID;
    
    public UserData(final String username, final int userID) {
        this.username = username;
        this.userID = userID;
    }
    
    public String getUsername() {
        return username;
    }
    
    public int getUserID() {
        return userID;
    }
}
