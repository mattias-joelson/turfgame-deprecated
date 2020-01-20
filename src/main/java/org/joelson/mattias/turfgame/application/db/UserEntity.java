package org.joelson.mattias.turfgame.application.db;

import com.sun.istack.NotNull;
import org.joelson.mattias.turfgame.application.model.UserData;
import org.joelson.mattias.turfgame.util.StringUtil;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users") //NON-NLS
public class UserEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(nullable = false)
    private int id;
    
    @NotNull
    @Column(nullable = false)
    private String name;
    
    public UserEntity() {
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
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
    
    public static UserData toData(UserEntity user) {
        return new UserData(user.getId(), user.getName());
    }

    public static UserEntity build(int id, @NotNull String name) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setName(name);
        return userEntity;
    }
}
