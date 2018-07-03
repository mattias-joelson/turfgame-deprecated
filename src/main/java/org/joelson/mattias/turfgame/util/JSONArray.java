package org.joelson.mattias.turfgame.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class JSONArray implements JSONValue {

    private final List<JSONValue> elements;

    public JSONArray(List<JSONValue> elements) {
        this.elements = elements;
    }

    @Override
    public List<Object> asJava() {
        List<Object> objects = new ArrayList<>(elements.size());
        for (JSONValue element : elements) {
            objects.add(element.asJava());
        }
        return objects;
    }

    public Iterable<JSONValue> getElements() {
        return Collections.unmodifiableList(elements);
    }
}
