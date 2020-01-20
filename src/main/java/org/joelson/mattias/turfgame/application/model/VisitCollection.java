package org.joelson.mattias.turfgame.application.model;

import org.joelson.mattias.turfgame.application.db.DatabaseEntityManager;
import org.joelson.mattias.turfgame.util.TimeUtil;
import org.joelson.mattias.turfgame.zundin.Today;
import org.joelson.mattias.turfgame.zundin.TodayZone;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class VisitCollection {
    
    private final DatabaseEntityManager dbEntity;
    
    public VisitCollection(DatabaseEntityManager dbEntity) {
        this.dbEntity = dbEntity;
    }
    
    public List<VisitData> getVisits(UserData user) {
        return Collections.emptyList();
    }
    
    public void updateVisits(Today today) {
        Map<String, UserData> userMap = getUsers(today);
        Map<String, ZoneData> zoneMap = getZones(today);
        
        List<VisitData> visits = today.getZones().stream().map(todayZone -> createVisit(zoneMap, userMap, today.getUserName(), todayZone))
                .collect(Collectors.toList());
        dbEntity.updateVisits(visits);
    }
    
    private Map<String, UserData> getUsers(Today today) {
        Set<String> userNames = new HashSet<>(today.getZones().size() + 1);
        userNames.add(today.getUserName());
        today.getZones().forEach(todayZone -> userNames.add(todayZone.getUserId()));
        return dbEntity.getUsers().stream()
                .filter(userData -> userNames.contains(userData.getName()))
                .collect(Collectors.toMap(UserData::getName, Function.identity()));
    }
    
    private Map<String, ZoneData> getZones(Today today) {
        Set<String> zoneNames = new HashSet<>(today.getZones().size());
        today.getZones().forEach(todayZone -> zoneNames.add(todayZone.getZoneName()));
        return dbEntity.getZones().stream()
                .filter(zoneData -> zoneNames.contains(zoneData.getName()))
                .collect(Collectors.toMap(ZoneData::getName, Function.identity()));
    }
    
    private VisitData createVisit(Map<String, ZoneData> zones, Map<String, UserData> userMap, String userName, TodayZone todayZone) {
        switch (todayZone.getActivity()) {
        case "Takeover":
            return new TakeData(getZone(zones, todayZone.getZoneName()), getWhen(todayZone.getDate()), getUser(userMap, userName));
        case "Lost":
            return new TakeData(getZone(zones, todayZone.getZoneName()), getWhen(todayZone.getDate()), getUser(userMap, todayZone.getUserId()));
        case "Assist":
            return new AssistData(getZone(zones, todayZone.getZoneName()), getWhen(todayZone.getDate()), getUser(userMap, todayZone.getUserId()),
                    getUser(userMap, userName));
        default:
            throw new RuntimeException("Activity " + todayZone.getActivity() + " not implemented yet.");
        }
    }
    
    private ZoneData getZone(Map<String, ZoneData> zones, String zoneName) {
        return Objects.requireNonNull(zones.get(zoneName), "Unknown zone " + zoneName);
    }
    
    private UserData getUser(Map<String, UserData> userMap, String userName) {
        return Objects.requireNonNull(userMap.get(userName), "Unknown user " + userName);
    }
    
    private Instant getWhen(String datetime) {
        return TimeUtil.zundinTimestampToInstant(datetime);
    }
}
