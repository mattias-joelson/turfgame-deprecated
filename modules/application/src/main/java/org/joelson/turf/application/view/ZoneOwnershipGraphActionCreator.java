package org.joelson.turf.application.view;

import org.joelson.turf.application.model.ApplicationData;
import org.joelson.turf.application.model.UserData;

import javax.swing.Action;
import java.awt.Container;

final class ZoneOwnershipGraphActionCreator {

    private ZoneOwnershipGraphActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated");
    }

    public static Action create(ApplicationUI applicationUI) {
        Action action = new ActionBuilder(actionEvent -> showZoneOwnershipGraph(applicationUI))
                .withName("Show Zone Ownership Graph").build();
        action.setEnabled(false);
        ActionUtil.addEnabledPropertyChangeListener(applicationUI.getApplicationData(), action,
                ApplicationData.HAS_DATABASE, true);
        return action;
    }

    private static void showZoneOwnershipGraph(ApplicationUI applicationUI) {
        ApplicationData applicationData = applicationUI.getApplicationData();
        UserData selectedUser = UserSelectionUtil.getSelectedUser(applicationData);
        ZoneOwnershipGraphModel zoneOwnershipGraphModel = new ZoneOwnershipGraphModel(applicationData.getVisits(),
                selectedUser);
        Container chartContainer = zoneOwnershipGraphModel.getChart();
        applicationUI.setPane(UserSelectionUtil.createContainer(applicationData, selectedUser,
                zoneOwnershipGraphModel::updateSelectedUser, chartContainer));
        applicationUI.setApplicationDataStatus();
    }
}
