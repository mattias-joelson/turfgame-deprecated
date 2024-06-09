package org.joelson.turf.application.controller;

/*import org.joelson.mattias.turfgame.apiv4.User;
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
                applicationUI.showErrorDialog("Wrong number of users found",
                        String.format("Found %d users matching user '%s' id '%s'!", users.size(), username, userId));
            } else {
                User user = users.get(0);
                applicationUI.showMessageDialog("User found", String.format("Found user '%s' id '%d'", user.getName(), user.getId()));
                applicationData.setCurrentUser(new UserData(user.getName(), user.getId()));
                applicationUI.clearPane();
                applicationUI.setStatus(applicationData.getStatus());
            }
        } catch (IOException e) {
            applicationUI.showErrorDialog("Unable to get user", String.format("Unable to get user '%s' id '%s'!%n%s", username, userId, e.getMessage()));
            e.printStackTrace();
        }
    }
    
    public void cancelUserSelecton() {
        applicationUI.clearPane();
    }
}*/
