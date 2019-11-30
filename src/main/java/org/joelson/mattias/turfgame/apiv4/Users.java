package org.joelson.mattias.turfgame.apiv4;

import org.joelson.mattias.turfgame.util.JSONArray;
import org.joelson.mattias.turfgame.util.JSONObject;
import org.joelson.mattias.turfgame.util.JSONParser;
import org.joelson.mattias.turfgame.util.JSONValue;
import org.joelson.mattias.turfgame.util.URLReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Users {
    
    private static final String USERS_REQUEST = "http://api.turfgame.com/v4/users";
    
    private Users() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }
    
    public static List<User> getUsers(String name, Integer id) throws IOException {
        Map<String, Object> params = new HashMap<>();
        if (name != null && !name.isEmpty()) {
            params.put("name", name);
        }
        if (id != null && id.intValue() > 0) {
            params.put("id", id);
        }
        JSONArray array = new JSONArray(List.of(JSONObject.of(params)));
        String json = array.toString();
        return fromHTML(URLReader.postRequest(USERS_REQUEST, json));
    }

    static List<User> fromHTML(String s) {
        JSONArray valueArray = (JSONArray) new JSONParser().parse(s);
        List<User> users = new ArrayList<>();
        for (JSONValue value : valueArray.getElements()) {
            users.add(User.fromJSON((JSONObject) value));
        }
        return users;
    }
}
