package org.joelson.mattias.turfgame.apiv4;

import org.joelson.mattias.turfgame.util.JSONNumber;
import org.joelson.mattias.turfgame.util.JSONObject;
import org.joelson.mattias.turfgame.util.JSONString;

public final class Zone {

    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String REGION = "region";
    private static final String LATITIUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String DATE_CREATED = "dateCreated";
    private static final String TAKEOVER_POINTS = "takeoverPoints";
    private static final String POINTS_PER_HOUR = "pointsPerHour";
    private static final String TOTAL_TAKEOVERS = "totalTakeovers";

    private final String name;
    private final int id;
    private final Region region;
    private final double latitude;
    private final double longitude;
    private final String dateCreated;
    private final int takeoverPoints;
    private final int pointsPerHour;
    private final int totalTakeovers;

    private Zone(String name, int id, Region region, double latitude, double longitude, String dateCreated,
                 int takeoverPoints, int pointsPerHour, int totalTakeovers) {
        this.name = name;
        this.id = id;
        this.region = region;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dateCreated = dateCreated;
        this.takeoverPoints = takeoverPoints;
        this.pointsPerHour = pointsPerHour;
        this.totalTakeovers = totalTakeovers;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Region getRegion() {
        return region;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public int getTakeoverPoints() {
        return takeoverPoints;
    }

    public int getPointsPerHour() {
        return pointsPerHour;
    }

    public int getTotalTakeovers() {
        return totalTakeovers;
    }

    static Zone fromJSON(JSONObject obj) {
        JSONString name = (JSONString) obj.getValue(NAME);
        JSONNumber id = (JSONNumber) obj.getValue(ID);
        JSONObject region = (JSONObject) obj.getValue(REGION);
        JSONNumber latitude = (JSONNumber) obj.getValue(LATITIUDE);
        JSONNumber longitude = (JSONNumber) obj.getValue(LONGITUDE);
        JSONNumber takeoverPoints = (JSONNumber) obj.getValue(TAKEOVER_POINTS);
        JSONNumber pointsPerHour = (JSONNumber) obj.getValue(POINTS_PER_HOUR);
        JSONNumber totalTakeovers = (JSONNumber) obj.getValue(TOTAL_TAKEOVERS);
        if (obj.containsName(DATE_CREATED)) {
            JSONString dateCreated = (JSONString) obj.getValue(DATE_CREATED);
            return new Zone(name.stringValue(), id.intValue(), Region.fromJSON(region),
                    latitude.doubleValue(), longitude.doubleValue(), dateCreated.stringValue(),
                    takeoverPoints.intValue(), pointsPerHour.intValue(), totalTakeovers.intValue());
        }
        return new Zone(name.stringValue(), id.intValue(), Region.fromJSON(region),
                latitude.doubleValue(), longitude.doubleValue(), null,
                takeoverPoints.intValue(), pointsPerHour.intValue(), totalTakeovers.intValue());
    }
}
