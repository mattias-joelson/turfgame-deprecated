package org.joelson.turf.application.view;

import org.joelson.turf.application.model.UserData;
import org.joelson.turf.application.model.ZoneVisitCollection;
import org.joelson.turf.application.model.ZoneVisitData;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Comparator;
import javax.swing.table.AbstractTableModel;

class ZoneVisitTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = { "Zone", "Visits" };
    private static final Class<?>[] COLUMN_CLASSES = { String.class, Integer.class };
    @Serial
    private static final long serialVersionUID = 1L;

    private final transient ZoneVisitCollection zoneVisits;
    private ArrayList<ZoneVisitData> currentZoneVisits;

    public ZoneVisitTableModel(ZoneVisitCollection zoneVisits, UserData selectedUser) {
        this.zoneVisits = zoneVisits;
        updateSelectedUser(selectedUser);
    }

    public void updateSelectedUser(UserData selectedUser) {
        currentZoneVisits = new ArrayList<>(zoneVisits.getZoneVisits(selectedUser));
        currentZoneVisits.sort(Comparator.comparing(zoneVisit -> zoneVisit.getZone().getName()));
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return currentZoneVisits.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ZoneVisitData zoneVisit = currentZoneVisits.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> zoneVisit.getZone().getName();
            case 1 -> zoneVisit.getVisits();
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
