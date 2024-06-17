package org.joelson.turf.idioten.db;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class AssistId implements Serializable {

    @Serial
    private static final long serialVersionUID = 1;

    private VisitEntity visit;
    private UserEntity user;

    public AssistId() {
    }

    public AssistId(VisitEntity visit, UserEntity user) {
        this.visit = Objects.requireNonNull(visit);
        this.user = Objects.requireNonNull(user);
    }

    public VisitEntity getVisit() {
        return visit;
    }

    public void setVisit(VisitEntity visit) {
        this.visit = Objects.requireNonNull(visit);
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = Objects.requireNonNull(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof AssistId that) {
            return Objects.equals(visit, that.visit) && Objects.equals(user, that.user);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(visit, user);
    }
}
