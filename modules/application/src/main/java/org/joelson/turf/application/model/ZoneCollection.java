package org.joelson.turf.application.model;

import org.joelson.turf.application.db.DatabaseEntityManager;
import org.joelson.turf.turfgame.apiv4.Region;
import org.joelson.turf.turfgame.apiv4.Zone;
import org.joelson.turf.util.TimeUtil;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ZoneCollection {

    private final DatabaseEntityManager dbEntity;

    public ZoneCollection(DatabaseEntityManager dbEntity) {
        this.dbEntity = dbEntity;
    }

    private static boolean isEqual(Region region, RegionData storedRegion) {
        return Objects.equals(region.getName(), storedRegion.getName()) && Objects.equals(region.getCountry(),
                storedRegion.getCountry());
    }

    private static RegionData toDTO(Region region) {
        return new RegionData(region.getId(), region.getName(), region.getCountry());
    }

    private static List<Region> extractRegions(List<Zone> zones) {
        Map<Integer, Region> regions = new HashMap<>();
        zones.stream().map(Zone::getRegion).forEach(region -> regions.put(region.getId(), region));
        return new ArrayList<>(regions.values());
    }

    private static boolean isEqual(Zone zone, ZoneData storedZone) {
        return Objects.equals(zone.getName(), storedZone.getName()) && isEqual(zone.getRegion(), storedZone.getRegion())
                && Objects.equals(TimeUtil.turfTimestampToInstant(zone.getDateCreated()), storedZone.getDateCreated())
                && zone.getLatitude() == storedZone.getLatitude() && zone.getLongitude() == storedZone.getLongitude()
                && zone.getTakeoverPoints() == storedZone.getTp() && zone.getPointsPerHour() == storedZone.getPph();
    }

    private static ZoneData toDTO(Zone zone) {
        return new ZoneData(zone.getId(), zone.getName(), toDTO(zone.getRegion()),
                TimeUtil.turfTimestampToInstant(zone.getDateCreated()), zone.getLatitude(), zone.getLongitude(),
                zone.getTakeoverPoints(), zone.getPointsPerHour());
    }

    public ZoneData getZone(String name) {
        return dbEntity.getZone(name);
    }

    public List<ZoneData> getZones() {
        return dbEntity.getZones();
    }

    public List<ZoneHistoryData> getZoneHistory() {
        return dbEntity.getZoneDataHistory();
    }

    public List<ZonePointsHistoryData> getZonePointsHistory() {
        return dbEntity.getZonePointsHistory();
    }

    private void updateRegions(Instant from, List<Region> regions) {
        List<RegionData> storedRegions = dbEntity.getRegions();
        Map<Integer, RegionData> storedRegionsMap = storedRegions.stream()
                .collect(Collectors.toMap(RegionData::getId, Function.identity()));
        List<RegionData> newRegions = new ArrayList<>();
        List<RegionData> changedRegions = new ArrayList<>();
        for (Region region : regions) {
            RegionData storedRegion = storedRegionsMap.get(region.getId());
            if (storedRegion == null) {
                newRegions.add(toDTO(region));
            } else if (!isEqual(region, storedRegion)) {
                changedRegions.add(toDTO(region));
            }
        }
        dbEntity.updateRegions(from, newRegions, changedRegions);
    }

    public void updateZones(Instant from, List<Zone> zones) {
        List<Region> regions = extractRegions(zones);
        updateRegions(from, regions);
        List<ZoneData> storedZones = dbEntity.getZones();
        Map<Integer, ZoneData> storedZonesMap = storedZones.stream()
                .collect(Collectors.toMap(ZoneData::getId, Function.identity()));
        List<ZoneData> newZones = new ArrayList<>();
        List<ZoneData> changedZones = new ArrayList<>();
        for (Zone zone : zones) {
            if (zone.getDateCreated() == null) {
                System.err.println("Skipping zone " + zone.getName() + " - no date created");
                continue;
            }
            ZoneData storedZone = storedZonesMap.get(zone.getId());
            if (storedZone == null) {
                newZones.add(toDTO(zone));
            } else if (!isEqual(zone, storedZone)) {
                changedZones.add(toDTO(zone));
            }
        }
        dbEntity.updateZones(from, newZones, changedZones);
    }
}
