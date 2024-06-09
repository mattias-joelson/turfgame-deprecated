package org.joelson.turf.application.db;

import org.hibernate.dialect.H2Dialect;

public class DropConstraintsH2Dialect extends H2Dialect {

    @Override
    public boolean dropConstraints() {
        return true;
    }
}
