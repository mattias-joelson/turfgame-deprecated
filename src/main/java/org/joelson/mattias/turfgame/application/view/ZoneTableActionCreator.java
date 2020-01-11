package org.joelson.mattias.turfgame.application.view;

import org.joelson.mattias.turfgame.application.model.ApplicationData;
import org.joelson.mattias.turfgame.application.model.ZoneData;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.List;
import javax.swing.Action;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public final class ZoneTableActionCreator {
    
    private ZoneTableActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }
    
    public static Action create(ApplicationUI applicationUI) {
        Action action = new ActionBuilder(actionEvent -> showZoneTable(applicationUI))
                .withName("Show Zone Table")
                .withAcceleratorKey('Z')
                .build();
        action.setEnabled(false);
        ActionUtil.addEnabledPropertyChangeListener(applicationUI.getApplicationData(), action, ApplicationData.HAS_DATABASE, true);
        return action;
    }
    
    private static void showZoneTable(ApplicationUI applicationUI) {
        JTable table = createZoneTable(applicationUI.getApplicationData().getZones().getZones());
    
        applicationUI.setPane(createTableContainer(table, createFilterContainer(createTableSorter(table))));
    }
    
    private static Container createTableContainer(JTable table, Container filterContainer) {
        Container tableContainer = new Container();
        tableContainer.setLayout(new BorderLayout());
        tableContainer.add(filterContainer, BorderLayout.PAGE_START);
        tableContainer.add(new JScrollPane(table), BorderLayout.CENTER);
        return tableContainer;
    }
    
    private static JTable createZoneTable(List<ZoneData> zones) {
        JTable table = new JTable(new ZoneTableModel(zones));
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        table.setDefaultEditor(Object.class, null); // disable edit
        return table;
    }
    
    private static TableRowSorter<TableModel> createTableSorter(JTable table) {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);
        return sorter;
    }
    
    private static Container createFilterContainer(TableRowSorter<TableModel> sorter) {
        Container filterContainer = new Container();
        GroupLayout groupLayout = new GroupLayout(filterContainer);
        filterContainer.setLayout(groupLayout);
        JLabel zoneFilterLabel = new JLabel("Zone Filter");
        JTextField zoneFilterField = new JTextField("");
        zoneFilterField.getDocument().addDocumentListener(new ZoneFilterDocumentListener(sorter, zoneFilterField));
    
        filterContainer.add(zoneFilterLabel);
        filterContainer.add(zoneFilterField);
        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
                .addComponent(zoneFilterLabel)
                .addComponent(zoneFilterField));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                .addComponent(zoneFilterLabel)
                .addComponent(zoneFilterField));
    
        return filterContainer;
    }
    
    private static final class ZoneFilterDocumentListener implements DocumentListener {
        
        private final TableRowSorter<TableModel> sorter;
        private final JTextField zoneFilterField;
        
        private ZoneFilterDocumentListener(TableRowSorter<TableModel> sorter, JTextField zoneFilterField) {
            this.sorter = sorter;
            this.zoneFilterField = zoneFilterField;
        }
        
        @Override
        public void insertUpdate(DocumentEvent e) {
            updateFilter();
        }
        
        @Override
        public void removeUpdate(DocumentEvent e) {
            updateFilter();
        }
        
        @Override
        public void changedUpdate(DocumentEvent e) {
            updateFilter();
        }

        private void updateFilter() {
            String filterText = zoneFilterField.getText();
            RowFilter<TableModel, Object> rf = RowFilter.regexFilter(filterText, 0, 1, 2, 3, 4, 5, 6, 7, 8);
            sorter.setRowFilter(rf);
        }
    }
}
