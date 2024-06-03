package org.joelson.mattias.turfgame.application.view;

import org.joelson.mattias.turfgame.application.model.ApplicationData;
import org.joelson.mattias.turfgame.application.model.ZoneCollection;

import javax.swing.Action;

final class ZonePointsHistoryTableActionCreator {

    private ZonePointsHistoryTableActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated");
    }

    public static Action create(ApplicationUI applicationUI) {
        Action action = new ActionBuilder(actionEvent -> showZonePointsTable(applicationUI))
                .withName("Show Zone Points History Table")
                .build();
        action.setEnabled(false);
        ActionUtil.addEnabledPropertyChangeListener(applicationUI.getApplicationData(), action, ApplicationData.HAS_DATABASE, true);
        return action;
    }

    private static void showZonePointsTable(ApplicationUI applicationUI) {
        ZoneCollection zones = applicationUI.getApplicationData().getZones();
        ZonePointsHistoryTableModel tableModel = new ZonePointsHistoryTableModel(zones.getZones(), zones.getZonePointsHistory());
        applicationUI.setPane(TableUtil.createDefaultTablePane(tableModel, "Zone Filter"));
    }
}
