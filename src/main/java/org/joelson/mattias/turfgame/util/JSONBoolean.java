package org.joelson.mattias.turfgame.util;

public class JSONBoolean implements JSONValue {

    private final boolean value;

    JSONBoolean(boolean value) {
        this.value = value;
    }

    public boolean booleanValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof JSONBoolean && booleanValue() == ((JSONBoolean) obj).booleanValue();
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(booleanValue());
    }

    @Override
    public String toString() {
        return Boolean.toString(booleanValue());
    }
}
