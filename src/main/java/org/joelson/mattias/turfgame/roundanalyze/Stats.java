package org.joelson.mattias.turfgame.roundanalyze;

import org.joelson.mattias.turfgame.apiv4.Zone;
import org.joelson.mattias.turfgame.util.ZoneUtil;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public class Stats {

    private final int takes;
    private final int neutralTakes;
    private final int takesTP;
    private final int takesPoint;
    private final int assists;
    private final int neutralAssists;
    private final int assistsTP;
    private final int revisits;
    private final int revisitsTP;

    private final Duration duration;
    private final double distance;

    private final Zone northernmost;
    private final Zone southernmost;
    private final Zone easternmost;
    private final Zone westernmost;

    public Stats(
            int takes,
            int neutralTakes,
            int takesTP,
            int takesPoint,
            int assists,
            int neutralAssists,
            int assistsTP,
            int revisits,
            int revisitsTP,
            Duration duration,
            double distance,
            Zone northernmost,
            Zone southernmost,
            Zone easternmost,
            Zone westernmost) {
        this.takes = takes;
        this.neutralTakes = neutralTakes;
        this.takesTP = takesTP;
        this.takesPoint = takesPoint;
        this.assists = assists;
        this.neutralAssists = neutralAssists;
        this.assistsTP = assistsTP;
        this.revisits = revisits;
        this.revisitsTP = revisitsTP;
        this.duration = duration;
        this.distance = distance;
        this.northernmost = northernmost;
        this.southernmost = southernmost;
        this.easternmost = easternmost;
        this.westernmost = westernmost;
    }

    public Stats plus(Stats that) {
        return new Stats(
                takes + that.takes,
                neutralTakes + that.neutralTakes,
                takesTP + that.takesTP,
                takesPoint + that.takesPoint,
                assists + that.assists,
                neutralAssists + that.neutralAssists,
                assistsTP + that.assistsTP,
                revisits + that.revisits,
                revisitsTP + that.revisitsTP,
                duration.plus(that.duration),
                distance + that.distance,
                northernmost(northernmost, that.northernmost),
                southernmost(southernmost, that.southernmost),
                easternmost(easternmost, that.easternmost),
                westernmost(westernmost, that.westernmost));
    }

    public void prettyPrint() {
        int visits = takes + assists + revisits;
        System.out.printf("Visits:   %4d%n", visits);
        System.out.printf("Takes:    %4d (%d neutrals)%n", takes, neutralTakes);
        System.out.printf("Assists:  %4d (%d neutrals)%n", assists, neutralAssists);
        System.out.printf("Revisits: %4d%n", revisits);
        System.out.println();
        int points = takesPoint + assistsTP + revisitsTP;
        System.out.printf("Points:          %7d (PPV %d)%n", points,
                (points) / (visits));
        System.out.printf("Take points:     %7d (TP %d, PPT %d)%n",
                takesPoint, takesTP, (takes > 0) ? takesPoint / takes : 0);
        System.out.printf("Assist points:   %7d (PPA %d)%n", assistsTP, (assists > 0) ? assistsTP / assists : 0);
        System.out.printf("Revisit points:  %7d (PPR %d)%n", revisitsTP, (revisits > 0) ? revisitsTP / revisits : 0);
        System.out.println();
        System.out.printf("Duration: %s%n", durationString(duration));
        System.out.printf("Distance: %.2f km%n", distance / 1000);
        if (duration.getSeconds() > 0) {
            float hours = duration.getSeconds() / 3600.0f;
            System.out.printf("Speed:    %.2f km/h%n", (distance / 1000) / hours);
            System.out.printf("Visits:   %.2f visits/h%n", visits / hours);
            System.out.printf("Points:   %.2f points/h%n", points / hours);
        }
        if (distance > 0) {
            System.out.printf("          %.2f points/km%n", points / (distance / 1000));
        }
        System.out.println();
        System.out.printf("Northernmost zone: %s%n", zoneString(northernmost));
        System.out.printf("Southernmost zone: %s%n", zoneString(southernmost));
        System.out.printf("Easternmost zone:  %s%n", zoneString(easternmost));
        System.out.printf("Westernmost zone:  %s%n", zoneString(westernmost));
        System.out.println();
        System.out.printf("Distance north-south: %.2f km%n", ZoneUtil.calcDistance(northernmost, southernmost) / 1000);
        System.out.printf("Distance east-west:   %.2f km%n", ZoneUtil.calcDistance(easternmost, westernmost) / 1000);
        System.out.println();
        System.out.printf("Box distance north-south: %.2f km%n",
                ZoneUtil.calcDistance(northernmost.getLatitude(), northernmost.getLongitude(), southernmost.getLatitude(), northernmost.getLongitude()) / 1000);
        System.out.printf("Box distance east-west: %.2f km%n",
                ZoneUtil.calcDistance(easternmost.getLatitude(), easternmost.getLongitude(), easternmost.getLatitude(), westernmost.getLongitude()) / 1000);
    }

    private static String durationString(Duration duration) {
        long seconds = duration.getSeconds();
        long days = seconds / 86400;
        seconds -= days * 86400;
        long hours = seconds / 3600;
        seconds -= hours * 3600;
        long minutes = seconds / 60;
        seconds -= minutes * 60;
        String s = "";
        if (days > 0) {
            s = days + " days ";
        }
        return s + String.format("%d:%02d:%02d", hours, minutes, seconds);
    }

    private static String zoneString(Zone zone) {
        return String.format("%d - %s (%.5f, %.5f)",
                zone.getId(), zone.getName(), zone.getLatitude(), zone.getLongitude());
    }

    public static Zone northernmost(Zone z1, Zone z2) {
        return (z1.getLatitude() >= z2.getLatitude()) ? z1 : z2;
    }

    public static Zone southernmost(Zone z1, Zone z2) {
        return (z1.getLatitude() <= z2.getLatitude()) ? z1 : z2;
    }

    public static Zone easternmost(Zone z1, Zone z2) {
        return (z1.getLongitude() >= z2.getLongitude()) ? z1 : z2;
    }

    public static Zone westernmost(Zone z1, Zone z2) {
        return (z1.getLongitude() <= z2.getLongitude()) ? z1 : z2;
    }
}
