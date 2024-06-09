package org.joelson.turf.application.view;

import org.joelson.turf.application.model.ApplicationData;
import org.joelson.turf.application.model.UserData;
import org.joelson.turf.application.model.ZoneVisitCollection;

import javax.swing.Action;
import java.awt.Container;

final class ZoneVisitTableActionCreator {

    private ZoneVisitTableActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated");
    }

    public static Action create(ApplicationUI applicationUI) {
        Action action = new ActionBuilder(actionEvent -> showZoneVisitTable(applicationUI))
                .withName("Show Zone Visit Table").build();
        action.setEnabled(false);
        ActionUtil.addEnabledPropertyChangeListener(applicationUI.getApplicationData(), action,
                ApplicationData.HAS_DATABASE, true);
        return action;
    }

    private static void showZoneVisitTable(ApplicationUI applicationUI) {
        ApplicationData applicationData = applicationUI.getApplicationData();
        ZoneVisitCollection zoneVisits = applicationData.getZoneVisits();
        UserData selectedUser = UserSelectionUtil.getSelectedUser(applicationData, zoneVisits.getZoneVisitUsers());
        ZoneVisitTableModel tableModel = new ZoneVisitTableModel(zoneVisits, selectedUser);
        Container tableContainer = TableUtil.createDefaultTablePane(tableModel, "Zone Filter");
        applicationUI.setPane(
                UserSelectionUtil.createContainer(applicationData, selectedUser, tableModel::updateSelectedUser,
                        tableContainer));
        applicationUI.setApplicationDataStatus();
    }
}
