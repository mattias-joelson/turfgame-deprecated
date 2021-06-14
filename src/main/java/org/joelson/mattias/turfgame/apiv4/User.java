package org.joelson.mattias.turfgame.apiv4;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    
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

    @JsonCreator
    public User(
            @JsonProperty("name") String name,
            @JsonProperty("id") int id,
            @JsonProperty("region") Region region,
            @JsonProperty("uniqueZonesTaken") int uniqueZonesTaken,
            @JsonProperty("pointsPerHour") int pointsPerHour,
            @JsonProperty("rank") int rank,
            @JsonProperty("totalPoints") int totalPoints,
            @JsonProperty("taken") int taken,
            @JsonProperty("points") int points,
            @JsonProperty("place") int place,
            @JsonProperty("country") String country) {
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

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", region=" + region +
                ", uniqueZonesTaken=" + uniqueZonesTaken +
                ", pointsPerHour=" + pointsPerHour +
                ", rank=" + rank +
                ", totalPoints=" + totalPoints +
                ", taken=" + taken +
                ", points=" + points +
                ", place=" + place +
                ", country='" + country + '\'' +
                '}';
    }
}
