package org.joelson.mattias.turfgame.application.view;

import org.joelson.mattias.turfgame.application.model.ApplicationData;

import java.awt.event.KeyEvent;
import javax.swing.Action;

final class RegionTableActionCreator {

    private RegionTableActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated");
    }

    public static Action create(ApplicationUI applicationUI) {
        Action action = new ActionBuilder(actionEvent -> showRegionTable(applicationUI))
                .withName("Show Region Table")
                .withAcceleratorKey(KeyEvent.VK_R)
                .build();
            action.setEnabled(false);
            ActionUtil.addEnabledPropertyChangeListener(applicationUI.getApplicationData(), action, ApplicationData.HAS_DATABASE, true);
            return action;
    }

    private static void showRegionTable(ApplicationUI applicationUI) {
        RegionTableModel tableModel = new RegionTableModel(applicationUI.getApplicationData().getRegions().getRegions());
        applicationUI.setPane(TableUtil.createDefaultTablePane(tableModel, "Region Filter"));
    }
}