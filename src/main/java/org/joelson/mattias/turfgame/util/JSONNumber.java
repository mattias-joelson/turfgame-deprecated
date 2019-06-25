package org.joelson.mattias.turfgame.util;

public class JSONNumber implements JSONValue {

    private final String str;

    JSONNumber(String str) {
        this.str = str;
    }

    public int intValue() {
        return Integer.parseInt(str);
    }

    public double doubleValue() {
        return Double.parseDouble(str);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof JSONNumber && str.equals(((JSONNumber) obj).str);
    }

    @Override
    public int hashCode() {
        return str.hashCode();
    }

    @Override
    public String toString() {
        return str;
    }
}
