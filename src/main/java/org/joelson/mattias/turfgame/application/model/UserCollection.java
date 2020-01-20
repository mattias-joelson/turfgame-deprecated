package org.joelson.mattias.turfgame.application.model;

import org.joelson.mattias.turfgame.apiv4.User;
import org.joelson.mattias.turfgame.apiv4.Users;
import org.joelson.mattias.turfgame.application.db.DatabaseEntityManager;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserCollection {
    
    private final DatabaseEntityManager dbEntityManager;
    
    public UserCollection(DatabaseEntityManager dbEntityManager) {
        this.dbEntityManager = dbEntityManager;
    }
    
    public List<UserData> getUsers() {
        return dbEntityManager.getUsers();
    }
    
    public void updateUsers(Iterable<String> usernames) throws IOException, ParseException {
        List<UserData> existingUsers = getUsers();
        Map<String, UserData> userMap = createUserMap(existingUsers);
        List<String> unknownUsernames = new ArrayList<>();
        for (String username : usernames) {
            if (!userMap.containsKey(username)) {
                unknownUsernames.add(username);
            }
        }
        if (unknownUsernames.size() == 0) {
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
    
    private Map<String, UserData> createUserMap(List<UserData> existingUsers) {
        return existingUsers.stream()
                .collect(Collectors.toMap(UserData::getName, Function.identity()));
    }

    private Map<Integer, UserData> createUserIdMap(List<UserData> existingUsers) {
        return existingUsers.stream()
                .collect(Collectors.toMap(UserData::getId, Function.identity()));
    }
}
