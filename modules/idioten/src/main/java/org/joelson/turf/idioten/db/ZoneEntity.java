package org.joelson.turf.idioten.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.joelson.turf.idioten.model.ZoneData;
import org.joelson.turf.util.StringUtil;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "zones",
        indexes = { @Index(columnList = "id", unique = true), @Index(columnList = "name", unique = true) })
public class ZoneEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(updatable = false, nullable = false)
    private int id;

    @NotNull
    @Column(nullable = false)
    private String name;

    public ZoneEntity() {
    }

    static ZoneEntity build(int id, @NotNull String name) {
        ZoneEntity zoneEntity = new ZoneEntity();
        zoneEntity.setId(id);
        zoneEntity.setName(name);
        return zoneEntity;
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = StringUtil.requireNotNullAndNotEmpty(name, "Name can not be null", "Name can not be empty");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ZoneEntity that) {
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
        return String.format("ZoneEntity[id %d, name %s]", id, name);
    }

    public ZoneData toData() {
        return new ZoneData(id, name);
    }
}
