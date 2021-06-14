package org.joelson.mattias.turfgame.apiv4;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Zone {

    private final String name;
    private final int id;
    private final Region region;
    private final double latitude;
    private final double longitude;
    private final String dateCreated;
    private final int takeoverPoints;
    private final int pointsPerHour;
    private final int totalTakeovers;

    @JsonCreator
    private Zone(
            @JsonProperty("name") String name,
            @JsonProperty("id") int id,
            @JsonProperty("region") Region region,
            @JsonProperty("latitude") double latitude,
            @JsonProperty("longitude") double longitude,
            @JsonProperty("dateCreated") String dateCreated,
            @JsonProperty("takeoverPoints") int takeoverPoints,
            @JsonProperty("pointsPerHour") int pointsPerHour,
            @JsonProperty("totalTakeovers") int totalTakeovers) {
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

    @Override
    public String toString() {
        return "Zone{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", region=" + region +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", dateCreated='" + dateCreated + '\'' +
                ", takeoverPoints=" + takeoverPoints +
                ", pointsPerHour=" + pointsPerHour +
                ", totalTakeovers=" + totalTakeovers +
                '}';
    }
}
