package org.joelson.turf.application.view;

import org.joelson.turf.application.model.MunicipalityCollection;
import org.joelson.turf.application.model.MunicipalityData;
import org.joelson.turf.application.model.TakeData;
import org.joelson.turf.application.model.UserData;
import org.joelson.turf.application.model.VisitCollection;
import org.joelson.turf.application.model.VisitData;

import javax.swing.table.AbstractTableModel;
import java.io.Serial;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

class SessionTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES =
            { "When", "Duration", "Municipalities", "Takes", "Owns", "Revisits", "Assists", "TP", "TP / h", "PPH",
                    "TP + PPH", "TP + PPH / h", "PPH / h" };
    private static final Class<?>[] COLUMN_CLASSES =
            { Instant.class, Duration.class, String.class, Integer.class, Integer.class, Integer.class, Integer.class,
                    Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class };
    @Serial
    private static final long serialVersionUID = 1L;
    private static final int SESSION_HOUR_LENGTH = 1200;
    private final transient VisitCollection visits;
    private final HashMap<String, MunicipalityData> municipalityMap;
    private ArrayList<SessionData> currentSessions;
    public SessionTableModel(VisitCollection visits, MunicipalityCollection municipalities, UserData selectedUser) {
        this.visits = visits;
        municipalityMap = initMunicipalities(municipalities);
        updateSelectedUser(selectedUser);
    }

    private static HashMap<String, MunicipalityData> initMunicipalities(MunicipalityCollection municipalities) {
        HashMap<String, MunicipalityData> municipalityMap = new HashMap<>();
        for (MunicipalityData municipality : municipalities.getMunicipalities()) {
            municipality.getZones().forEach(zoneData -> municipalityMap.put(zoneData.getName(), municipality));
        }
        return municipalityMap;
    }

    private static String toString(Set<MunicipalityData> municipalities) {
        Map<String, Set<String>> regionMunicipalities = new HashMap<>();
        for (MunicipalityData municipality : municipalities) {
            String regionName = municipality.getRegion().getName();
            Set<String> names = regionMunicipalities.computeIfAbsent(regionName, k -> new HashSet<>());
            names.add(municipality.getName());
        }
        return regionMunicipalities.keySet().stream().sorted().map(
                regionName -> String.format("%s / %s", regionMunicipalities.get(regionName).stream().sorted()
                        .collect(Collectors.joining(",")), regionName)).collect(Collectors.joining(", "));
    }

    private static Integer nullZero(int i) {
        return (i != 0) ? i : null;
    }

    public void updateSelectedUser(UserData selectedUser) {
        List<VisitData> currentVisits = new ArrayList<>(visits.getVisits(selectedUser));
        currentVisits.sort(Comparator.comparing(VisitData::getWhen));
        currentSessions = createSessions(currentVisits);

        fireTableDataChanged();
    }

    private ArrayList<SessionData> createSessions(List<VisitData> currentVisits) {
        ArrayList<SessionData> sessionData = new ArrayList<>();
        Instant start = null;
        Instant last = null;
        Set<MunicipalityData> municipalities = new HashSet<>();
        int takes = 0;
        int owns = 0;
        int revisits = 0;
        int assists = 0;
        int tp = 0;
        int pph = 0;
        int incPph = 0;
        for (VisitData visit : currentVisits) {
            if (last == null || visit.getWhen().isAfter(last.plusSeconds(SESSION_HOUR_LENGTH))) {
                if (last != null) {
                    sessionData.add(
                            new SessionData(start, Duration.between(start, last), toString(municipalities), takes, owns,
                                    revisits, assists, tp, pph, incPph));
                    municipalities.clear();
                    takes = 0;
                    owns = 0;
                    revisits = 0;
                    assists = 0;
                    tp = 0;
                    pph = 0;
                    incPph = 0;
                }
                start = visit.getWhen();
            }
            last = visit.getWhen();
            MunicipalityData municipality = municipalityMap.get(visit.getZone().getName());
            if (municipality != null) {
                municipalities.add(municipality);
            }
            switch (visit.getType()) {
                case "Take": // NON-NLS
                    takes += 1;
                    pph += (int) (visit.getDuration().getSeconds() * visit.getPph() / 3600);
                    if (((TakeData) visit).isOwning()) {
                        owns += 1;
                        incPph += visit.getZone().getPph();
                    }
                    break;
                case "Revisit": // NON-NLS
                    revisits += 1;
                    break;
                case "Assist": // NON-NLS
                    assists += 1;
                    break;
                default:
                    throw new RuntimeException("Unknown visit type " + visit.getType());
            }
            tp += visit.getTp();
        }
        if (last != null) {
            sessionData.add(new SessionData(start, Duration.between(start, last), toString(municipalities), takes, owns,
                    revisits, assists, tp, pph, incPph));
        }
        return sessionData;
    }

    @Override
    public int getRowCount() {
        return currentSessions.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SessionData session = currentSessions.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> session.start;
            case 1 -> session.duration;
            case 2 -> session.municipalities;
            case 3 -> nullZero(session.takes);
            case 4 -> nullZero(session.owns);
            case 5 -> nullZero(session.revisits);
            case 6 -> nullZero(session.assists);
            case 7 -> session.tp;
            case 8 -> session.getTpH();
            case 9 -> session.pph;
            case 10 -> session.tp + session.pph;
            case 11 -> session.getTpPphH();
            case 12 -> nullZero(session.incPph);
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

    private record SessionData(Instant start, Duration duration, String municipalities, int takes, int owns,
            int revisits, int assists, int tp, int pph, int incPph) {

        public int getTpH() {
            int seconds = (int) duration.getSeconds();
            if (seconds < 3600) {
                return tp;
            }
            return (tp * 3600) / seconds;
        }

        public int getTpPphH() {
            int seconds = (int) duration.getSeconds();
            if (seconds < 3600) {
                return tp + pph;
            }
            return ((tp + pph) * 3600) / seconds;
        }
    }
}
