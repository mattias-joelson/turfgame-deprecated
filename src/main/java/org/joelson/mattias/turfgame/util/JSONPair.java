package org.joelson.mattias.turfgame.util;

public class JSONPair {

    private final JSONString string;
    private final JSONValue value;

    public JSONPair(JSONString string, JSONValue value) {
        this.string = string;
        this.value = value;
    }

    public JSONString getString() {
        return string;
    }

    public JSONValue getValue() {
        return value;
    }
}
