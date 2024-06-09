package org.joelson.mattias.turfgame.application.view;

import org.joelson.mattias.turfgame.application.model.ZoneData;
import org.joelson.mattias.turfgame.application.model.ZonePointsHistoryData;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.swing.table.AbstractTableModel;

class ZonePointsHistoryTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = { "ID", "Name", "From", "TP", "PPH" };
    private static final Class<?>[] COLUMN_CLASSES = { Integer.class, String.class, Instant.class, Integer.class, Integer.class };
    private static final long serialVersionUID = 1L;

    private final ArrayList<ZonePointsHistoryData> zonePointsHistory;
    private final HashMap<Integer, ZoneData> zones;

    public ZonePointsHistoryTableModel(List<ZoneData> zones, List<ZonePointsHistoryData> zonePointsHistory) {
        this.zonePointsHistory = new ArrayList<>(zonePointsHistory);
        this.zones = new HashMap<>(zones.stream().collect(Collectors.toMap(ZoneData::getId, Function.identity())));
        this.zonePointsHistory.sort((o1, o2) -> compareZones(this.zones, o1, o2));
    }

    @Override
    public int getRowCount() {
        return zonePointsHistory.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ZonePointsHistoryData zonePoints = zonePointsHistory.get(rowIndex);
        switch (columnIndex) {
        case 0:
            return zonePoints.getId();
        case 1:
            return zones.get(zonePoints.getId()).getName();
        case 2:
            return zonePoints.getFrom();
        case 3:
            return zonePoints.getTp();
        case 4:
            return zonePoints.getPph();
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

    private static int compareZones(Map<Integer, ZoneData> zones, ZonePointsHistoryData zonePointsHistoryData1, ZonePointsHistoryData zonePointsHistoryData2) {
        int cmpName = zones.get(zonePointsHistoryData1.getId()).getName().compareTo(zones.get(zonePointsHistoryData2.getId()).getName());
        if (cmpName != 0) {
            return cmpName;
        }
        int cmpFrom = zonePointsHistoryData1.getFrom().compareTo(zonePointsHistoryData2.getFrom());
        if (cmpFrom != 0) {
            return cmpFrom;
        }
        return -1;
    }
}
