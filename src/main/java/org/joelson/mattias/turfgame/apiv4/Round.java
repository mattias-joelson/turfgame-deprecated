package org.joelson.mattias.turfgame.apiv4;

import org.joelson.mattias.turfgame.util.JSONObject;
import org.joelson.mattias.turfgame.util.JSONString;

public class Round {

    private static final String NAME = "name";
    private static final String START = "start";

    private final String name;
    private final String start;

    private Round(String name, String start) {
        this.name = name;
        this.start = start;
    }

    public String getName() {
        return name;
    }

    public String getStart() {
        return start;
    }

    static Round fromJSON(JSONObject obj) {
        JSONString name = (JSONString) obj.getValue(NAME);
        JSONString start = (JSONString) obj.getValue(START);
        return new Round(name.stringValue(), start.stringValue());
    }

    @Override
    public String toString() {
        return "Round{" +
                "name='" + name + '\'' +
                ", start='" + start + '\'' +
                '}';
    }
}
