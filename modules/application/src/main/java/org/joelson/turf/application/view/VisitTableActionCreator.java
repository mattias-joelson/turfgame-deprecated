package org.joelson.turf.application.view;

import org.joelson.turf.application.model.ApplicationData;
import org.joelson.turf.application.model.UserData;

import javax.swing.Action;
import java.awt.Container;
import java.awt.event.KeyEvent;

final class VisitTableActionCreator {

    private VisitTableActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }

    public static Action create(ApplicationUI applicationUI) {
        Action action = new ActionBuilder(actionEvent -> showVisitTable(applicationUI)).withName("Show Visit Table")
                .withAcceleratorKey(KeyEvent.VK_V).build();
        action.setEnabled(false);
        ActionUtil.addEnabledPropertyChangeListener(applicationUI.getApplicationData(), action,
                ApplicationData.HAS_DATABASE, true);
        return action;
    }

    private static void showVisitTable(ApplicationUI applicationUI) {
        ApplicationData applicationData = applicationUI.getApplicationData();
        UserData selectedUser = UserSelectionUtil.getSelectedUser(applicationData);
        VisitTableModel tableModel = new VisitTableModel(applicationData.getVisits(),
                applicationData.getMunicipalities(), selectedUser);
        Container tableContainer = TableUtil.createDefaultTablePane(tableModel, "Visit Filter");
        applicationUI.setPane(
                UserSelectionUtil.createContainer(applicationData, selectedUser, tableModel::updateSelectedUser,
                        tableContainer));
        applicationUI.setApplicationDataStatus();
    }
}
