package org.joelson.mattias.turfgame.application.view;

import org.joelson.mattias.turfgame.application.model.ApplicationData;
import org.joelson.mattias.turfgame.application.model.UserData;

import java.awt.Container;
import javax.swing.Action;

final class SessionsTableActionCreator {

    private SessionsTableActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated"); //NON-NLS
    }

    public static Action create(ApplicationUI applicationUI) {
        Action action = new ActionBuilder(actionEvent -> showSessionTable(applicationUI))
                .withName("Show Sessions Table")
                .build();
        action.setEnabled(false);
        ActionUtil.addEnabledPropertyChangeListener(applicationUI.getApplicationData(), action, ApplicationData.HAS_DATABASE, true);
        return action;
    }

    private static void showSessionTable(ApplicationUI applicationUI) {
        ApplicationData applicationData = applicationUI.getApplicationData();
        UserData selectedUser = UserSelectionUtil.getSelectedUser(applicationData);
        SessionTableModel tableModel = new SessionTableModel(applicationData.getVisits(), selectedUser);
        Container tableContainer = TableUtil.createDefaultTablePane(tableModel, "Visit Filter");
        applicationUI.setPane(UserSelectionUtil.createContainer(applicationData, selectedUser, tableModel::updateSelectedUser, tableContainer));
        applicationUI.setApplicationDataStatus();

    }
}
