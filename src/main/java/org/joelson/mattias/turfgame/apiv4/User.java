package org.joelson.mattias.turfgame.apiv4;

import org.joelson.mattias.turfgame.util.JSONNumber;
import org.joelson.mattias.turfgame.util.JSONObject;
import org.joelson.mattias.turfgame.util.JSONString;

public class User {
    
    private static final String NAME = "name";
    private static final String ID = "id";
    public static final String REGION = "region";
    public static final String UNIQUE_ZONES_TAKEN = "uniqueZonesTaken";
    public static final String POINTS_PER_HOUR = "pointsPerHour";
    public static final String RANK = "rank";
    public static final String TOTAL_POINTS = "totalPoints";
    public static final String TAKEN = "taken";
    public static final String POINTS = "points";
    public static final String PLACE = "place";
    public static final String COUNTRY = "country";
    
    private final String name;
    private final int id;
    private final Region region;
    private final int uniqueZonesTaken;
    private final int pointsPerHour;
    private final int rank;
    private final int totalPoints;
    private final int taken;
    private final int points;
    private final int place;
    private final String country;
    // TODO medals
    // TODO zones
    
    public User(String name, int id, Region region, int uniqueZonesTaken, int pointsPerHour, int rank, int totalPoints, int taken, int points, int place,
            String country) {
        this.name = name;
        this.id = id;
        this.region = region;
        this.uniqueZonesTaken = uniqueZonesTaken;
        this.pointsPerHour = pointsPerHour;
        this.rank = rank;
        this.totalPoints = totalPoints;
        this.taken = taken;
        this.points = points;
        this.place = place;
        this.country = country;
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
    
    public int getUniqueZonesTaken() {
        return uniqueZonesTaken;
    }
    
    public int getPointsPerHour() {
        return pointsPerHour;
    }
    
    public int getRank() {
        return rank;
    }
    
    public int getTotalPoints() {
        return totalPoints;
    }
    
    public int getTaken() {
        return taken;
    }
    
    public int getPoints() {
        return points;
    }
    
    public int getPlace() {
        return place;
    }
    
    public String getCountry() {
        return country;
    }

    static User fromJSON(JSONObject obj) {
        JSONString name = (JSONString) obj.getValue(NAME);
        JSONNumber id = (JSONNumber) obj.getValue(ID);
        Region region = null;
        if (obj.containsName(REGION)) {
            region = Region.fromJSON((JSONObject) obj.getValue(REGION));
        }
        JSONNumber uniqueZonesTaken = (JSONNumber) obj.getValue(UNIQUE_ZONES_TAKEN);
        JSONNumber pointsPerHour = (JSONNumber) obj.getValue(POINTS_PER_HOUR);
        JSONNumber rank = (JSONNumber) obj.getValue(RANK);
        JSONNumber totalPoints = (JSONNumber) obj.getValue(TOTAL_POINTS);
        JSONNumber taken = (JSONNumber) obj.getValue(TAKEN);
        JSONNumber points = (JSONNumber) obj.getValue(POINTS);
        JSONNumber place = (JSONNumber) obj.getValue(PLACE);
        JSONString country = (JSONString) obj.getValue(COUNTRY);
        
        return new User(name.stringValue(), id.intValue(), region, uniqueZonesTaken.intValue(), pointsPerHour.intValue(), rank.intValue(),
                totalPoints.intValue(), taken.intValue(), points.intValue(), place.intValue(), country.stringValue());
    }
}
