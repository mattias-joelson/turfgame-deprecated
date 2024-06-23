package org.joelson.turf.idioten.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.joelson.turf.idioten.model.UserProgressData;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Entity
@Table(name = "user_progress",
        indexes = { @Index(columnList = "user_id"), @Index(columnList = "type"), @Index(columnList = "date") })
public class UserProgressEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1;

    @Id
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false, nullable = false)
    private UserEntity user;

    @Id
    @NotNull
    @Column(updatable = false, nullable = false)
    private UserProgressType type;

    @Id
    @NotNull
    @Column(updatable = false, nullable = false)
    private Instant date;

    @NotNull
    @Column(name = "previous_day_completed", updatable = false, nullable = false)
    private int previousDayCompleted;

    @NotNull
    @Column(name = "day_completed", nullable = false)
    private int dayCompleted;

    @NotNull
    @Column(name = "time_completed", nullable = false)
    private Instant timeCompleted;

    public UserProgressEntity() {
    }

    public static UserProgressEntity build(
            @NotNull UserEntity user, @NotNull UserProgressType type, @NotNull Instant date,
            @NotNull int previousDayCompleted, @NotNull int dayCompleted, @NotNull Instant timeCompleted) {
        UserProgressEntity userProgress = new UserProgressEntity();
        userProgress.setUser(user);
        userProgress.setType(type);
        userProgress.setDate(date);
        userProgress.setPreviousDayCompleted(previousDayCompleted);
        userProgress.setDayCompleted(dayCompleted);
        userProgress.setTimeCompleted(timeCompleted);
        return userProgress;
    }

    private static Instant instantTruncatedToSecond(Instant instant) {
        if (!instant.truncatedTo(ChronoUnit.SECONDS).equals(instant)) {
            throw new IllegalArgumentException(String.format("Instant %s not truncated to second.", instant));
        }
        return instant;
    }

    private static Instant instantTruncatedToDay(Instant instant) {
        if (!instant.truncatedTo(ChronoUnit.DAYS).equals(instant)) {
            throw new IllegalArgumentException(String.format("Instant %s not truncated to second.", instant));
        }
        return instant;
    }

    public @NotNull UserEntity getUser() {
        return user;
    }

    public void setUser(@NotNull UserEntity user) {
        this.user = Objects.requireNonNull(user);
    }

    public @NotNull UserProgressType getType() {
        return type;
    }

    public void setType(@NotNull UserProgressType type) {
        this.type = Objects.requireNonNull(type);
    }

    public @NotNull Instant getDate() {
        return date;
    }

    public void setDate(@NotNull Instant date) {
        this.date = instantTruncatedToDay(Objects.requireNonNull(date));
    }

    @NotNull
    public int getPreviousDayCompleted() {
        return previousDayCompleted;
    }

    public void setPreviousDayCompleted(@NotNull int previousDayCompleted) {
        this.previousDayCompleted = previousDayCompleted;
    }

    @NotNull
    public int getDayCompleted() {
        return dayCompleted;
    }

    public void setDayCompleted(@NotNull int dayCompleted) {
        this.dayCompleted = dayCompleted;
    }

    public @NotNull Instant getTimeCompleted() {
        return timeCompleted;
    }

    public void setTimeCompleted(@NotNull Instant timeCompleted) {
        this.timeCompleted = instantTruncatedToSecond(Objects.requireNonNull(timeCompleted));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof UserProgressEntity that) {
            return Objects.equals(user, that.user) && type == that.type && Objects.equals(date, that.date)
                    && previousDayCompleted == that.previousDayCompleted && dayCompleted == that.dayCompleted
                    && Objects.equals(timeCompleted, that.timeCompleted);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, type, date);
    }

    @Override
    public String toString() {
        return String.format("UserProgressEntity[%s]", EntityUtil.toStringPart(this));
    }

    public UserProgressData toData() {
        return new UserProgressData(user.toData(), type, date, previousDayCompleted, dayCompleted, timeCompleted);
    }
}
