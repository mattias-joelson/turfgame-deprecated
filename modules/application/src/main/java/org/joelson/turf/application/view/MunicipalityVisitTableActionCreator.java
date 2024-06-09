package org.joelson.turf.application.view;

import org.joelson.turf.application.model.ApplicationData;
import org.joelson.turf.application.model.MunicipalityData;
import org.joelson.turf.application.model.UserData;
import org.joelson.turf.application.model.ZoneVisitCollection;

import javax.swing.Action;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

final class MunicipalityVisitTableActionCreator {

    private MunicipalityVisitTableActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated"); //NON-NLS
    }

    public static Action create(ApplicationUI applicationUI) {
        Action action = new ActionBuilder(actionEvent -> showMunicipalityVisitTable(applicationUI))
                .withName("Show Municipality Visit Table").build();
        action.setEnabled(false);
        ActionUtil.addEnabledPropertyChangeListener(applicationUI.getApplicationData(), action,
                ApplicationData.HAS_DATABASE, true);
        return action;
    }

    private static void showMunicipalityVisitTable(ApplicationUI applicationUI) {
        ApplicationData applicationData = applicationUI.getApplicationData();
        List<MunicipalityData> municipalities = applicationData.getMunicipalities().getMunicipalities();
        MunicipalityTableModel municipalityTableModel = new MunicipalityTableModel(municipalities);
        JTable municipalityTable = TableUtil.createDefaultTable(municipalityTableModel);
        Container municipalityTableContainer = TableUtil.createDefaultTablePane(municipalityTable,
                "Municipality Filter");
        ZoneVisitCollection zoneVisits = applicationData.getZoneVisits();
        List<UserData> zoneVisitUsers = zoneVisits.getZoneVisitUsers();
        UserData selectedUser = UserSelectionUtil.getSelectedUser(applicationData, zoneVisitUsers);
        MunicipalityVisitTableModel tableModel = new MunicipalityVisitTableModel(municipalities, zoneVisits,
                selectedUser);
        Container municipalityVisitTableContainer = TableUtil.createDefaultTablePane(tableModel, "Visit Filter");
        Container tablesContainer = createContainer(municipalityTableContainer, municipalityVisitTableContainer);
        applicationUI.setPane(
                UserSelectionUtil.createContainer(zoneVisitUsers, selectedUser, tableModel::updateSelectedUser,
                        tablesContainer));
        applicationUI.setApplicationDataStatus();

        ListSelectionModel listSelectionModel = municipalityTable.getSelectionModel();
        municipalityTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                tableModel.updateSelectedMunicipalities(
                        municipalityTableModel.getMunicipalities(listSelectionModel.getSelectedIndices()));
            }
        });
    }

    private static Container createContainer(
            Container municipalityTableContainer, Container municipalityVisitTableContainer) {
        Container container = new Container();
        container.setLayout(new GridBagLayout());
        container.add(municipalityTableContainer, createConstraints(0, 0.3));
        container.add(municipalityVisitTableContainer, createConstraints(1, 0.7));
        return container;
    }

    private static GridBagConstraints createConstraints(int gridy, double weighty) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = gridy;
        constraints.weightx = 1;
        constraints.weighty = weighty;
        constraints.fill = GridBagConstraints.BOTH;
        return constraints;
    }
}
