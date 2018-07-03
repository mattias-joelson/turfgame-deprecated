package org.joelson.mattias.turfgame.util;

public class JSONNumber implements JSONValue {

    private final Number number;

    public JSONNumber(Number number) {
        this.number = number;
    }

    @Override
    public Number asJava() {
        return number;
    }
}
