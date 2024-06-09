package org.joelson.turf.application.view;

import org.joelson.turf.application.model.UserData;

import javax.swing.table.AbstractTableModel;
import java.io.Serial;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class UserTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = { "Id", "Name" };
    private static final Class<?>[] COLUMN_CLASSES = { Integer.class, String.class };
    @Serial
    private static final long serialVersionUID = 1L;

    private final ArrayList<UserData> users;

    public UserTableModel(List<UserData> users) {
        this.users = new ArrayList<>(users);
        this.users.sort(Comparator.comparing(UserData::getName));
    }

    @Override
    public int getRowCount() {
        return users.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        UserData user = users.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> user.getId();
            case 1 -> user.getName();
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
