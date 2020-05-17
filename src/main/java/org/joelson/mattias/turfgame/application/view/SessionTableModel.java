package org.joelson.mattias.turfgame.application.view;

import org.joelson.mattias.turfgame.application.model.MunicipalityCollection;
import org.joelson.mattias.turfgame.application.model.MunicipalityData;
import org.joelson.mattias.turfgame.application.model.UserData;
import org.joelson.mattias.turfgame.application.model.VisitCollection;
import org.joelson.mattias.turfgame.application.model.VisitData;

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
import javax.swing.table.AbstractTableModel;

class SessionTableModel extends AbstractTableModel {

    private static class SessionData {
        private final Instant start;
        private final Duration duration;
        private final String municipalities;
        private final int takes;
        private final int revisits;
        private final int assists;
        private final int tp;
        private final int pph;

        private SessionData(Instant start, Duration duration, String municipalities, int takes, int revisits, int assists, int tp, int pph) {
            this.start = start;
            this.duration = duration;
            this.municipalities = municipalities;
            this.takes = takes;
            this.revisits = revisits;
            this.assists = assists;
            this.tp = tp;
            this.pph = pph;
        }

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

    private static final String[] COLUMN_NAMES = {
            "When", "Duration", "Municipalities", "Takes", "Revisits", "Assists", "TP", "TP / h", "PPH", "TP + PPH", "TP + PPH / h"
    };
    private static final Class<?>[] COLUMN_CLASSES = {
            Instant.class, Duration.class, String.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class
    };
    private static final long serialVersionUID = 1L;
    private static final int SESSION_HOUR_LENGTH = 1200;

    private final VisitCollection visits;
    private final Map<String, MunicipalityData> municipalityMap;
    private List<SessionData> currentSessions;

    public SessionTableModel(VisitCollection visits, MunicipalityCollection municipalities, UserData selectedUser) {
        this.visits = visits;
        municipalityMap = initMunicipalities(municipalities);
        updateSelectedUser(selectedUser);
    }

    private static Map<String, MunicipalityData> initMunicipalities(MunicipalityCollection municipalities) {
        Map<String, MunicipalityData> municipalityMap = new HashMap<>();
        for (MunicipalityData municipality : municipalities.getMunicipalities()) {
            municipality.getZones().forEach(zoneData -> municipalityMap.put(zoneData.getName(), municipality));
        }
        return municipalityMap;
    }

    public void updateSelectedUser(UserData selectedUser) {
        List<VisitData> currentVisits = new ArrayList<>(visits.getVisits(selectedUser));
        currentVisits.sort(Comparator.comparing(VisitData::getWhen));
        currentSessions = createSessions(currentVisits);

        fireTableDataChanged();
    }

    private List<SessionData> createSessions(List<VisitData> currentVisits) {
        List<SessionData> sessionData = new ArrayList<>();
        Instant start = null;
        Instant last = null;
        Set<MunicipalityData> municipalities = new HashSet<>();
        int takes = 0;
        int revisits = 0;
        int assists = 0;
        int tp = 0;
        int pph = 0;
        for (VisitData visit : currentVisits) {
            if (last == null || visit.getWhen().isAfter(last.plusSeconds(SESSION_HOUR_LENGTH))) {
                if (last != null) {
                    sessionData.add(new SessionData(start, Duration.between(start, last), toString(municipalities), takes, revisits, assists, tp, pph));
                    municipalities.clear();
                    takes = 0;
                    revisits = 0;
                    assists = 0;
                    tp = 0;
                    pph = 0;
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
            sessionData.add(new SessionData(start, Duration.between(start, last), toString(municipalities), takes, revisits, assists, tp, pph));
        }
        return sessionData;
    }

    private static String toString(Set<MunicipalityData> municipalities) {
        Map<String, Set<String>> regionMunicipalities = new HashMap<>();
        for (MunicipalityData municipality : municipalities) {
            String regionName = municipality.getRegion().getName();
            Set<String> names = regionMunicipalities.get(regionName);
            if (names == null) {
                names = new HashSet<>();
                regionMunicipalities.put(regionName, names);
            }
            names.add(municipality.getName());
        }
        return regionMunicipalities.keySet().stream()
                .sorted()
                .map(regionName -> String.format("%s / %s",
                        regionMunicipalities.get(regionName).stream()
                                .sorted()
                                .collect(Collectors.joining(",")),
                        regionName))
                .collect(Collectors.joining(", "));
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
        switch (columnIndex) {
            case 0:
                return session.start;
            case 1:
                return session.duration;
            case 2:
                return session.municipalities;
            case 3:
                return session.takes;
            case 4:
                return session.revisits;
            case 5:
                return session.assists;
            case 6:
                return session.tp;
            case 7:
                return session.getTpH();
            case 8:
                return session.pph;
            case 9:
                return session.tp + session.pph;
            case 10:
                return session.getTpPphH();
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
}
