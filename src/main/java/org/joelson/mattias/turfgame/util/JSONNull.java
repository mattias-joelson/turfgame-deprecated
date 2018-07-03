package org.joelson.mattias.turfgame.util;

public class JSONNull implements JSONValue {

    @Override
    public boolean equals(Object obj) {
        return obj instanceof JSONNull;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return "null";
    }
}
