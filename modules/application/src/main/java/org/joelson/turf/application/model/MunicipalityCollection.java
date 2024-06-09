package org.joelson.turf.application.model;

import org.joelson.turf.application.db.DatabaseEntityManager;

import java.util.List;

public class MunicipalityCollection {

    private final DatabaseEntityManager dbEntity;

    public MunicipalityCollection(DatabaseEntityManager dbEntity) {
        this.dbEntity = dbEntity;
    }

    public MunicipalityData getMunicipality(String name) {
        return dbEntity.getMunicipality(name);
    }

    public List<MunicipalityData> getMunicipalities() {
        return dbEntity.getMunicipalities();
    }

    public void updateMunicipality(MunicipalityData municipality) {
        dbEntity.updateMunicipality(municipality);
    }
}
