package org.joelson.mattias.turfgame.roundanalyze;

import java.time.Duration;
import java.time.LocalDateTime;

public class Take extends Visit {

    private final boolean neutralized;
    private final int pph;
    private LocalDateTime loss;
    private int totalPph;

    public Take(LocalDateTime time, int zoneId, int tp, boolean neutralized, int pph) {
        super(time, zoneId, tp + ((neutralized) ? 50 : 0));
        this.neutralized = neutralized;
        this.pph = pph;
    }

    public void lost(LocalDateTime loss) {
        this.loss = loss;
        Duration duration = Duration.between(getTime(), loss);
        long hours = duration.toHours();
        long seconds = duration.getSeconds() - hours * 3600;
        totalPph = (int) (pph * hours + seconds * pph / 3600);
    }

    public int getPoints() {
        if (loss == null) {
            System.err.println("Has not lost zone " + getZoneId());
        }
        return super.getPoints() + totalPph;
    }

    public boolean isNeutralized() {
        return neutralized;
    }
}
