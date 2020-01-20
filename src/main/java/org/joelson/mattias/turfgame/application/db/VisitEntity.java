package org.joelson.mattias.turfgame.application.db;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "visits", uniqueConstraints = @UniqueConstraint(columnNames = { "take_id", "user_id"}))
public class VisitEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @ManyToOne(optional = false)
    TakeEntity take;
    
    @Id
    @ManyToOne(optional = false)
    UserEntity user;
    
    @Enumerated
    VisitType type;
    
    public VisitEntity() {
    }
    
    public TakeEntity getTake() {
        return take;
    }
    
    public void setTake(TakeEntity take) {
        this.take = Objects.requireNonNull(take, "Take can not be null!"); //NON-NLS
    }
    
    public UserEntity getUser() {
        return user;
    }
    
    public void setUser(UserEntity user) {
        this.user = Objects.requireNonNull(user, "Ussr can not be null!"); //NON-NLS
    }
    
    public VisitType getType() {
        return type;
    }
    
    public void setType(VisitType type) {
        this.type = Objects.requireNonNull(type, "Type can not be null!"); //NON-NLS
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof VisitEntity) {
            VisitEntity that = (VisitEntity) obj;
            return Objects.equals(take, that.take) && Objects.equals(user, that.user) && type == that.type;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(take, user, type);
    }
    
    @Override
    public String toString() {
        return String.format("VisitEntity{zone %s, when %s, user %s, type %s", //NON-NLS
                (take != null) ? EntityUtil.toStringPart(take.getZone()) : null, take.getWhen(), EntityUtil.toStringPart(user), type);
    }
    
    public static VisitEntity build(TakeEntity take, UserEntity user, VisitType type) {
        VisitEntity visit = new VisitEntity();
        visit.setTake(take);
        visit.setUser(user);
        visit.setType(type);
        return visit;
    }
}
