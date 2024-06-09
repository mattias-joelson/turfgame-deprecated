package org.joelson.turf.application.view;

import org.joelson.turf.application.model.ApplicationData;

import javax.swing.Action;
import java.awt.event.KeyEvent;

final class RegionTableActionCreator {

    private RegionTableActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated");
    }

    public static Action create(ApplicationUI applicationUI) {
        Action action = new ActionBuilder(actionEvent -> showRegionTable(applicationUI)).withName("Show Region Table")
                .withAcceleratorKey(KeyEvent.VK_R).build();
        action.setEnabled(false);
        ActionUtil.addEnabledPropertyChangeListener(applicationUI.getApplicationData(), action,
                ApplicationData.HAS_DATABASE, true);
        return action;
    }

    private static void showRegionTable(ApplicationUI applicationUI) {
        RegionTableModel tableModel = new RegionTableModel(
                applicationUI.getApplicationData().getRegions().getRegions());
        applicationUI.setPane(TableUtil.createDefaultTablePane(tableModel, "Region Filter"));
    }
}
