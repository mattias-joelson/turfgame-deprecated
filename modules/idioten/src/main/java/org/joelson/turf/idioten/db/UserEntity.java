package org.joelson.turf.idioten.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.joelson.turf.idioten.model.UserData;
import org.joelson.turf.util.StringUtil;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "users",
        indexes = { @Index(columnList = "id", unique = true), @Index(columnList = "name", unique = true) })
public class UserEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(updatable = false, nullable = false)
    private int id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private Instant time;

    public UserEntity() {
    }

    public static UserEntity build(int id, @NotNull String name, @NotNull Instant time) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setName(name);
        userEntity.setTime(time);
        return userEntity;
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
        this.name = StringUtil.requireNotNullAndNotEmpty(name, "Name can not be null", "Name can not be empty");
    }

    public @NotNull Instant getTime() {
        return time;
    }

    public void setTime(@NotNull Instant time) {
        this.time = Objects.requireNonNull(time);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof UserEntity that) {
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
        return String.format("UserEntity[%s]", EntityUtil.toStringPart(this));
    }

    public UserData toData() {
        return new UserData(id, name);
    }
}
