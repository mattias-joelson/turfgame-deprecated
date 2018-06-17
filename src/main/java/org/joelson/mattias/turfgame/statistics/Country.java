package org.joelson.mattias.turfgame.statistics;

import java.io.Serializable;

public class Country implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String id;
    private final String name;

    public Country(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Country) {
            Country country = (Country) obj;
            return id.equals(country.id) && name.equals(country.name);
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "Country{id:'" + id + "',name:'" + name + "'}";
    }
}
