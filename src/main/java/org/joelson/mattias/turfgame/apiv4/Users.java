package org.joelson.mattias.turfgame.apiv4;

import org.joelson.mattias.turfgame.util.JSONArray;
import org.joelson.mattias.turfgame.util.JSONObject;
import org.joelson.mattias.turfgame.util.URLReader;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class Users {
    
    private static final String USERS_REQUEST = "http://api.turfgame.com/v4/users"; //NON-NLS
    private static final String NAME_PARAMETER = "name"; //NON-NLS
    private static final String ID_PARAMETER = "id"; // NON-NLS
    
    private Users() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }
    
    public static List<User> getUsers(String name, Integer id) throws IOException, ParseException {
        Map<String, Object> params = new HashMap<>(2);
        if (name != null && !name.isEmpty()) {
            params.put(NAME_PARAMETER, name);
        }
        if (id != null && id > 0) {
            params.put(ID_PARAMETER, id);
        }
        JSONArray array = JSONArray.of(JSONObject.of(params));
        String json = String.valueOf(array);
        return fromJSON(URLReader.postRequest(USERS_REQUEST, json));
    }

    static List<User> fromJSON(String s) throws ParseException {
        return JSONArray.parseArray(s).stream()
                .map(JSONObject.class::cast)
                .map(User::fromJSON)
                .collect(Collectors.toList());
    }
}
