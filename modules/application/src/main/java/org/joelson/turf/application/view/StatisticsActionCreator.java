package org.joelson.mattias.turfgame.application.view;

import org.joelson.mattias.turfgame.application.model.ApplicationData;
import org.joelson.mattias.turfgame.application.model.UserData;
import org.joelson.mattias.turfgame.zundin.Monthly;
import org.joelson.mattias.turfgame.zundin.MonthlyZone;

import java.awt.Container;
import java.awt.event.KeyEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.Action;
import javax.swing.JTextArea;

final class StatisticsActionCreator {

    private StatisticsActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated"); //NON-NLS
    }

    public static Action create(ApplicationUI applicationUI) {
        Action action = new ActionBuilder(actionEvent -> showStatistics(applicationUI))
                .withName("Show Statistics")
                .withAcceleratorKey(KeyEvent.VK_S)
                .build();
        ActionUtil.addEnabledPropertyChangeListener(applicationUI.getApplicationData(), action, ApplicationData.HAS_DATABASE, true);
        return action;
    }

    private static void showStatistics(ApplicationUI applicationUI) {
        ApplicationData applicationData = applicationUI.getApplicationData();
        UserData selectedUser = UserSelectionUtil.getSelectedUser(applicationData);
        StatisticsModel statisticsModel = new StatisticsModel(applicationData.getVisits(), applicationData.getMunicipalities(), selectedUser);
        JTextArea dataContainer = new JTextArea();
        dataContainer.setText(statisticsModel.createData());
        applicationUI.setPane(UserSelectionUtil.createContainer(applicationData, selectedUser, userData -> dataContainer.setText(statisticsModel.updateSelectedUser(userData)), dataContainer));
        applicationUI.setApplicationDataStatus();
    }
}
