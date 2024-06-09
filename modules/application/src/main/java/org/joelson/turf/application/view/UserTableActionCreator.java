package org.joelson.mattias.turfgame.application.view;

import org.joelson.mattias.turfgame.application.model.ApplicationData;
import org.joelson.mattias.turfgame.application.model.UserCollection;
import org.joelson.mattias.turfgame.application.model.UserData;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

final class UserTableActionCreator {
    
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
        JTable userTable = TableUtil.createDefaultTable(userTableModel);
        createPopupMenu(applicationUI, userTable);
        applicationUI.setPane(TableUtil.createDefaultTablePane(userTable, "User Filter"));
    }

    private static void createPopupMenu(ApplicationUI applicationUI, JTable userTable) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem setCurrentUser = new JMenuItem("Set current user");
        setCurrentUser.addActionListener(e -> setCurrentUser(applicationUI, userTable));
        popupMenu.add(setCurrentUser);
        PopupMenuUtil.addToTable(userTable, popupMenu);
    }

    private static void setCurrentUser(ApplicationUI applicationUI, JTable userTable) {
        int row = userTable.getSelectedRow();
        int userId = (int) userTable.getValueAt(row, 0);
        UserCollection userCollection = applicationUI.getApplicationData().getUsers();
        UserData user = userCollection.getUser(userId);
        if (user != null) {
            userCollection.setSelectedUser(user);
            applicationUI.setApplicationDataStatus();
        }
    }
}
