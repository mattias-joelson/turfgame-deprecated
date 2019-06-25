package org.joelson.mattias.turfgame.util;

class JSONPair {

    private final JSONString name;
    private final JSONValue value;

    JSONPair(JSONString name, JSONValue value) {
        this.name = name;
        this.value = value;
    }

    JSONString getName() {
        return name;
    }

    JSONValue getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JSONPair) {
            return name.equals(((JSONPair) obj).name) && value.equals(((JSONPair) obj).value);
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return name.hashCode() * 31 + value.hashCode();
    }

    @Override
    public String toString() {
        return name + ":" + value;
    }
}
