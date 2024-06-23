package org.joelson.turf.dailyinc.model;

import org.joelson.turf.util.StringUtil;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class UserData implements Serializable {

    // 0beroff 80119

    @Serial
    private static final long serialVersionUID = 1L;

    private final int id;
    private final String name;

    public UserData(int id, String name) {
        this.id = id;
        this.name = StringUtil.requireNotNullAndNotEmpty(name, "Name can not be null", "Name can not be empty");
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof UserData that) {
            return id == that.id && Objects.equals(name, that.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("UserData[id=%d, name=%s]", id, name);
    }
}
