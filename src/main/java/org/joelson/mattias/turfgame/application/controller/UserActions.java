package org.joelson.mattias.turfgame.application.controller;

import org.joelson.mattias.turfgame.apiv4.User;
import org.joelson.mattias.turfgame.apiv4.Users;
import org.joelson.mattias.turfgame.application.model.ApplicationData;
import org.joelson.mattias.turfgame.application.model.UserData;
import org.joelson.mattias.turfgame.application.view.ApplicationUI;

import java.io.IOException;
import java.util.List;

public class UserActions {
    
    private final ApplicationUI applicationUI;
    private final ApplicationData applicationData;
    
    public UserActions(final ApplicationUI applicationUI, final ApplicationData applicationData) {
        this.applicationUI = applicationUI;
        this.applicationData = applicationData;
    }
    
    public void userSelected(String username, String userId) {
        Integer id = null;
        if (userId != null && !userId.isEmpty()) {
            id = Integer.valueOf(userId);
        }
        try {
            List<User> users = Users.getUsers(username, id);
            if (users.size() != 1) {
                applicationUI.showErrorDialog(String.format("Found %d users matching user '%s' id '%s'!", users.size(), username, userId),
                        "Wrong number of users found");
            } else {
                User user = users.get(0);
                applicationUI.showMessageDialog(String.format("Found user '%s' id '%d'", user.getName(), user.getId()), "User found");
                applicationData.setCurrentUser(new UserData(user.getName(), user.getId()));
                applicationUI.clearPane();
                applicationUI.setStatus("User " + user.getName());
            }
        } catch (IOException e) {
            applicationUI.showErrorDialog(String.format("Unable to get user '%s' id '%s'!%n%s", username, userId, e.getMessage()), "Unable to get user");
            e.printStackTrace();
        }
    }
    
    public void cancelUserSelecton() {
        applicationUI.clearPane();
    }
}
