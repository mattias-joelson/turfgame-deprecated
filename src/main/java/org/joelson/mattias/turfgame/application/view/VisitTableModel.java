package org.joelson.mattias.turfgame.application.view;

import org.joelson.mattias.turfgame.application.model.UserData;
import org.joelson.mattias.turfgame.application.model.VisitCollection;
import org.joelson.mattias.turfgame.application.model.VisitData;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class VisitTableModel extends AbstractTableModel {
    
    private static final String[] COLUMN_NAMES = { "When", "Zone", "TP", "PPH", "Type", "TP / h" };
    private static final Class<?>[] COLUMN_CLASSES = { Instant.class, String.class, Integer.class, Integer.class, String.class, Integer.class };
    private static final long serialVersionUID = 1L;
    private static final int HALF_HOUR_RANGE = 1801;
    
    private final VisitCollection visits;
    private List<VisitData> currentVisits;
    
    public VisitTableModel(VisitCollection visits, UserData selectedUser) {
        this.visits = visits;
        updateSelectedUser(selectedUser);
    }
    
    public void updateSelectedUser(UserData selectedUser) {
        currentVisits = new ArrayList<>(visits.getVisits(selectedUser));
        currentVisits.sort(Comparator.comparing(VisitData::getWhen));
        fireTableDataChanged();
    }
    
    @Override
    public int getRowCount() {
        return currentVisits.size();
    }
    
    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        VisitData visit = currentVisits.get(rowIndex);
        switch (columnIndex) {
        case 0:
            return visit.getWhen();
        case 1:
            return visit.getZone().getName();
        case 2:
            return visit.getTp();
        case 3:
            return visit.getPph();
        case 4:
            return visit.getType();
        case 5:
            return calcTpH(visit.getWhen());
        default:
            throw new IllegalArgumentException("Invalid columnIndex " + columnIndex);
        }
    }
    
    private int calcTpH(Instant when) {
        Instant before = when.minusSeconds(HALF_HOUR_RANGE);
        Instant after = when.plusSeconds(HALF_HOUR_RANGE);
        return currentVisits.stream()
                .filter(visit -> visit.getWhen().isAfter(before))
                .filter(visit -> visit.getWhen().isBefore(after))
                .mapToInt(VisitData::getTp)
                .sum();
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
