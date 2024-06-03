package org.joelson.mattias.turfgame.application.model;

import org.joelson.mattias.turfgame.util.StringUtil;

import java.util.Objects;

public class UserData {
    
    // Oberoff 80119
    
    private final int id;
    private final String name;
    
    public UserData(int id, String name) {
        this.id = id;
        this.name = StringUtil.requireNotNullAndNotEmpty(name, "Name can not be null", "Name can not be empty"); //NON-NLS;
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof UserData) {
            UserData that = (UserData) obj;
            return id == that.id && Objects.equals(name, that.name);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
    
    @Override
    public String toString() {
        return String.format("UserData[id %d, name %s]", id, name); //NON-NLS
    }
}
