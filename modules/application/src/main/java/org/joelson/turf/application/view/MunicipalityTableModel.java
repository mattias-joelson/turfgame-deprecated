package org.joelson.turf.application.view;

import org.joelson.turf.application.model.MunicipalityData;

import javax.swing.table.AbstractTableModel;
import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MunicipalityTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = { "Country", "Region", "Municipality", "Zones" };
    private static final Class<?>[] COLUMN_CLASSES = { String.class, String.class, String.class, Integer.class };
    @Serial
    private static final long serialVersionUID = 1L;

    private final ArrayList<MunicipalityData> municipalities;

    public MunicipalityTableModel(List<MunicipalityData> municipalities) {
        this.municipalities = new ArrayList<>(municipalities);
        this.municipalities.sort(MunicipalityTableModel::sortMunicipalities);
    }

    private static int sortMunicipalities(MunicipalityData m1, MunicipalityData m2) {
        int cmpRegion = RegionTableModel.compareRegions(m1.getRegion(), m2.getRegion());
        if (cmpRegion != 0) {
            return cmpRegion;
        }
        int cmpName = m1.getName().compareTo(m2.getName());
        return (cmpName != 0) ? cmpName : -1;
    }

    @Override
    public int getRowCount() {
        return municipalities.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        MunicipalityData municipality = municipalities.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> municipality.getRegion().getCountry();
            case 1 -> municipality.getRegion().getName();
            case 2 -> municipality.getName();
            case 3 -> municipality.getZones().size();
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

    public List<MunicipalityData> getMunicipalities(int[] selectedIndices) {
        return Arrays.stream(selectedIndices).mapToObj(municipalities::get).toList();
    }
}
