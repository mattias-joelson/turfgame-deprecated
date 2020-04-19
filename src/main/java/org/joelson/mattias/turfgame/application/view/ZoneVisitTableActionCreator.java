package org.joelson.mattias.turfgame.application.view;

import org.joelson.mattias.turfgame.application.model.ApplicationData;
import org.joelson.mattias.turfgame.application.model.UserData;

import java.awt.Container;
import javax.swing.Action;

final class ZoneVisitTableActionCreator {

    private ZoneVisitTableActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated"); //NON-NLS
    }

    public static Action create(ApplicationUI applicationUI) {
        Action action = new ActionBuilder(actionEvent -> showZoneVisitTable(applicationUI))
                .withName("Show Zone Visit Table")
                .build();
        action.setEnabled(false);
        ActionUtil.addEnabledPropertyChangeListener(applicationUI.getApplicationData(), action, ApplicationData.HAS_DATABASE, true);
        return action;
    }

    private static void showZoneVisitTable(ApplicationUI applicationUI) {
        ApplicationData applicationData = applicationUI.getApplicationData();
        UserData selectedUser = UserSelectionUtil.getSelectedUser(applicationData);
        ZoneVisitTableModel tableModel = new ZoneVisitTableModel(applicationData.getZoneVisits(), selectedUser);
        Container tableContainer = TableUtil.createDefaultTablePane(tableModel, "Zone Filter");
        applicationUI.setPane(UserSelectionUtil.createContainer(applicationData, selectedUser, tableModel::updateSelectedUser, tableContainer));
    }
}
