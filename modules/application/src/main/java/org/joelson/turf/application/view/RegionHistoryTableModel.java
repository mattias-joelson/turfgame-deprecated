package org.joelson.mattias.turfgame.application.view;

import org.joelson.mattias.turfgame.application.model.RegionHistoryData;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class RegionHistoryTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = { "ID", "Name", "Country", "From" };
    private static final Class<?>[] COLUMN_CLASSES = { Integer.class, String.class, String.class, Instant.class };
    private static final long serialVersionUID = 1L;

    private final ArrayList<RegionHistoryData> regionHistory;

    public RegionHistoryTableModel(List<RegionHistoryData> regionHistory) {
        this.regionHistory = new ArrayList<>(regionHistory);
        this.regionHistory.sort(RegionHistoryTableModel::compare);
    }


    @Override
    public int getRowCount() {
        return regionHistory.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        RegionHistoryData region = regionHistory.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return region.getId();
            case 1:
                return region.getName();
            case 2:
                return region.getCountry();
            case 3:
                return region.getFrom();
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

    private static int compare(RegionHistoryData regionHistoryData1, RegionHistoryData regionHistoryData2) {
        int cmpId = regionHistoryData1.getId() - regionHistoryData2.getId();
        if (cmpId != 0) {
            return cmpId;
        }
        int cmpFrom = regionHistoryData1.getFrom().compareTo(regionHistoryData2.getFrom());
        if (cmpFrom != 0) {
            return cmpFrom;
        }
        int cmpCountry = RegionTableModel.compareCountry(regionHistoryData1.getCountry(), regionHistoryData2.getCountry());
        if (cmpCountry != 0) {
            return cmpCountry;
        }
        int cmpName = regionHistoryData1.getName().compareTo(regionHistoryData2.getName());
        if (cmpName != 0) {
            return cmpName;
        }
        return -1;
    }
}
