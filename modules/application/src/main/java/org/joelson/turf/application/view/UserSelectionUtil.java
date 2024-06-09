package org.joelson.turf.application.view;

import org.joelson.turf.application.model.ApplicationData;
import org.joelson.turf.application.model.UserCollection;
import org.joelson.turf.application.model.UserData;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.io.Serial;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import java.util.function.Consumer;

final class UserSelectionUtil {

    private UserSelectionUtil() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated");
    }

    public static Container createContainer(
            ApplicationData applicationData, UserData selectedUser, Consumer<UserData> userSelectionListener,
            Container tableContainer) {
        return createContainer(applicationData.getUsers().getUsers(), selectedUser, userSelectionListener,
                tableContainer);
    }

    public static Container createContainer(
            List<UserData> users, UserData selectedUser, Consumer<UserData> userSelectionListener,
            Container tableContainer) {
        return createContainer(createUserSelectionContainer(users, selectedUser, userSelectionListener),
                tableContainer);
    }

    private static Container createContainer(Container userContainer, Container tableContainer) {
        Container container = new Container();
        container.setLayout(new BorderLayout());
        container.add(userContainer, BorderLayout.PAGE_START);
        container.add(tableContainer, BorderLayout.CENTER);
        return container;
    }

    public static UserData getSelectedUser(ApplicationData applicationData) {
        return getSelectedUser(applicationData, applicationData.getUsers().getUsers());
    }

    public static UserData getSelectedUser(ApplicationData applicationData, List<UserData> existingUsers) {
        UserCollection users = applicationData.getUsers();
        UserData user = users.getSelectedUser();
        if (user == null || !existingUsers.contains(user)) {
            if (!existingUsers.isEmpty()) {
                user = existingUsers.get(0);
                users.setSelectedUser(user);
            }
        }
        if (user == null) {
            throw new NullPointerException("No existing user!");
        }
        return user;
    }

    public static Container createUserSelectionContainer(
            List<UserData> users, UserData selectedUser, Consumer<UserData> userSelectionListener) {
        Container userContainer = new Container();
        GroupLayout groupLayout = new GroupLayout(userContainer);
        userContainer.setLayout(groupLayout);
        JLabel userLabel = new JLabel("User");
        ComboBoxModel<UserData> userComboBoxModel = createComboBoxModel(users);
        userComboBoxModel.setSelectedItem(selectedUser);
        JComboBox<UserData> userComboBox = new JComboBox<>(userComboBoxModel);
        userComboBox.setRenderer(new UserComboBoxCellRenderer());
        userComboBox.addActionListener(e -> userSelectionListener.accept((UserData) userComboBox.getSelectedItem()));

        userContainer.add(userLabel);
        userContainer.add(userComboBox);
        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup().addComponent(userLabel).addComponent(userComboBox));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(userLabel)
                .addComponent(userComboBox));

        return userContainer;
    }

    private static ComboBoxModel<UserData> createComboBoxModel(List<UserData> users) {
        Vector<UserData> userData = new Vector<>(users);
        userData.sort(Comparator.comparing(UserData::getName));
        return new DefaultComboBoxModel<>(userData);
    }

    private static final class UserComboBoxCellRenderer extends JLabel implements ListCellRenderer<UserData> {

        @Serial
        private static final long serialVersionUID = 1L;

        UserComboBoxCellRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
        }

        @Override
        public Component getListCellRendererComponent(
                JList<? extends UserData> list, UserData value, int index, boolean isSelected, boolean cellHasFocus) {
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setText((value != null) ? value.getName() : "<null>");
            setFont(list.getFont());
            return this;
        }
    }
}
