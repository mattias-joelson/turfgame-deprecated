package org.joelson.turf.application.view;

import org.joelson.turf.application.model.ApplicationData;
import org.joelson.turf.application.model.UserData;

import javax.swing.Action;
import javax.swing.JTextArea;
import java.awt.event.KeyEvent;

final class StatisticsActionCreator {

    private StatisticsActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated");
    }

    public static Action create(ApplicationUI applicationUI) {
        Action action = new ActionBuilder(actionEvent -> showStatistics(applicationUI)).withName("Show Statistics")
                .withAcceleratorKey(KeyEvent.VK_S).build();
        ActionUtil.addEnabledPropertyChangeListener(applicationUI.getApplicationData(), action,
                ApplicationData.HAS_DATABASE, true);
        return action;
    }

    private static void showStatistics(ApplicationUI applicationUI) {
        ApplicationData applicationData = applicationUI.getApplicationData();
        UserData selectedUser = UserSelectionUtil.getSelectedUser(applicationData);
        StatisticsModel statisticsModel = new StatisticsModel(applicationData.getVisits(),
                applicationData.getMunicipalities(), selectedUser);
        JTextArea dataContainer = new JTextArea();
        dataContainer.setText(statisticsModel.createData());
        applicationUI.setPane(UserSelectionUtil.createContainer(applicationData, selectedUser,
                userData -> dataContainer.setText(statisticsModel.updateSelectedUser(userData)), dataContainer));
        applicationUI.setApplicationDataStatus();
    }
}
