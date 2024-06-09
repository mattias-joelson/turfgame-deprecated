package org.joelson.turf.application.view;

import org.joelson.turf.application.model.ApplicationData;
import org.joelson.turf.application.model.UserData;

import javax.swing.Action;
import java.awt.Container;

final class SessionTableActionCreator {

    private SessionTableActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated");
    }

    public static Action create(ApplicationUI applicationUI) {
        Action action = new ActionBuilder(actionEvent -> showSessionTable(applicationUI)).withName("Show Session Table")
                .build();
        action.setEnabled(false);
        ActionUtil.addEnabledPropertyChangeListener(applicationUI.getApplicationData(), action,
                ApplicationData.HAS_DATABASE, true);
        return action;
    }

    private static void showSessionTable(ApplicationUI applicationUI) {
        ApplicationData applicationData = applicationUI.getApplicationData();
        UserData selectedUser = UserSelectionUtil.getSelectedUser(applicationData);
        SessionTableModel tableModel = new SessionTableModel(applicationData.getVisits(),
                applicationData.getMunicipalities(), selectedUser);
        Container tableContainer = TableUtil.createDefaultTablePane(tableModel, "Visit Filter");
        applicationUI.setPane(
                UserSelectionUtil.createContainer(applicationData, selectedUser, tableModel::updateSelectedUser,
                        tableContainer));
        applicationUI.setApplicationDataStatus();

    }
}
