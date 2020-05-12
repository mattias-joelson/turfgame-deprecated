package org.joelson.mattias.turfgame.application.view;

import org.joelson.mattias.turfgame.application.model.UserData;
import org.joelson.mattias.turfgame.application.model.VisitCollection;
import org.joelson.mattias.turfgame.application.model.VisitData;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.table.AbstractTableModel;

class SessionTableModel extends AbstractTableModel {

    private static class SessionData {
        private final Instant start;
        private final Duration duration;
        private final int takes;
        private final int revisits;
        private final int assists;
        private final int tp;

        private SessionData(Instant start, Duration duration, int takes, int revisits, int assists, int tp) {
            this.start = start;
            this.duration = duration;
            this.takes = takes;
            this.revisits = revisits;
            this.assists = assists;
            this.tp = tp;
        }

        public int getTpH() {
            if (takes + revisits + assists < 2) {
                return tp;
            }
            int seconds = (int) duration.getSeconds();
            return (tp * 3600) / seconds;
        }
    }

    private static final String[] COLUMN_NAMES = { "When", "Duration", "Takes", "Revisits", "Assists", "TP", "TP / h" };
    private static final Class<?>[] COLUMN_CLASSES = { Instant.class, Duration.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class };
    private static final long serialVersionUID = 1L;
    private static final int SESSION_HOUR_LENGTH = 1200;

    private final VisitCollection visits;
    private List<SessionData> currentSessions;

    public SessionTableModel(VisitCollection visits, UserData selectedUser) {
        this.visits = visits;
        updateSelectedUser(selectedUser);
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
        int takes = 0;
        int revisits = 0;
        int assists = 0;
        int tp = 0;
        for (VisitData visit : currentVisits) {
            if (last == null || visit.getWhen().isAfter(last.plusSeconds(SESSION_HOUR_LENGTH))) {
                if (last != null) {
                    sessionData.add(new SessionData(start, Duration.between(start, last), takes, revisits, assists, tp));
                    takes = 0;
                    revisits = 0;
                    assists = 0;
                    tp = 0;
                }
                start = visit.getWhen();
            }
            last = visit.getWhen();
            switch (visit.getType()) {
                case "Take": // NON-NLS
                    takes += 1;
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
            sessionData.add(new SessionData(start, Duration.between(start, last), takes, revisits, assists, tp));
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
        switch (columnIndex) {
            case 0:
                return session.start;
            case 1:
                return session.duration;
            case 2:
                return session.takes;
            case 3:
                return session.revisits;
            case 4:
                return session.assists;
            case 5:
                return session.tp;
            case 6:
                return session.getTpH();
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
