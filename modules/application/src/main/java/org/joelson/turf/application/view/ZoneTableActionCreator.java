package org.joelson.turf.application.view;

import org.joelson.turf.application.model.ApplicationData;

import javax.swing.Action;
import java.awt.event.KeyEvent;

final class ZoneTableActionCreator {

    private ZoneTableActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }

    public static Action create(ApplicationUI applicationUI) {
        Action action = new ActionBuilder(actionEvent -> showZoneTable(applicationUI)).withName("Show Zone Table")
                .withAcceleratorKey(KeyEvent.VK_Z).build();
        action.setEnabled(false);
        ActionUtil.addEnabledPropertyChangeListener(applicationUI.getApplicationData(), action,
                ApplicationData.HAS_DATABASE, true);
        return action;
    }

    private static void showZoneTable(ApplicationUI applicationUI) {
        ZoneTableModel tableModel = new ZoneTableModel(applicationUI.getApplicationData().getZones().getZones());
        applicationUI.setPane(TableUtil.createDefaultTablePane(tableModel, "Zone Filter"));
    }
}
