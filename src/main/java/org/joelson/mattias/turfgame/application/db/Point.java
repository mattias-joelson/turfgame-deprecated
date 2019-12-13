package org.joelson.mattias.turfgame.application.db;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "points")
public class Point {
    
    @Id
    private int id;
    
    private int tp;
    
    private int pph;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getTp() {
        return tp;
    }
    
    public void setTp(int tp) {
        this.tp = tp;
    }
    
    public int getPph() {
        return pph;
    }
    
    public void setPph(int pph) {
        this.pph = pph;
    }

    public String toString() {
        return String.format("Point[id %d, tp %d, pph %d]",
                id, tp, pph);
    }
}
