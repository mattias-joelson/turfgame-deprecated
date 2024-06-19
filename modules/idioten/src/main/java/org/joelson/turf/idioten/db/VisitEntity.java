package org.joelson.turf.idioten.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import org.joelson.turf.idioten.model.RevisitData;
import org.joelson.turf.idioten.model.TakeData;
import org.joelson.turf.idioten.model.VisitData;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "visits", indexes = { @Index(columnList = "id", unique = true), @Index(columnList = "zone_id"),
        @Index(columnList = "user_id"), @Index(columnList = "time") }, uniqueConstraints = {
        @UniqueConstraint(name = "UniqueZoneAndTime", columnNames = { "zone_id", "time" }) })
public class VisitEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private int id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "zone_id", updatable = false, nullable = false)
    private ZoneEntity zone;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false, nullable = false)
    private UserEntity user;

    @NotNull
    @Column(updatable = false, nullable = false)
    private Instant time;

    @NotNull
    @Column(updatable = false, nullable = false)
    private VisitType type;

    public VisitEntity() {
    }

    static VisitEntity build(
            @NotNull ZoneEntity zone, @NotNull UserEntity user, @NotNull Instant time, @NotNull VisitType type) {
        VisitEntity visitEntity = new VisitEntity();
        visitEntity.setZone(zone);
        visitEntity.setUser(user);
        visitEntity.setTime(time);
        visitEntity.setType(type);
        return visitEntity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public @NotNull ZoneEntity getZone() {
        return zone;
    }

    public void setZone(@NotNull ZoneEntity zone) {
        this.zone = Objects.requireNonNull(zone);
    }

    public @NotNull UserEntity getUser() {
        return user;
    }

    public void setUser(@NotNull UserEntity user) {
        this.user = Objects.requireNonNull(user);
    }

    public @NotNull Instant getTime() {
        return time;
    }

    public void setTime(@NotNull Instant time) {
        this.time = Objects.requireNonNull(time);
    }

    public @NotNull VisitType getType() {
        return type;
    }

    public void setType(@NotNull VisitType type) {
        this.type = Objects.requireNonNull(type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof VisitEntity that) {
            return id == that.id && Objects.equals(zone, that.zone) && Objects.equals(user, that.user)
                    && Objects.equals(time, that.time) && type == that.type;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, zone, user, time, type);
    }

    @Override
    public String toString() {
        return String.format("VisitEntity[%s]", EntityUtil.toStringPart(this));
    }

    public VisitData toData() {
        return switch (type) {
            case TAKE -> new TakeData(zone.toData(), user.toData(), time);
            case REVISIT -> new RevisitData(zone.toData(), user.toData(), time);
        };
    }
}
