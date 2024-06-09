package org.joelson.turf.application.db;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "municipality_zones",
        uniqueConstraints = @UniqueConstraint(columnNames = { "municipality_id", "zone_id" }))
public class MunicipalityZoneEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne(optional = false)
    MunicipalityEntity municipality;

    @Id
    @ManyToOne(optional = false)
    ZoneEntity zone;

    public MunicipalityZoneEntity() {
    }

    static MunicipalityZoneEntity build(@NotNull MunicipalityEntity municipality, @NotNull ZoneEntity zone) {
        MunicipalityZoneEntity municipalityZone = new MunicipalityZoneEntity();
        municipalityZone.setMunicipality(municipality);
        municipalityZone.setZone(zone);
        return municipalityZone;
    }

    public MunicipalityEntity getMunicipality() {
        return municipality;
    }

    public void setMunicipality(MunicipalityEntity municipality) {
        this.municipality = Objects.requireNonNull(municipality, "Municipality can not be null");
    }

    public ZoneEntity getZone() {
        return zone;
    }

    public void setZone(ZoneEntity zone) {
        this.zone = Objects.requireNonNull(zone, "Zone can not be null!");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof MunicipalityZoneEntity that) {
            return municipality.equals(that.municipality) && zone.equals(that.zone);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(municipality, zone);
    }

    @Override
    public String toString() {
        return String.format("MunicipalityZoneEntity[municipality %s, zone %s]",
                EntityUtil.toStringPart(municipality), EntityUtil.toStringPart(zone));
    }
}
