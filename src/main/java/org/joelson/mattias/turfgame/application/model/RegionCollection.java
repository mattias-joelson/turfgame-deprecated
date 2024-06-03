package org.joelson.mattias.turfgame.application.model;

import org.joelson.mattias.turfgame.apiv4.Region;
import org.joelson.mattias.turfgame.application.db.DatabaseEntityManager;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RegionCollection {
    
    private final DatabaseEntityManager dbEntity;
    
    public RegionCollection(DatabaseEntityManager dbEntity) {
        this.dbEntity = dbEntity;
    }
    
    public RegionData getRegion(String name) {
        return dbEntity.getRegion(name);
    }

    public List<RegionData> getRegions() {
        return dbEntity.getRegions();
    }
    
    public List<RegionHistoryData> getRegionHistory() {
        return dbEntity.getRegionHistory();
    }

    public void updateRegions(Instant from, Iterable<Region> regions) {
        List<RegionData> storedRegions = dbEntity.getRegions();
        Map<Integer, RegionData> storedRegionsMap = storedRegions.stream().collect(Collectors.toMap(RegionData::getId, Function.identity()));
        List<RegionData> newRegions = new ArrayList<>();
        List<RegionData> changedRegions = new ArrayList<>();
        for (Region region : regions) {
            RegionData storedRegion = storedRegionsMap.get(region.getId());
            if (storedRegion == null) {
                newRegions.add(toDTO(region));
            } else if (hasChanged(region, storedRegion)) {
                changedRegions.add(toDTO(region));
            }
        }
        dbEntity.updateRegions(from, newRegions, changedRegions);
    }
    
    private static boolean hasChanged(Region region, RegionData storedRegion) {
        return !Objects.equals(region.getName(), storedRegion.getName())
                || !Objects.equals(region.getCountry(), storedRegion.getCountry());
    }
    
    private static RegionData toDTO(Region region) {
        return new RegionData(region.getId(), region.getName(), region.getCountry());
    }
}
