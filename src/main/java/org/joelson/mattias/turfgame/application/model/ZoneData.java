package org.joelson.mattias.turfgame.application.model;

import org.joelson.mattias.turfgame.apiv4.Zone;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZoneData {
    
    private final Map<String, Zone> zones;
    
    public ZoneData(final Collection<Zone> zones) {
        this.zones = mapZones(zones);
    }

    private static Map<String, Zone> mapZones(Collection<Zone> zones) {
        // TODO Only because name is not unique
//        return zones.stream().collect(Collectors.toMap(Zone::getName, Function.identity()));
        Map<String, Zone> zoneMap = new HashMap<>(zones.size());
        zones.forEach(zone -> zoneMap.put(zone.getName(), zone));
        return zoneMap;
    }
    
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(1);
        out.writeInt(zones.size());
        for (Zone zone : zones.values()) {
            out.writeObject(zone);
        }
    }

    public static ZoneData readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int version = in.readInt();
        int size = in.readInt();
        List<Zone> zones = new ArrayList<>(size);
        for (int i = 0; i < size; i += 1) {
            zones.add((Zone) in.readObject());
        }
        return new ZoneData(zones);
    }
    
    public Map<String, Zone> getZones() {
        return zones;
    }
}
