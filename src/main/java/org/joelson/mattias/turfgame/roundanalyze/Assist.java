package org.joelson.mattias.turfgame.roundanalyze;

import java.time.LocalDateTime;

public class Assist extends Visit {

    private final boolean neutralized;

    public Assist(LocalDateTime time, int zoneId, int tp, boolean neutralized) {
        super(time, zoneId, tp + ((neutralized) ? 50 : 0));
        this.neutralized = neutralized;
    }

    public boolean isNeutralized() {
        return neutralized;
    }
}
