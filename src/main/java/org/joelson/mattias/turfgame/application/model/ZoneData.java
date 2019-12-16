package org.joelson.mattias.turfgame.application.model;

import org.joelson.mattias.turfgame.apiv4.Region;
import org.joelson.mattias.turfgame.apiv4.Zone;
import org.joelson.mattias.turfgame.application.db.DatabaseEntityManager;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ZoneData {
    
    private final DatabaseEntityManager dbEntity;
    
    public ZoneData(DatabaseEntityManager dbEntity) {
        this.dbEntity = dbEntity;
    }

    public ZoneDTO getZone(String name) {
        return dbEntity.getZone(name);
    }
    
    public List<ZoneDTO> getZones() {
        return dbEntity.getZones();
    }
    
    public List<ZoneDataHistoryDTO> getZoneDataHistory() {
        return dbEntity.getZoneDataHistory();
    }

    public List<ZonePointsHistoryDTO> getZonePointsHistory() {
        return dbEntity.getZonePointsHistory();
    }
    
    private void updateRegions(Instant from, List<Region> regions) {
        List<RegionDTO> storedRegions = dbEntity.getRegions();
        Map<Integer, RegionDTO> storedRegionsMap = storedRegions.stream().collect(Collectors.toMap(RegionDTO::getId, Function.identity()));
        List<RegionDTO> newRegions = new ArrayList<>();
        List<RegionDTO> changedRegions = new ArrayList<>();
        for (Region region : regions) {
            RegionDTO storedRegion = storedRegionsMap.get(region.getId());
            if (storedRegion == null) {
                newRegions.add(toDTO(region));
            } else if (!isEqual(region, storedRegion)) {
                changedRegions.add(toDTO(region));
            }
        }
        dbEntity.updateRegions(from, newRegions, changedRegions);
    }
    
    private static boolean isEqual(Region region, RegionDTO storedRegion) {
        return Objects.equals(region.getName(), storedRegion.getName())
                && Objects.equals(region.getCountry(), storedRegion.getCountry());
    }
    
    private static RegionDTO toDTO(Region region) {
        return new RegionDTO(region.getId(), region.getName(), region.getCountry());
    }

    public void updateZones(Instant from, List<Zone> zones) {
        List<Region> regions = extractRegions(zones);
        updateRegions(from, regions);
        List<ZoneDTO> storedZones = dbEntity.getZones();
        Map<Integer, ZoneDTO> storedZonesMap = storedZones.stream().collect(Collectors.toMap(ZoneDTO::getId, Function.identity()));
        List<ZoneDTO> newZones = new ArrayList<>();
        List<ZoneDTO> changedZones = new ArrayList<>();
        for (Zone zone : zones) {
            if (zone.getDateCreated() == null) {
                System.err.println("Skipping zone " + zone.getName() + " - no date created");
            }
            ZoneDTO storedZone = storedZonesMap.get(zone.getId());
            if (storedZone == null) {
                newZones.add(toDTO(zone));
            } else if (!isEqual(zone, storedZone)) {
                changedZones.add(toDTO(zone));
            }
        }
        dbEntity.updateZones(from, newZones, changedZones);
    }
    
    private static List<Region> extractRegions(List<Zone> zones) {
        Map<Integer, Region> regions = new HashMap<>();
        zones.stream().map(Zone::getRegion).forEach(region -> regions.put(region.getId(), region));
        return new ArrayList<>(regions.values());
    }
    
    private static boolean isEqual(Zone zone, ZoneDTO storedZone) {
        return Objects.equals(zone.getName(), storedZone.getName())
                && isEqual(zone.getRegion(), storedZone.getRegion())
                && Objects.equals(toInstant(zone.getDateCreated()), storedZone.getDateCreated())
                && zone.getLatitude() == storedZone.getLatitude()
                && zone.getLongitude() == storedZone.getLongitude()
                && zone.getTakeoverPoints() == storedZone.getTp()
                && zone.getPointsPerHour() == storedZone.getPph();
    }
    
    private static Instant toInstant(String s) {
        TemporalAccessor temporalAccessor = DateTimeFormatter.ISO_DATE_TIME.parse(s.substring(0, 19));
        LocalDateTime localDateTime = LocalDateTime.from(temporalAccessor);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
        return Instant.from(zonedDateTime);
    }
    
    private static ZoneDTO toDTO(Zone zone) {
        return new ZoneDTO(zone.getId(), zone.getName(), toDTO(zone.getRegion()), toInstant(zone.getDateCreated()),
                zone.getLatitude(), zone.getLongitude(), zone.getTakeoverPoints(), zone.getPointsPerHour());
    }
    
}
