package org.joelson.mattias.turfgame.application.view;

import org.joelson.mattias.turfgame.application.model.ApplicationData;
import org.joelson.mattias.turfgame.application.model.ZoneDTO;

import java.util.List;
import java.util.stream.Collectors;
import javax.swing.Action;
import javax.swing.JScrollPane;
import javax.swing.JTable;

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
        JTable table = new JTable(createTableData(applicationUI.getApplicationData()), createTableColumns());
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        table.setDefaultEditor(Object.class, null); // disable edit
        applicationUI.setPane(scrollPane);
    }
    
    private static Object[][] createTableData(ApplicationData applicationData) {
        List<ZoneDTO> zones = applicationData.getZones().getZones();
        List<Object[]> tableRows = zones.stream().map(ZoneTableActionCreator::toArray).collect(Collectors.toList());
        return tableRows.toArray(new Object[tableRows.size()][]);
    }
    
    private static Object[] toArray(ZoneDTO zoneDTO) {
        return new Object[] { zoneDTO.getName(), zoneDTO.getId(), zoneDTO.getRegion().getName(), zoneDTO.getRegion().getCountry(), zoneDTO.getLatitude(),
                zoneDTO.getLongitude(), zoneDTO.getDateCreated(), zoneDTO.getTp(), zoneDTO.getPph() };
    }
    
    private static Object[] createTableColumns() {
        return new Object[] { "Name", "ID", "Region Name", "Country", "Latitude", "Longitude", "Date Created", "TP", "PPH"};
    }
}
