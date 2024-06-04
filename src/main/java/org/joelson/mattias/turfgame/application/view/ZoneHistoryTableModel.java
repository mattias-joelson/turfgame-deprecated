package org.joelson.mattias.turfgame.application.view;

import org.joelson.mattias.turfgame.application.model.ZoneHistoryData;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

class ZoneHistoryTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = { "ID", "Name", "From", "Region Name", "Country", "Latitude", "Longitude", "Date Created" };
    private static final Class<?>[] COLUMN_CLASSES = { Integer.class, String.class, Instant.class, String.class, String.class, Double.class, Double.class,
            Instant.class };
    private static final long serialVersionUID = 1L;

    private final ArrayList<ZoneHistoryData> zones;

    public ZoneHistoryTableModel(List<ZoneHistoryData> zones) {
        this.zones = new ArrayList<>(zones);
        this.zones.sort(ZoneHistoryTableModel::compareZoneHistoryData);
    }


    @Override
    public int getRowCount() {
        return zones.size();
    }

    @Override
    public int getColumnCount() {
        return 8;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ZoneHistoryData zone = zones.get(rowIndex);
        switch (columnIndex) {
        case 0:
            return zone.getId();
        case 1:
            return zone.getName();
        case 2:
            return zone.getFrom();
        case 3:
            return zone.getRegion().getName();
        case 4:
            return zone.getRegion().getCountry();
        case 5:
            return zone.getLatitude();
        case 6:
            return zone.getLongitude();
        case 7:
            return zone.getDateCreated();
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

    private static int compareZoneHistoryData(ZoneHistoryData zoneHistoryData1, ZoneHistoryData zoneHistoryData2) {
        int cmpName = zoneHistoryData1.getName().compareTo(zoneHistoryData2.getName());
        if (cmpName != 0) {
            return cmpName;
        }
        int cmpFrom = zoneHistoryData1.getFrom().compareTo(zoneHistoryData2.getFrom());
        if (cmpFrom != 0) {
            return cmpFrom;
        }
        return -1;
    }
}
