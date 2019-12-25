package org.joelson.mattias.turfgame.apiv4;

import org.joelson.mattias.turfgame.util.JSONArray;
import org.joelson.mattias.turfgame.util.JSONObject;
import org.joelson.mattias.turfgame.util.JSONParser;
import org.joelson.mattias.turfgame.util.JSONValue;
import org.joelson.mattias.turfgame.util.URLReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class Rounds {

    private static final String ROUNDS_REQUEST = "http://api.turfgame.com/v4/rounds";

    private Rounds() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }

    public static List<Round> readRounds() throws IOException {
        return fromJSON(URLReader.getRequest(ROUNDS_REQUEST));
    }

    static List<Round> fromJSON(String s) {
        JSONArray valueArray = (JSONArray) new JSONParser().parse(s);
        List<Round> regions = new ArrayList<>();
        for (JSONValue value : valueArray.getElements()) {
            regions.add(Round.fromJSON((JSONObject) value));
        }
        return regions;
    }
}
