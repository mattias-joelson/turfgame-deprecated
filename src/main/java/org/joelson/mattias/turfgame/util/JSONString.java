package org.joelson.mattias.turfgame.util;

public class JSONString implements JSONValue {

    private final String str;

    JSONString(String str) {
        this.str = str;
    }

    public String stringValue() {
        return str;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JSONString) {
            return str.equals(((JSONString) obj).stringValue());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return str.hashCode();
    }

    @Override
    public String toString() {
        return '"' + str + '"';
    }
}
