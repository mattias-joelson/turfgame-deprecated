package org.joelson.mattias.turfgame.application.view;

import org.joelson.mattias.turfgame.application.model.ZoneData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

class ZoneTableModel extends AbstractTableModel {
    
    private static final String[] COLUMN_NAMES = { "Name", "ID", "Region Name", "Country", "Latitude", "Longitude", "Date Created", "TP", "PPH" }; //NON-NLS
    private static final long serialVersionUID = 1L;

    private final List<ZoneData> zones;
    
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
        switch (columnIndex) {
        case 0:
            return zone.getId();
        case 1:
            return zone.getName();
        case 2:
            return zone.getRegion().getName();
        case 3:
            return zone.getRegion().getCountry();
        case 4:
            return zone.getLatitude();
        case 5:
            return zone.getLongitude();
        case 6:
            return zone.getDateCreated();
        case 7:
            return zone.getTp();
        case 8:
            return zone.getPph();
        default:
            throw new IllegalArgumentException("Invalid columnIndex " + columnIndex);
        }
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }
    
    public static void initTableRowSorter(TableRowSorter<TableModel> tableRowSorter) {
        tableRowSorter.setComparator(0, TableUtil.getIntegerComparator());
        tableRowSorter.setComparator(4, TableUtil.getDoubleComparator());
        tableRowSorter.setComparator(5, TableUtil.getDoubleComparator());
        tableRowSorter.setComparator(7, TableUtil.getIntegerComparator());
        tableRowSorter.setComparator(8, TableUtil.getIntegerComparator());
    }
}
