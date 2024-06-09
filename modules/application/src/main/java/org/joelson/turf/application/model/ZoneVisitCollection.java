package org.joelson.turf.application.model;

import org.joelson.turf.application.db.DatabaseEntityManager;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ZoneVisitCollection {

    private final DatabaseEntityManager dbEntity;

    public ZoneVisitCollection(DatabaseEntityManager dbEntity) {
        this.dbEntity = dbEntity;
    }

    public List<UserData> getZoneVisitUsers() {
        return dbEntity.getZoneVisitUsers();
    }

    public List<ZoneVisitData> getZoneVisits(UserData user) {
        return dbEntity.getZoneVisits(user);
    }

    public void updateZoneVisits(String userName, Map<String, Integer> visits) {
        UserData user = dbEntity.getUser(userName);
        List<ZoneData> zones = dbEntity.getZones();
        Map<String, ZoneData> zoneNameMap = zones.stream()
                .collect(Collectors.toMap(ZoneData::getName, Function.identity()));
        List<ZoneVisitData> zoneVisits = visits.entrySet().stream()
                .map(entry -> new ZoneVisitData(user, zoneNameMap.get(entry.getKey()), entry.getValue())).toList();
        dbEntity.updateZoneVisits(zoneVisits);
    }
}
