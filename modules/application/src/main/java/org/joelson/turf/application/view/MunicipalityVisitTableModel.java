package org.joelson.mattias.turfgame.application.view;

import org.joelson.mattias.turfgame.application.model.MunicipalityData;
import org.joelson.mattias.turfgame.application.model.UserData;
import org.joelson.mattias.turfgame.application.model.ZoneData;
import org.joelson.mattias.turfgame.application.model.ZoneVisitCollection;
import org.joelson.mattias.turfgame.application.model.ZoneVisitData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.table.AbstractTableModel;

class MunicipalityVisitTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = { "Region", "Municipality", "Zone", "Visits" };
    private static final Class<?>[] COLUMN_CLASSES = { String.class, String.class, String.class, Integer.class };
    private static final long serialVersionUID = 1L;

    private final ArrayList<MunicipalityData> municipalities;
    private final HashMap<ZoneData, MunicipalityData> zoneMunicipalityMap;
    private final transient ZoneVisitCollection zoneVisits;
    private ArrayList<MunicipalityData> selectedMunicipalities;
    private transient UserData selectedUser;
    private ArrayList<ZoneVisitData> currentZoneVisits;


    public MunicipalityVisitTableModel(List<MunicipalityData> municipalities, ZoneVisitCollection zoneVisits, UserData selectedUser) {
        this.municipalities = new ArrayList<>(municipalities);
        selectedMunicipalities = new ArrayList<>(municipalities);
        zoneMunicipalityMap = createZoneMunicipalityMap(municipalities);
        this.zoneVisits = zoneVisits;
        updateSelectedUser(selectedUser);
    }

    public void updateSelectedUser(UserData selectedUser) {
        this.selectedUser = selectedUser;
        updateData();
    }

    public void updateSelectedMunicipalities(List<MunicipalityData> municipalities) {
        selectedMunicipalities = (municipalities.isEmpty()) ? this.municipalities : new ArrayList<>(municipalities);
        updateData();
    }

    private void updateData() {
        currentZoneVisits = new ArrayList<>(zoneVisits.getZoneVisits(selectedUser).stream()
                .filter(zoneVisitData -> zoneMunicipalityMap.containsKey(zoneVisitData.getZone()))
                .filter(zoneVisitData -> selectedMunicipalities.contains(zoneMunicipalityMap.get(zoneVisitData.getZone())))
                .collect(Collectors.toList()));
        // add unvisited zones
        Set<ZoneData> visitedZones = currentZoneVisits.stream().map(ZoneVisitData::getZone).collect(Collectors.toSet());
        selectedMunicipalities.stream()
                .map(MunicipalityData::getZones)
                .flatMap(Collection::stream)
                .filter(zone -> !visitedZones.contains(zone))
                .forEach(zone -> currentZoneVisits.add(new ZoneVisitData(selectedUser, zone, 0)));
        currentZoneVisits.sort((zv1, zv2) -> compareZoneVisits(zoneMunicipalityMap, zv1, zv2));
        fireTableDataChanged();

    }

    @Override
    public int getRowCount() {
        return currentZoneVisits.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ZoneVisitData zoneVisit = currentZoneVisits.get(rowIndex);
        switch(columnIndex) {
            case 0:
                return zoneVisit.getZone().getRegion().getName();
            case 1:
                return zoneMunicipalityMap.get(zoneVisit.getZone()).getName();
            case 2:
                return zoneVisit.getZone().getName();
            case 3:
                return zoneVisit.getVisits();
            default:
                throw new IllegalArgumentException("Invalid columnIndex " + columnIndex);
        }
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return COLUMN_CLASSES[columnIndex];
    }

    private static HashMap<ZoneData, MunicipalityData> createZoneMunicipalityMap(List<MunicipalityData> municipalities) {
        HashMap<ZoneData, MunicipalityData> zoneMunicipalityMap = new HashMap<>();
        municipalities.forEach(municipalityData -> municipalityData.getZones().forEach(zoneData -> zoneMunicipalityMap.put(zoneData, municipalityData)));
        return zoneMunicipalityMap;
    }

    private static int compareZoneVisits(Map<ZoneData, MunicipalityData> zoneMunicipalityMap, ZoneVisitData zoneVisit1, ZoneVisitData zoneVisit2) {
        ZoneData zone1 = zoneVisit1.getZone();
        ZoneData zone2 = zoneVisit2.getZone();
        int cmpRegion = RegionTableModel.compareRegions(zone1.getRegion(), zone2.getRegion());
        if (cmpRegion != 0) {
            return cmpRegion;
        }
        int cmpMunicipality = zoneMunicipalityMap.get(zone1).getName().compareTo(zoneMunicipalityMap.get(zone2).getName());
        if (cmpMunicipality != 0) {
            return cmpMunicipality;
        }
        int cmpVisits = zoneVisit1.getVisits() - zoneVisit2.getVisits();
        if (cmpVisits != 0) {
            return cmpVisits;
        }
        int cmpZoneName = zone1.getName().compareTo(zone2.getName());
        return (cmpZoneName != 0) ? cmpZoneName : -1;
    }
}
