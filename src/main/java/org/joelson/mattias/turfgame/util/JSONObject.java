package org.joelson.mattias.turfgame.util;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

public class JSONObject implements JSONValue {

    private final List<JSONPair> members;

    public JSONObject(List<JSONPair> members) {
        this.members = members;
    }

    @Override
    public List<Entry<String, Object>> asJava() {
        List<Entry<String, Object>> members = new ArrayList<>();
        for (JSONPair member : this.members) {
            members.add(new AbstractMap.SimpleEntry<>(member.getString().asJava(), member.getValue().asJava()));
        }
        return members;
    }

    public ListIterator<JSONPair> getMembers() {
        return Collections.unmodifiableList(members).listIterator();
    }

    public boolean containsString(String s) {
        return containsString(new JSONString(s));
    }

    public boolean containsString(JSONString s) {
        for (JSONPair member : members) {
            if (member.getString().equals(s)) {
                return true;
            }
        }
        return false;
    }

    public JSONValue getValue(String s) {
        return getValue(new JSONString(s));
    }

    private JSONValue getValue(JSONString s) {
        for (JSONPair member : members) {
            if (member.getString().equals(s)) {
                return member.getValue();
            }
        }
        throw new NoSuchElementException("There exist no value pair " + s.asJava() + '!');
    }
}
