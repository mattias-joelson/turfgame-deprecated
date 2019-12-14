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

public class RegionData {
    
    private final DatabaseEntityManager dbEntity;
    
    public RegionData(DatabaseEntityManager dbEntity) {
        this.dbEntity = dbEntity;
    }
    
    public RegionDTO getRegion(String name) {
        return dbEntity.getRegion(name);
    }

    public List<RegionDTO> getRegions() {
        return dbEntity.getRegions();
    }
    
    public List<RegionHistoryDTO> getRegionHistory() {
        return dbEntity.getRegionHistory();
    }

    public void updateRegions(Instant from, Iterable<Region> regions) {
        List<RegionDTO> storedRegions = dbEntity.getRegions();
        Map<Integer, RegionDTO> storedRegionsMap = storedRegions.stream().collect(Collectors.toMap(RegionDTO::getId, Function.identity()));
        List<RegionDTO> newRegions = new ArrayList<>();
        List<RegionDTO> changedRegions = new ArrayList<>();
        for (Region region : regions) {
            RegionDTO storedRegion = storedRegionsMap.get(region.getId());
            if (storedRegion == null) {
                newRegions.add(toDTO(region));
            } else if (hasChanged(region, storedRegion)) {
                changedRegions.add(toDTO(region));
            }
        }
        dbEntity.updateRegions(from, newRegions, changedRegions);
    }
    
    private static boolean hasChanged(Region region, RegionDTO storedRegion) {
        return !Objects.equals(region.getName(), storedRegion.getName())
                || !Objects.equals(region.getCountry(), storedRegion.getCountry());
    }
    
    private static RegionDTO toDTO(Region region) {
        return new RegionDTO(region.getId(), region.getName(), region.getCountry());
    }
}
