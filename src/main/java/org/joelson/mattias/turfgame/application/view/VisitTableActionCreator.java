package org.joelson.mattias.turfgame.application.view;

import org.joelson.mattias.turfgame.application.model.ApplicationData;
import org.joelson.mattias.turfgame.application.model.UserData;

import java.awt.Container;
import java.awt.event.KeyEvent;
import javax.swing.Action;

final class VisitTableActionCreator {
    
    private VisitTableActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }
    
    public static Action create(ApplicationUI applicationUI) {
        Action action = new ActionBuilder(actionEvent -> showVisitTable(applicationUI))
                .withName("Show Visit Table")
                .withAcceleratorKey(KeyEvent.VK_V)
                .build();
        action.setEnabled(false);
        ActionUtil.addEnabledPropertyChangeListener(applicationUI.getApplicationData(), action, ApplicationData.HAS_DATABASE, true);
        return action;
    }
    
    private static void showVisitTable(ApplicationUI applicationUI) {
        ApplicationData applicationData = applicationUI.getApplicationData();
        UserData selectedUser = UserSelectionUtil.getSelectedUser(applicationData);
        VisitTableModel tableModel = new VisitTableModel(applicationData.getVisits(), applicationData.getMunicipalities(), selectedUser);
        Container tableContainer = TableUtil.createDefaultTablePane(tableModel, "Visit Filter");
        applicationUI.setPane(UserSelectionUtil.createContainer(applicationData, selectedUser, tableModel::updateSelectedUser, tableContainer));
        applicationUI.setApplicationDataStatus();
    }
}
