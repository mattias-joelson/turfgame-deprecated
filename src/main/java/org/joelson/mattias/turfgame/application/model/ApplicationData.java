package org.joelson.mattias.turfgame.application.model;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.file.Path;

public class ApplicationData {
    
    private boolean changed;
    private Path savePath;
    private UserData currentUser;
    private ZoneData zones;
    
    public ApplicationData() {
    }
    
    public boolean isChanged() {
        return changed;
    }
    
    public void setChanged() {
        changed = true;
    }
    
    public void clearChanged() {
        changed = false;
    }

    public Path getSavePath() {
        return savePath;
    }
    
    public void setSavePath(Path savePath) {
        this.savePath = savePath;
    }

    public UserData getCurrentUser() {
        return currentUser;
    }
    
    public void setCurrentUser(UserData userData) {
        currentUser = userData;
        changed = true;
    }
    
    public ZoneData getZones() {
        return zones;
    }
    
    public void setZones(ZoneData zones) {
        this.zones = zones;
    }

    /*public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(1);
        currentUser.writeExternal(out);
        zones.writeExternal(out);
    }
    
    public static ApplicationData readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int version = in.readInt();
        ApplicationData applicationData = new ApplicationData();
        applicationData.setCurrentUser(UserData.readExternal(in));
        applicationData.setZones(ZoneData.readExternal(in));
        applicationData.clearChanged();
        return applicationData;
    }*/
    
    public String getStatus() {
        return String.format("User %s, Zones %s",
                (getCurrentUser() != null) ? getCurrentUser().getUsername() : "<no user>",
                (getZones() != null) ? getZones().getZones().size() : "<no zones>");
    }
}
