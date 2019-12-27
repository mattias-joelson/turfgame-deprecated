package org.joelson.mattias.turfgame.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JSONArray implements JSONValue {

    private final List<JSONValue> elements;

    public JSONArray(List<JSONValue> elements) {
        this.elements = new ArrayList<>(elements);
    }

    public Iterable<JSONValue> getElements() {
        return Collections.unmodifiableList(elements);
    }
    
    public int size() {
        return elements.size();
    }
    
    public Stream<JSONValue> stream() {
        return elements.stream();
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
        return elements.stream()
                .map(Object::toString)
                .collect(Collectors.joining(",", "[", "]"));
    }
    
    public static JSONArray of(JSONValue... elements) {
        return new JSONArray(Arrays.asList(elements));
    }
    
    public static JSONArray parseArray(String s) throws ParseException {
        return (JSONArray) JSONValue.parse(s);
    }
}
