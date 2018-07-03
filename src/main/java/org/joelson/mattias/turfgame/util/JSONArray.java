package org.joelson.mattias.turfgame.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JSONArray implements JSONValue {

    private final List<JSONValue> elements;

    JSONArray(List<JSONValue> elements) {
        this.elements = new ArrayList<>(elements.size());
        this.elements.addAll(elements);
    }

    public Iterable<JSONValue> getElements() {
        return Collections.unmodifiableList(elements);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof JSONArray && elements.equals(((JSONArray) obj).elements);
    }

    @Override
    public int hashCode() {
        return elements.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean addComma = false;
        for (JSONValue value : elements) {
            if (addComma) {
                sb.append(',');
            }
            addComma = true;
            sb.append(value);
        }
        sb.append(']');
        return sb.toString();
    }
}
