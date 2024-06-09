package org.joelson.turf.application.db;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import org.joelson.turf.application.model.MunicipalityData;
import org.joelson.turf.application.model.ZoneData;
import org.joelson.turf.util.StringUtil;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "municipalities", uniqueConstraints = @UniqueConstraint(columnNames = { "region_id", "name" }))
public class MunicipalityEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private int id;

    @ManyToOne(optional = false)
    private RegionEntity region;

    @NotNull
    @Column(nullable = false)
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "municipality_id")
    private ArrayList<MunicipalityZoneEntity> municipalityZones;


    public MunicipalityEntity() {
    }

    public static MunicipalityEntity build(RegionEntity region, String name) {
        MunicipalityEntity municipality = new MunicipalityEntity();
        municipality.setRegion(region);
        municipality.setName(name);
        municipality.municipalityZones = new ArrayList<>();
        return municipality;
    }

    public int getId() {
        return id;
    }

    public RegionEntity getRegion() {
        return region;
    }

    public void setRegion(RegionEntity region) {
        this.region = Objects.requireNonNull(region, "Region can not be null");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = StringUtil.requireNotNullAndNotEmpty(name, "Name can not be null", "Name can not be empty");
    }

    public List<MunicipalityZoneEntity> getMunicipalityZones() {
        return municipalityZones;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof MunicipalityEntity that) {
            return region.equals(that.region) && name.equals(that.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, name);
    }

    @Override
    public String toString() {
        return String.format("MunicipalityEntity[region %s, name %s, municipalityZones %s]",
                EntityUtil.toStringPart(region), name, municipalityZones);
    }

    public MunicipalityData toData() {
        List<ZoneData> zones = municipalityZones.stream().map(MunicipalityZoneEntity::getZone).map(ZoneEntity::toData)
                .toList();
        return new MunicipalityData(region.toData(), name, zones);
    }
}
