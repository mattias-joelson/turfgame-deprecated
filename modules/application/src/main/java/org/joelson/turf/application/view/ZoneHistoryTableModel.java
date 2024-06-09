package org.joelson.turf.application.view;

import org.joelson.turf.application.model.ZoneHistoryData;

import javax.swing.table.AbstractTableModel;
import java.io.Serial;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

class ZoneHistoryTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES =
            { "ID", "Name", "From", "Region Name", "Country", "Latitude", "Longitude", "Date Created" };
    private static final Class<?>[] COLUMN_CLASSES =
            { Integer.class, String.class, Instant.class, String.class, String.class, Double.class, Double.class,
                    Instant.class };
    @Serial
    private static final long serialVersionUID = 1L;

    private final ArrayList<ZoneHistoryData> zones;

    public ZoneHistoryTableModel(List<ZoneHistoryData> zones) {
        this.zones = new ArrayList<>(zones);
        this.zones.sort(ZoneHistoryTableModel::compareZoneHistoryData);
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
        return switch (columnIndex) {
            case 0 -> zone.getId();
            case 1 -> zone.getName();
            case 2 -> zone.getFrom();
            case 3 -> zone.getRegion().getName();
            case 4 -> zone.getRegion().getCountry();
            case 5 -> zone.getLatitude();
            case 6 -> zone.getLongitude();
            case 7 -> zone.getDateCreated();
            default -> throw new IllegalArgumentException("Invalid columnIndex " + columnIndex);
        };
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return COLUMN_CLASSES[columnIndex];
    }
}
