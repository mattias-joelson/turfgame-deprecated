package org.joelson.turf.application.model;

import org.joelson.turf.util.StringUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MunicipalityData {

    private final String name;
    private final RegionData region;
    private final List<ZoneData> zones;

    public MunicipalityData(RegionData region, String name, List<ZoneData> zones) {
        this.region = Objects.requireNonNull(region, "Region can not be null");
        this.name = StringUtil.requireNotNullAndNotEmpty(name, "Name can not be null", "Name can not be empty");
        this.zones = sortedZones(Objects.requireNonNull(zones, "Zones can not be null"));
    }

    private static List<ZoneData> sortedZones(List<ZoneData> zones) {
        return zones.stream().sorted(Comparator.comparing(ZoneData::getName)).toList();
    }

    public RegionData getRegion() {
        return region;
    }

    public String getName() {
        return name;
    }

    public List<ZoneData> getZones() {
        return Collections.unmodifiableList(zones);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof MunicipalityData) {
            return name.equals(((MunicipalityData) obj).name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public String toString() {
        return String.format("MunicipalityData[region %s, name %s, zones [%s]]", ModelUtil.toStringPart(region), name,
                zones.stream().map(ModelUtil::toStringPart).collect(Collectors.joining(", ")));
    }
}
