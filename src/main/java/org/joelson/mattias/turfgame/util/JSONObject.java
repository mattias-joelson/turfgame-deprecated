package org.joelson.mattias.turfgame.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class JSONObject implements JSONValue {

    private final List<JSONPair> members;

    JSONObject(List<JSONPair> members) {
        this.members = new ArrayList<>(members.size());
        this.members.addAll(members);
    }

    public ListIterator<JSONPair> getMembers() {
        return Collections.unmodifiableList(members).listIterator();
    }

    public boolean containsName(String name) {
        return containsName(new JSONString(name));
    }

    public boolean containsName(JSONString name) {
        return getPair(name) != null;
    }

    public JSONValue getValue(String name) {
        return getValue(new JSONString(name));
    }

    public JSONValue getValue(JSONString name) {
        JSONPair pair = getPair(name);
        if (pair != null) {
            return (pair).getValue();
        }
        throw new NoSuchElementException("There exist no value pair " + name + '!');
    }

    private JSONPair getPair(JSONString name) {
        for (JSONPair member : members) {
            if ((member.getName()).equals(name)) {
                return member;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof JSONObject && hasEqualMembers((JSONObject) obj);
    }

    private boolean hasEqualMembers(JSONObject obj) {
        if (members.size() != obj.members.size()) {
            return false;
        }
        ListIterator<JSONPair> iterator = members.listIterator();
        ListIterator<JSONPair> objIterator = members.listIterator();
        while (iterator.hasNext()) {
            if (!iterator.next().equals(objIterator.next())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return members.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean addComma = false;
        for (JSONPair member : members) {
            if (addComma) {
                sb.append(',');
            }
            addComma = true;
            sb.append(member);
        }
        sb.append('}');
        return sb.toString();
    }
    
    public static JSONObject of(Map<String, Object> members) {
        return new JSONObject(members.entrySet().stream()
                .map(entry -> new JSONPair(new JSONString(entry.getKey()), valueOf(entry.getValue())))
                .collect(Collectors.toList()));
    }
    
    private static JSONValue valueOf(Object obj) {
        if (obj == null) {
            return JSONNull.NULL;
        }
        if (obj instanceof String) {
            return new JSONString((String) obj);
        }
        if (obj instanceof Number) {
            return new JSONNumber(String.valueOf(obj));
        }
        throw new IllegalArgumentException("Unknown type " + obj.getClass());
    }
}
