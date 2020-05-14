package org.joelson.mattias.turfgame.application.view;

import org.joelson.mattias.turfgame.application.model.TakeData;
import org.joelson.mattias.turfgame.application.model.UserData;
import org.joelson.mattias.turfgame.application.model.VisitCollection;
import org.joelson.mattias.turfgame.application.model.VisitData;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;
import javax.swing.table.AbstractTableModel;

class VisitTableModel extends AbstractTableModel {
    
    private static final String[] COLUMN_NAMES = { "When", "Zone", "TP", "PPH", "Type", "TP / h", "Duration", "Total PPH", "Total", "TP + PPH / h" };
    private static final Class<?>[] COLUMN_CLASSES = {
            Instant.class, String.class, Integer.class, Integer.class, String.class, Integer.class, Duration.class, Integer.class, Integer.class, Integer.class
    };
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
            return sumRange(visit.getWhen(), VisitData::getTp);
        case 6:
            return visit.getDuration();
        case 7:
            return calcPph(visit);
        case 8:
            return totalPoints(visit);
        case 9:
            return sumRange(visit.getWhen(), VisitTableModel::totalPoints);
        default:
            throw new IllegalArgumentException("Invalid columnIndex " + columnIndex);
        }
    }
    
    private int sumRange(Instant when, ToIntFunction<VisitData> pointFunction) {
        Instant before = when.minusSeconds(HALF_HOUR_RANGE);
        Instant after = when.plusSeconds(HALF_HOUR_RANGE);
        return currentVisits.stream()
                .filter(visit -> visit.getWhen().isAfter(before))
                .filter(visit -> visit.getWhen().isBefore(after))
                .mapToInt(pointFunction)
                .sum();
    }

    private static Integer calcPph(VisitData visit) {
        if (visit instanceof TakeData) {
            long seconds = visit.getDuration().getSeconds();
            return (int) (seconds * visit.getPph() / 3600);
        } else {
            return null;
        }
    }

    private static int totalPoints(VisitData visit) {
        Integer pph = calcPph(visit);
        return visit.getTp() + ((pph != null) ? pph : 0);
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
