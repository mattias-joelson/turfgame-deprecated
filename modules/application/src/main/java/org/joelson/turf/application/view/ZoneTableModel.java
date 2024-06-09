package org.joelson.turf.application.view;

import org.joelson.turf.application.model.ZoneData;

import javax.swing.table.AbstractTableModel;
import java.io.Serial;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class ZoneTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES =
            { "ID", "Name", "Region Name", "Country", "Latitude", "Longitude", "Date Created", "TP", "PPH" };
    private static final Class<?>[] COLUMN_CLASSES =
            { Integer.class, String.class, String.class, String.class, Double.class, Double.class, Instant.class,
                    Integer.class, Integer.class };
    @Serial
    private static final long serialVersionUID = 1L;

    private final ArrayList<ZoneData> zones;

    ZoneTableModel(List<ZoneData> zones) {
        this.zones = new ArrayList<>(zones);
        this.zones.sort(Comparator.comparing(ZoneData::getName));
    }

    @Override
    public int getRowCount() {
        return zones.size();
    }

    @Override
    public int getColumnCount() {
        return 9;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ZoneData zone = zones.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> zone.getId();
            case 1 -> zone.getName();
            case 2 -> zone.getRegion().getName();
            case 3 -> zone.getRegion().getCountry();
            case 4 -> zone.getLatitude();
            case 5 -> zone.getLongitude();
            case 6 -> zone.getDateCreated();
            case 7 -> zone.getTp();
            case 8 -> zone.getPph();
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
