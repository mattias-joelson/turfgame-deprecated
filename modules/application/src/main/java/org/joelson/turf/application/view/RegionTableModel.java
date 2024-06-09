package org.joelson.turf.application.view;

import org.joelson.turf.application.model.RegionData;

import javax.swing.table.AbstractTableModel;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

class RegionTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = { "ID", "Name", "Country" };
    private static final Class<?>[] COLUMN_CLASSES = { Integer.class, String.class, String.class };
    @Serial
    private static final long serialVersionUID = 1L;

    private final ArrayList<RegionData> regions;

    public RegionTableModel(List<RegionData> regions) {
        this.regions = new ArrayList<>(regions);
        this.regions.sort(RegionTableModel::endCompareRegions);
    }

    private static int endCompareRegions(RegionData regionData1, RegionData regionData2) {
        int cmpRegions = compareRegions(regionData1, regionData2);
        return (cmpRegions != 0) ? cmpRegions : -1;
    }

    static int compareRegions(RegionData regionData1, RegionData regionData2) {
        int cmpCountry = compareCountry(regionData1.getCountry(), regionData2.getCountry());
        if (cmpCountry != 0) {
            return cmpCountry;
        }
        return regionData1.getName().compareTo(regionData2.getName());
    }

    static int compareCountry(String country1, String country2) {
        if (country1 == null) {
            return (country2 == null) ? 0 : 1;
        }
        if (country2 == null) {
            return -1;
        }
        return country1.compareTo(country2);
    }

    @Override
    public int getRowCount() {
        return regions.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        RegionData region = regions.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> region.getId();
            case 1 -> region.getName();
            case 2 -> region.getCountry();
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
