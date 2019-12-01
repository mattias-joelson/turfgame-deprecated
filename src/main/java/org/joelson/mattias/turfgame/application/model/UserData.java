package org.joelson.mattias.turfgame.application.model;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class UserData {
    
    // Oberoff 80119
    
    private final String username;
    private final int userID;
    
    public UserData(String username, int userID) {
        this.username = username;
        this.userID = userID;
    }
    
    public String getUsername() {
        return username;
    }
    
    public int getUserID() {
        return userID;
    }
    
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(1);
        out.writeObject(username);
        out.writeObject(userID);
    
    }
    
    public static UserData readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int version = in.readInt();
        String username = (String) in.readObject();
        int userID = in.readInt();
        return new UserData(username, userID);
    }
}
