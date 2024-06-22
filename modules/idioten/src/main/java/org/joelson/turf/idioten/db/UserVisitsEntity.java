package org.joelson.turf.idioten.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.joelson.turf.idioten.model.UserVisitsData;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Entity
@IdClass(UserVisitsId.class)
@Table(name = "users_visits", indexes = { @Index(columnList = "user_id"), @Index(columnList = "date") })
public class UserVisitsEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1;

    @Id
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false, nullable = false)
    private UserEntity user;

    @NotNull
    @Column(updatable = false, nullable = false)
    private Instant date;

    private int visits;

    public UserVisitsEntity() {
    }

    static UserVisitsEntity build(@NotNull UserEntity user, @NotNull Instant date, int visits) {
        UserVisitsEntity userVisits = new UserVisitsEntity();
        userVisits.setUser(user);
        userVisits.setDate(date);
        userVisits.setVisits(visits);
        return userVisits;
    }

    private static Instant instantTruncatedToDate(Instant instant) {
        if (!instant.truncatedTo(ChronoUnit.DAYS).equals(instant)) {
            throw new IllegalArgumentException(String.format("Instant %s not truncated to date.", instant));
        }
        return instant;
    }

    public @NotNull UserEntity getUser() {
        return user;
    }

    public void setUser(@NotNull UserEntity user) {
        this.user = Objects.requireNonNull(user);
    }

    public @NotNull Instant getDate() {
        return date;
    }

    public void setDate(@NotNull Instant date) {
        this.date = instantTruncatedToDate(Objects.requireNonNull(date));
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        if (visits < this.visits) {
            throw new IllegalArgumentException(String.format("Visits decreases from %d to %d.", this.visits, visits));
        }
        this.visits = visits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof UserVisitsEntity that) {
            return visits == that.visits && Objects.equals(user, that.user) && Objects.equals(date, that.date);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, date);
    }

    @Override
    public String toString() {
        return String.format("UserVisitsEntity[%s]", EntityUtil.toStringPart(this));
    }

    public UserVisitsData toData() {
        return new UserVisitsData(user.toData(), date, visits);
    }
}
