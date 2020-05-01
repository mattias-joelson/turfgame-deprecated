package org.joelson.mattias.turfgame.application.db;

import com.sun.istack.NotNull;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "municipality_zones", //NON-NLS
        uniqueConstraints = @UniqueConstraint(columnNames = { "municipality_id", "zone_id" })) //NON-NLS )
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

    public MunicipalityEntity getMunicipality() {
        return municipality;
    }

    public void setMunicipality(MunicipalityEntity municipality) {
        this.municipality = Objects.requireNonNull(municipality, "Municipality can not be null"); //NON-NLS
    }

    public ZoneEntity getZone() {
        return zone;
    }

    public void setZone(ZoneEntity zone) {
        this.zone = Objects.requireNonNull(zone, "Zone can not be null!"); //NON-NLS
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof MunicipalityZoneEntity) {
            MunicipalityZoneEntity that = (MunicipalityZoneEntity) obj;
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
        return String.format("MunicipalityZoneEntity[municipality %s, zone %s]", //NON-NLS
                EntityUtil.toStringPart(municipality), EntityUtil.toStringPart(zone));
    }

    static MunicipalityZoneEntity build(@NotNull MunicipalityEntity municipality, @NotNull ZoneEntity zone) {
        MunicipalityZoneEntity municipalityZone = new MunicipalityZoneEntity();
        municipalityZone.setMunicipality(municipality);
        municipalityZone.setZone(zone);
        return municipalityZone;
    }
}
