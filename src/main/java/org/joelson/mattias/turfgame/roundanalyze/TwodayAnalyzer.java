package org.joelson.mattias.turfgame.roundanalyze;

import org.joelson.mattias.turfgame.apiv4.Zone;
import org.joelson.mattias.turfgame.apiv4.Zones;
import org.joelson.mattias.turfgame.util.ZoneUtil;
import org.joelson.mattias.turfgame.zundin.Today;
import org.joelson.mattias.turfgame.zundin.TodayZone;
import org.joelson.mattias.turfgame.zundin.Twoday;
import org.joelson.mattias.turfgame.zundin.TwodayZone;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TwodayAnalyzer {

    private static final String DURATION_DAYS_STRING = "&nbsp;days&nbsp;";

    public static void main(String[] args) throws IOException {

        if (args.length <= 0) {
            System.out.println(String.format("Usage:\n\t%s today.html ...", TodayAnalyzer.class));
            return;
        }

        int roundNumber = 132;
        LocalDateTime roundStart = LocalDateTime.of(2021, Month.JUNE, 6, 11, 0);
        LocalDateTime roundEnd = LocalDateTime.of(2021, Month.JULY, 4, 10, 0);
        Path roundZoneFile = Path.of("../turfgame-history/zones/zones_2021-07-03_23-09.json");
        Path currentZoneFile = Path.of("../turfgame-history/zones/zones_2021-07-17_21-21.json");

        //Map<String, Zone> zoneMap = createZoneMap(roundZoneFile, currentZoneFile);
        Map<Integer, Zone> zoneMap = ZoneUtil.toIdMap(Zones.fromJSON(Files.readString(roundZoneFile)));
        Map<String, List<String>> userFilesMap = createUserFilesMap(args);
        userFilesMap.forEach((user, filenames) -> analyzeUser(user, filenames, zoneMap, roundNumber, roundStart, roundEnd));
    }

//    private static Map<String, Zone> createZoneMap(Path roundZoneFile, Path currentZoneFile) throws IOException {
//        Map<Integer, Zone> currentZones = ZoneUtil.toIdMap(Zones.fromJSON(Files.readString(currentZoneFile)));
//        return Zones.fromJSON(Files.readString(roundZoneFile)).stream()
//                .collect(Collectors.toMap(zone -> currentZones.get(zone.getId()).getName(), Function.identity()));
//    }

    private static Map<String, List<String>> createUserFilesMap(String[] filenames) {
        return Arrays.stream(filenames)
                .collect(Collectors.groupingBy(filename -> getUser(filename)));
    }

    private static void analyzeUser(String username, List<String> filenames, Map<Integer, Zone> zoneMap,
                                    int roundNumber, LocalDateTime roundStart, LocalDateTime roundEnd) {
        User user = new User(username);
        filenames = filenames.stream()
                .sorted(Comparator.comparing(filename -> getDate(filename)))
                .collect(Collectors.toList());
        filenames.stream()
                .forEach(filename -> analyzeDay(user, filename, zoneMap, roundNumber, roundStart, roundEnd));
        int sessionNumber = 1;
        Map<Integer, Zone> zoneIdMap = zoneMap.values().stream()
                .collect(Collectors.toMap(Zone::getId, Function.identity()));
        for (Session session : user.getSessions()) {
            List<Visit> visits = session.getVisits();
            String duration = visits.size() > 1 ? String.format("from %s to %s", TODAY_DATE_FORMATTER.format(visits.get(0).getTime()), TODAY_DATE_FORMATTER.format(visits.get(visits.size() - 1).getTime())) : TODAY_DATE_FORMATTER.format(visits.get(0).getTime());
            System.out.println("Session " + sessionNumber + " : " + duration);
            int takePoints = 0;
            int totalPoints = 0;
            int neutralized = 0;
            double distance = 0.0f;
            Zone previousZone = null;
            for (int i = 0; i < visits.size(); i += 1) {
                Visit visit = visits.get(i);
                takePoints += visit.getTp();
                totalPoints += visit.getPoints();
                if (visit instanceof Take && ((Take) visit).isNeutralized()) {
                    neutralized += 1;
                }
                Zone currentZone = zoneIdMap.get(visit.getZoneId());
                if (previousZone != null) {
                    distance += ZoneUtil.calcDistance(previousZone, currentZone);
                }
                previousZone = currentZone;
            }
            System.out.println("    visits:       " + visits.size());
            System.out.println("    take points:  " + takePoints);
            System.out.println("    total points: " + totalPoints);
            System.out.println("    neutralized:  " + neutralized);
            System.out.println("    distance:     " + distance);
            sessionNumber += 1;
        }
    }

    private static void analyzeDay(User user, String filename, Map<Integer, Zone> zoneMap,
                                   int roundNumber, LocalDateTime roundStart, LocalDateTime roundEnd) {
        Twoday twoday;
        try {
            twoday = Twoday.fromHTML(user.getUsername(), getDate(filename), Files.readString(Path.of(filename)));
        } catch (IOException e) {
            System.out.println("Error reading " + filename + " - " + e.getMessage());
            e.printStackTrace();
            return;
        }
        twoday.getZones()
                .forEach(twodayZone -> {
                    LocalDateTime dateTime = getLocalDateTime(twodayZone);
                    if (dateTime.isBefore(roundStart) || dateTime.isAfter(roundEnd)) {
                        return;
                    }
                    Zone zone = zoneMap.get(twodayZone.getZoneId());
                    switch (twodayZone.getActivity()) {
                        case "Takeover":
                            if (zone.getTakeoverPoints() != twodayZone.getTP() || zone.getPointsPerHour() != twodayZone.getPPH()) {
//                                throw new IllegalStateException(String.format("%s: Zone(%d,+%d) id=%d name=%s differs from TwodayZone(%d,+%d)",
//                                        dateTime, zone.getTakeoverPoints(), zone.getPointsPerHour(), zone.getId(), zone.getName(),
//                                        twodayZone.getTP(), twodayZone.getPPH()));
                                System.out.println(String.format("%s: Zone(%d,+%d) id=%d name=%s differs from TwodayZone(%d,+%d)",
                                        dateTime, zone.getTakeoverPoints(), zone.getPointsPerHour(), zone.getId(), zone.getName(),
                                        twodayZone.getTP(), twodayZone.getPPH()));
                            }
                            user.addTake(dateTime, zone, twodayZone.isNeutralized(), getDuration(twodayZone));
                            break;
                        case "Assist":
                            if (zone.getTakeoverPoints() != twodayZone.getTP()) {
//                                throw new IllegalStateException(String.format("%s: Zone(%d)  id=%d name=%s differs from TwodayZone(%d)",
//                                        dateTime, zone.getTakeoverPoints(), zone.getId(), zone.getName(), twodayZone.getTP()));
                                System.out.println(String.format("%s: Zone(%d)  id=%d name=%s differs from TwodayZone(%d)",
                                        dateTime, zone.getTakeoverPoints(), zone.getId(), zone.getName(), twodayZone.getTP()));
                            }
                            user.addAssist(dateTime, zone, twodayZone.isNeutralized());
                            break;
                        case "Revisit":
                            if (zone.getTakeoverPoints() / 2 != twodayZone.getTP()) {
//                                throw new IllegalStateException(String.format("Zone(%d) revisit differs from TwodayZone(%d)",
//                                        zone.getTakeoverPoints(), twodayZone.getTP()));
                                System.out.println(String.format("Zone(%d) revisit differs from TwodayZone(%d)",
                                        zone.getTakeoverPoints(), twodayZone.getTP()));
                            }
                            user.addRevisit(dateTime, zone);
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown activity " + twodayZone.getActivity());
                    }
                });
    }

    private static final DateTimeFormatter TODAY_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static LocalDateTime getLocalDateTime(TwodayZone twodayZone) {
        return LocalDateTime.parse(twodayZone.getDate(), TODAY_DATE_FORMATTER);
    }

    private static Duration getDuration(TwodayZone twodayZone) {
        String durationString = twodayZone.getDuration();
        int daysIndex = durationString.indexOf(DURATION_DAYS_STRING);
        int days = 0;
        if (daysIndex >= 0) {
            days = Integer.parseInt(durationString.substring(0, daysIndex));
            durationString = durationString.substring(daysIndex + DURATION_DAYS_STRING.length());
        }
        return Duration.ofDays(days)
                .plusHours(Integer.parseInt(durationString.substring(0, 2)))
                .plusMinutes(Integer.parseInt(durationString.substring(3, 5)))
                .plusSeconds(Integer.parseInt(durationString.substring(6)));
    }

    private static String getUser(String filename) {
        int roundIndex = filename.lastIndexOf("round-132.");
        int extIndex = filename.lastIndexOf(".html");
        int dotIndex = filename.lastIndexOf('.', extIndex - 1);
        return filename.substring(roundIndex + 10, dotIndex);
    }

    private static String getDate(String filename) {
        int extIndex = filename.lastIndexOf(".html");
        int dotIndex = filename.lastIndexOf('.', extIndex - 1);
        return filename.substring(dotIndex + 1, extIndex);
    }
}
