package org.joelson.mattias.turfgame.application.view;

import org.joelson.mattias.turfgame.application.model.ApplicationData;

import java.awt.event.KeyEvent;
import javax.swing.Action;

public final class UserTableActionCreator {
    
    private UserTableActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }
    
    public static Action create(ApplicationUI applicationUI) {
        Action action = new ActionBuilder(actionEvent -> showUserTable(applicationUI))
                .withName("Show User Table")
                .withAcceleratorKey(KeyEvent.VK_U)
                .build();
        action.setEnabled(false);
        ActionUtil.addEnabledPropertyChangeListener(applicationUI.getApplicationData(), action, ApplicationData.HAS_DATABASE, true);
        return action;
    }
    
    private static void showUserTable(ApplicationUI applicationUI) {
        UserTableModel userTableModel = new UserTableModel(applicationUI.getApplicationData().getUsers().getUsers());
        applicationUI.setPane(TableUtil.createDefaultTablePane(userTableModel, "User Filter"));
    }
}
