package org.joelson.turf.idioten.db;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class UserVisitsId implements Serializable {

    @Serial
    private static final long serialVersionUID = 1;

    private UserEntity user;
    private Instant date;

    public UserVisitsId() {
    }

    public UserVisitsId(UserEntity user, Instant date) {
        this.user = Objects.requireNonNull(user);
        this.date = Objects.requireNonNull(date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof UserVisitsId that) {
            return Objects.equals(user, that.user) && Objects.equals(date, that.date);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, date);
    }
}
