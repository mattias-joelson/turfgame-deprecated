package org.joelson.mattias.turfgame.application.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.stream.IntStream;
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

final class TableUtil {
    
    private TableUtil() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }
    
    public static Container createDefaultTablePane(TableModel tabelModel, String filterLabelText) {
        JTable table = createDefaultTable(tabelModel);
        TableRowSorter<TableModel> tableSorter = createTableSorter(table);
        Container filterContainer = createFilterContainer(tableSorter, filterLabelText);
        return createTableContainer(table, filterContainer);
        
    }
    
    private static JTable createDefaultTable(TableModel tableModel) {
        JTable table = new JTable(tableModel);
        table.setShowGrid(true);
        table.setGridColor(Color.BLACK);
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
    
    private static Container createTableContainer(JTable table, Container filterContainer) {
        Container tableContainer = new Container();
        tableContainer.setLayout(new BorderLayout());
        tableContainer.add(filterContainer, BorderLayout.PAGE_START);
        tableContainer.add(new JScrollPane(table), BorderLayout.CENTER);
        return tableContainer;
    }
    
    private static Container createFilterContainer(TableRowSorter<TableModel> sorter, String filterLabelText) {
        Container filterContainer = new Container();
        GroupLayout groupLayout = new GroupLayout(filterContainer);
        filterContainer.setLayout(groupLayout);
        JLabel filterLabel = new JLabel(filterLabelText);
        JTextField filterField = new JTextField("");
        filterField.getDocument().addDocumentListener(new FilterDocumentListener(sorter, filterField));
        
        filterContainer.add(filterLabel);
        filterContainer.add(filterField);
        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
                .addComponent(filterLabel)
                .addComponent(filterField));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                .addComponent(filterLabel)
                .addComponent(filterField));
        
        return filterContainer;
    }
    
    private static final class FilterDocumentListener implements DocumentListener {
        
        private final TableRowSorter<TableModel> sorter;
        private final JTextField filterField;
        private final int[] columnIndices;
        
        private FilterDocumentListener(TableRowSorter<TableModel> sorter, JTextField filterField) {
            this.sorter = sorter;
            this.filterField = filterField;
            columnIndices = IntStream.range(0, sorter.getModel().getColumnCount()).toArray();
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
            String filterText = filterField.getText();
            RowFilter<TableModel, Object> rf = RowFilter.regexFilter(filterText, columnIndices);
            sorter.setRowFilter(rf);
        }
    }
}
