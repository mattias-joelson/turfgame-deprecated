package org.joelson.mattias.turfgame.application.view;

import org.joelson.mattias.turfgame.application.model.ApplicationData;

import javax.swing.Action;

final class RegionHistoryTableActionCreator {

    private RegionHistoryTableActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated");
    }

    public static Action create(ApplicationUI applicationUI) {
        Action action = new ActionBuilder(actionEvent -> showRegionHistoryTable(applicationUI))
                .withName("Show Region History Table")
                .build();
        action.setEnabled(false);
        ActionUtil.addEnabledPropertyChangeListener(applicationUI.getApplicationData(), action, ApplicationData.HAS_DATABASE, true);
        return action;
    }

    private static void showRegionHistoryTable(ApplicationUI applicationUI) {
        RegionHistoryTableModel tableModel = new RegionHistoryTableModel(applicationUI.getApplicationData().getRegions().getRegionHistory());
        applicationUI.setPane(TableUtil.createDefaultTablePane(tableModel, "Region Filter"));
    }
}
