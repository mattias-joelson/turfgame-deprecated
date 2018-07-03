package org.joelson.mattias.turfgame.util;

public class JSONString implements JSONValue {

    private final String string;

    public JSONString(String string) {
        this.string = string;
    }

    @Override
    public String asJava() {
        return string;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JSONString) {
            return string.equals(((JSONString) obj).string);
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return string.hashCode();
    }
}
