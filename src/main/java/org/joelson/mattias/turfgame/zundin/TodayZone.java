package org.joelson.mattias.turfgame.zundin;

public class TodayZone {

    private final String date;
    private final String eagerTime;
    private final String zoneName;
    private final int zoneId;
    private final String areaName;
    private final int tp;
    private final int pph;
    private final String activity;
    private final String userId;
    private final int take;

    public TodayZone(String date, String eagerTime, String zoneName, int zoneId, String areaName,
                     int tp, int pph, String activity, String userId, int take) {
        this.date = date;
        this.eagerTime = eagerTime;
        this.zoneName = zoneName;
        this.zoneId = zoneId;
        this.areaName = areaName;
        this.tp = tp;
        this.pph = pph;
        this.activity = activity;
        this.userId = userId;
        this.take = take;
    }

    public String getDate() {
        return date;
    }

    public String getEagerTime() {
        return eagerTime;
    }

    public String getZoneName() {
        return zoneName;
    }

    public int getZoneId() {
        return zoneId;
    }

    public String getAreaName() {
        return areaName;
    }

    public int getTP() {
        return tp;
    }

    public int getPPH() {
        return pph;
    }

    public String getActivity() {
        return activity;
    }

    public String getUserId() {
        return userId;
    }

    public int getTake() {
        return take;
    }

    public static TodayZone fromHTML(String html) {
        return null;
    }
}
