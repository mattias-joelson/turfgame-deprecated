package org.joelson.mattias.turfgame.application.view;

import org.joelson.mattias.turfgame.application.model.ApplicationData;
import org.joelson.mattias.turfgame.application.model.MunicipalityData;
import org.joelson.mattias.turfgame.application.model.UserData;
import org.joelson.mattias.turfgame.application.model.ZoneVisitCollection;

import java.awt.Container;
import java.util.List;
import javax.swing.Action;

final class MunicipalityVisitTableActionCreator {

    private MunicipalityVisitTableActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated"); //NON-NLS
    }

    public static Action create(ApplicationUI applicationUI) {
        Action action = new ActionBuilder(actionEvent -> showMunicipalityVisitTable(applicationUI))
                .withName("Show Municipality Visit Table")
                .build();
        action.setEnabled(false);
        ActionUtil.addEnabledPropertyChangeListener(applicationUI.getApplicationData(), action, ApplicationData.HAS_DATABASE, true);
        return action;
    }

    private static void showMunicipalityVisitTable(ApplicationUI applicationUI) {
        ApplicationData applicationData = applicationUI.getApplicationData();
        List<MunicipalityData> municipalities = applicationData.getMunicipalities().getMunicipalities();
        ZoneVisitCollection zoneVisits = applicationData.getZoneVisits();
        UserData selectedUser = UserSelectionUtil.getSelectedUser(applicationData);
        MunicipalityVisitTableModel tableModel = new MunicipalityVisitTableModel(municipalities, zoneVisits, selectedUser);
        Container tableContainer = TableUtil.createDefaultTablePane(tableModel, "Visit Filter");
        // TODO fix users
        applicationUI.setPane(UserSelectionUtil.createContainer(applicationData, selectedUser, tableModel::updateSelectedUser, tableContainer));
    }
}
