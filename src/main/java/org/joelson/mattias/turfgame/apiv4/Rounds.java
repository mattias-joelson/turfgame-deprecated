package org.joelson.mattias.turfgame.apiv4;

import org.joelson.mattias.turfgame.util.JSONArray;
import org.joelson.mattias.turfgame.util.JSONObject;
import org.joelson.mattias.turfgame.util.URLReader;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

public final class Rounds {

    private static final String ROUNDS_REQUEST = "http://api.turfgame.com/v4/rounds"; // NON-NLS

    private Rounds() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }

    public static List<Round> readRounds() throws IOException, ParseException {
        return fromJSON(URLReader.getRequest(ROUNDS_REQUEST));
    }

    static List<Round> fromJSON(String s) throws ParseException {
        return JSONArray.parseArray(s).stream()
                .map(JSONObject.class::cast)
                .map(Round::fromJSON)
                .collect(Collectors.toList());
    }
}
