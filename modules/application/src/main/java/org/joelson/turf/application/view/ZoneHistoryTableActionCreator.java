package org.joelson.turf.application.view;

import org.joelson.turf.application.model.ApplicationData;

import javax.swing.Action;

final class ZoneHistoryTableActionCreator {

    private ZoneHistoryTableActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated");
    }

    public static Action create(ApplicationUI applicationUI) {
        Action action = new ActionBuilder(actionEvent -> showZoneHistoryTable(applicationUI))
                .withName("Show Zone History Table").build();
        action.setEnabled(false);
        ActionUtil.addEnabledPropertyChangeListener(applicationUI.getApplicationData(), action,
                ApplicationData.HAS_DATABASE, true);
        return action;
    }

    private static void showZoneHistoryTable(ApplicationUI applicationUI) {
        ZoneHistoryTableModel tableModel = new ZoneHistoryTableModel(
                applicationUI.getApplicationData().getZones().getZoneHistory());
        applicationUI.setPane(TableUtil.createDefaultTablePane(tableModel, "Zone Filter"));
    }
}
