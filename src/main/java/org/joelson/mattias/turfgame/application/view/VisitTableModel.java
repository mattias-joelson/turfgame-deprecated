package org.joelson.mattias.turfgame.application.view;

import org.joelson.mattias.turfgame.application.model.MunicipalityCollection;
import org.joelson.mattias.turfgame.application.model.MunicipalityData;
import org.joelson.mattias.turfgame.application.model.TakeData;
import org.joelson.mattias.turfgame.application.model.UserData;
import org.joelson.mattias.turfgame.application.model.VisitCollection;
import org.joelson.mattias.turfgame.application.model.VisitData;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.function.ToIntFunction;
import javax.swing.table.AbstractTableModel;

class VisitTableModel extends AbstractTableModel {
    
    private static final String[] COLUMN_NAMES = { "When", "Municipality", "Zone", "TP", "PPH", "Type", "TP / h", "Owns", "Duration", "PPH", "TP + PPH", "TP + PPH / h" };
    private static final Class<?>[] COLUMN_CLASSES = {
            Instant.class, String.class, String.class, Integer.class, Integer.class, String.class, Integer.class, Boolean.class, Duration.class, Integer.class,
            Integer.class, Integer.class
    };
    private static final long serialVersionUID = 1L;
    private static final int HALF_HOUR_RANGE = 1801;
    
    private final transient VisitCollection visits;
    private final HashMap<String, String> municipalityMap;
    private ArrayList<VisitData> currentVisits;

    public VisitTableModel(VisitCollection visits, MunicipalityCollection municipalities, UserData selectedUser) {
        this.visits = visits;
        municipalityMap = initMunicipalities(municipalities);
        updateSelectedUser(selectedUser);
    }

    private static HashMap<String, String> initMunicipalities(MunicipalityCollection municipalities) {
        HashMap<String, String> municipalityMap = new HashMap<>();
        for (MunicipalityData municipality : municipalities.getMunicipalities()) {
            String municipalityName = String.format("%s / %s", municipality.getName(), municipality.getRegion().getName());
            municipality.getZones().forEach(zoneData -> municipalityMap.put(zoneData.getName(), municipalityName));
        }
        return municipalityMap;
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
            String municipality = municipalityMap.get(visit.getZone().getName());
            return (municipality != null) ? municipality : String.format("? / %s", visit.getZone().getRegion().getName());
        case 2:
            return visit.getZone().getName();
        case 3:
            return visit.getTp();
        case 4:
            return visit.getPph();
        case 5:
            return visit.getType();
        case 6:
            return sumRange(visit.getWhen(), VisitData::getTp);
        case 7:
            return visit instanceof TakeData && ((TakeData) visit).isOwning();
        case 8:
            return visit.getDuration();
        case 9:
            return calcPph(visit);
        case 10:
            return totalPoints(visit);
        case 11:
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
