package org.joelson.mattias.turfgame.application.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.stream.IntStream;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

final class TableUtil {
    
    private TableUtil() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!"); //NON-NLS
    }
    
    public static Container createDefaultTablePane(TableModel tabelModel, String filterLabelText) {
        return createDefaultTablePane(createDefaultTable(tabelModel), filterLabelText);
    }

    public static Container createDefaultTablePane(JTable table, String filterLabelText) {
        TableRowSorter<TableModel> tableSorter = createTableSorter(table);
        Container filterContainer = createFilterContainer(tableSorter, filterLabelText);
        return createTableContainer(table, filterContainer);
    }
    
    public static JTable createDefaultTable(TableModel tableModel) {
        JTable table = new JTable(tableModel);
        table.setShowGrid(true);
        table.setGridColor(Color.BLACK);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        table.setDefaultEditor(Object.class, null); // disable edit
        table.setDefaultRenderer(Double.class, new DoubleRenderer());
        table.setDefaultRenderer(Duration.class, new DurationRenderer());
        table.setDefaultRenderer(Instant.class, new InstantRenderer());
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

    private static final class DoubleRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 1L;

        private NumberFormat formatter;

        private DoubleRenderer() {
            setHorizontalAlignment(SwingConstants.RIGHT);
        }

        @Override
        public void setValue(Object value) {
            if (formatter == null) {
                formatter = NumberFormat.getInstance();
                formatter.setMinimumFractionDigits(6);
                formatter.setMaximumFractionDigits(6);
            }
            setText((value == null) ? "" : formatter.format(value));
        }
    }

    private static final class InstantRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 1L;

        private transient DateTimeFormatter formatter;

        private InstantRenderer() {
            setHorizontalAlignment(SwingConstants.RIGHT);
        }

        @Override
        public void setValue(Object value) {
            if (formatter == null) {
                formatter = new DateTimeFormatterBuilder()
                        .parseCaseInsensitive()
                        .append(DateTimeFormatter.ISO_LOCAL_DATE)
                        .appendLiteral(' ')
                        .append(DateTimeFormatter.ISO_LOCAL_TIME)
                        .toFormatter()
                        .withZone(ZoneId.systemDefault());
            }
            if (value instanceof Instant) {
                setText(formatter.format((Instant) value));
            } else {
                setText((value == null) ? "" : value.toString());
            }
        }
    }

    private static final class DurationRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 1L;

        private DurationRenderer() {
            setHorizontalAlignment(SwingConstants.RIGHT);
        }

        @Override
        public void setValue(Object value) {
            if (value instanceof Duration) {
                Duration duration = (Duration) value;
                String dayString;
                long days = duration.toDays();
                if (days == 0) {
                    setText(String.format("%d:%02d:%02d", duration.toHours(), duration.toMinutesPart(), duration.toSecondsPart()));
                } else if (days == 1) {
                    setText(String.format("1 day %02d:%02d:%02d", duration.toHoursPart(), duration.toMinutesPart(), duration.toSecondsPart()));
                } else {
                    setText(String.format("%d days %02d:%02d:%02d", duration.toDays(), duration.toHoursPart(), duration.toMinutesPart(), duration.toSecondsPart()));
                }
            } else {
                setText((value == null) ? "" : value.toString());
            }
        }
    }
}
