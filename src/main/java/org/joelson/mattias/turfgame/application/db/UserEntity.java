package org.joelson.mattias.turfgame.application.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.joelson.mattias.turfgame.application.model.UserData;
import org.joelson.mattias.turfgame.util.StringUtil;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "users") //NON-NLS
public class UserEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(updatable = false, nullable = false)
    private int id;
    
    @NotNull
    @Column(nullable = false)
    private String name;
    
    public UserEntity() {
    }
    
    public int getId() {
        return id;
    }
    
    private void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = StringUtil.requireNotNullAndNotEmpty(name, "Name can not be null", "Name can not be empty"); //NON-NLS
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof UserEntity) {
            UserEntity that = (UserEntity) obj;
            return id == that.id && Objects.equals(name, that.name);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return id;
    }
    
    @Override
    public String toString() {
        return String.format("UserEntity[id %d, name %s]", id, name); //NON-NLS
    }
    
    public UserData toData() {
        return new UserData(id, name);
    }

    public static UserEntity build(int id, @NotNull String name) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setName(name);
        return userEntity;
    }
}
