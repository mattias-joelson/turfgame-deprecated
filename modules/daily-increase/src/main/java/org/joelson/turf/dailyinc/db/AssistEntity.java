package org.joelson.turf.dailyinc.db;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.joelson.turf.dailyinc.model.AssistData;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@IdClass(AssistId.class)
@Table(name = "assists", indexes = { @Index(columnList = "visit_id"), @Index(columnList = "user_id") })
public class AssistEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @ManyToOne
    @JoinColumn(name = "visit_id", updatable = false, nullable = false)
    private VisitEntity visit;

    @Id
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false, nullable = false)
    private UserEntity user;

    public AssistEntity() {
    }

    static AssistEntity build(@NotNull VisitEntity visit, @NotNull UserEntity user) {
        AssistEntity assistEntity = new AssistEntity();
        assistEntity.setVisit(Objects.requireNonNull(visit));
        assistEntity.setUser(Objects.requireNonNull(user));
        return assistEntity;
    }

    public @NotNull VisitEntity getVisit() {
        return visit;
    }

    public void setVisit(@NotNull VisitEntity visit) {
        this.visit = visit;
    }

    public @NotNull UserEntity getUser() {
        return user;
    }

    public void setUser(@NotNull UserEntity user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof AssistEntity that) {
            return Objects.equals(visit, that.visit) && Objects.equals(user, that.user);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(visit, user);
    }

    @Override
    public String toString() {
        return String.format("AssistEntity[%s]", EntityUtil.toStringPart(this));
    }

    public AssistData toData() {
        return new AssistData(getVisit().getZone().toData(), getUser().toData(), getVisit().getTime(),
                getVisit().getUser().toData());
    }
}
