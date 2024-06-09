package org.joelson.turf.idioten.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.joelson.turf.idioten.model.PlayerData;
import org.joelson.turf.util.StringUtil;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "idiot_players",
        indexes = { @Index(columnList = "id", unique = true), @Index(columnList = "name", unique = true) })
public class IdiotPlayerEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(updatable = false, nullable = false)
    private int id;

    @NotNull
    @Column(nullable = false)
    private String name;

    public IdiotPlayerEntity() {
    }

    public static IdiotPlayerEntity build(int id, @NotNull String name) {
        IdiotPlayerEntity playerEntity = new IdiotPlayerEntity();
        playerEntity.setId(id);
        playerEntity.setName(name);
        return playerEntity;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof IdiotPlayerEntity that) {
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
        return String.format("PlayerEntity[id %d, name %s]", id, name);
    }

    public PlayerData toData() {
        return new PlayerData(id, name);
    }
}
