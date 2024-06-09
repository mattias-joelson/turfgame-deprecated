package org.joelson.turf.application.model;

import org.joelson.turf.application.db.DatabaseEntityManager;
import org.joelson.turf.turfgame.apiv4.User;
import org.joelson.turf.turfgame.apiv4.Users;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserCollection {

    private final DatabaseEntityManager dbEntityManager;

    private UserData selectedUser;

    public UserCollection(DatabaseEntityManager dbEntityManager) {
        this.dbEntityManager = dbEntityManager;
    }

    private static Map<String, UserData> createUserMap(List<UserData> existingUsers) {
        return existingUsers.stream().collect(Collectors.toMap(UserData::getName, Function.identity()));
    }

    private static Map<Integer, UserData> createUserIdMap(List<UserData> existingUsers) {
        return existingUsers.stream().collect(Collectors.toMap(UserData::getId, Function.identity()));
    }

    public UserData getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(UserData selectedUser) {
        this.selectedUser = selectedUser;
    }

    public UserData getUser(int userId) {
        return dbEntityManager.getUser(userId);
    }

    public List<UserData> getUsers() {
        return dbEntityManager.getUsers();
    }

    public void updateUsers(Iterable<String> usernames) throws IOException {
        List<UserData> existingUsers = getUsers();
        Map<String, UserData> userMap = createUserMap(existingUsers);
        List<String> unknownUsernames = new ArrayList<>();
        for (String username : usernames) {
            if (!userMap.containsKey(username)) {
                unknownUsernames.add(username);
            }
        }
        if (unknownUsernames.isEmpty()) {
            return;
        }
        List<User> lookedupUsers = Users.getUsers((Object[]) unknownUsernames.toArray(String[]::new));
        Map<Integer, UserData> userIdMap = createUserIdMap(existingUsers);
        List<UserData> updatedUsers = new ArrayList<>();
        List<UserData> newUsers = new ArrayList<>();
        for (User user : lookedupUsers) {
            UserData userData = new UserData(user.getId(), user.getName());
            if (userIdMap.containsKey(user.getId())) {
                updatedUsers.add(userData);
            } else {
                newUsers.add(userData);
            }
        }
        dbEntityManager.updateUsers(newUsers, updatedUsers);
    }
}
