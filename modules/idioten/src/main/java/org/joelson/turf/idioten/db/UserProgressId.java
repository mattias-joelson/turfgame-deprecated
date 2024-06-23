package org.joelson.turf.idioten.db;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class UserProgressId implements Serializable {

    @Serial
    private static final long serialVersionUID = 1;

    private UserEntity user;
    private UserProgressType type;
    private Instant date;

    public UserProgressId() {
    }

    public UserProgressId(UserEntity user, UserProgressType type, Instant date) {
        this.user = Objects.requireNonNull(user);
        this.type = Objects.requireNonNull(type);
        this.date = Objects.requireNonNull(date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof UserProgressId that) {
            return Objects.equals(user, that.user) && type == that.type && Objects.equals(date, that.date);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, type, date);
    }
}
