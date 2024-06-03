package org.joelson.mattias.turfgame.animate;

public class SummedScore {

    private final int score;
    private final int takes;
    private final int ouZones;
    private final int zonesOwned;
    private final int zonePPH;

    public SummedScore(int score, int takes, int ouZones, int zonesOwned, int zonePPH) {
        this.score = score;
        this.takes = takes;
        this.ouZones = ouZones;
        this.zonesOwned = zonesOwned;
        this.zonePPH = zonePPH;
    }
}
