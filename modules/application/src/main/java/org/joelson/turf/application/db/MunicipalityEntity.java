package org.joelson.mattias.turfgame.application.db;

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
import org.joelson.mattias.turfgame.application.model.MunicipalityData;
import org.joelson.mattias.turfgame.application.model.ZoneData;
import org.joelson.mattias.turfgame.util.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "municipalities", //NON-NLS
        uniqueConstraints = @UniqueConstraint(columnNames = { "region_id", "name" })) //NON-NLS
public class MunicipalityEntity implements Serializable {

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

    @OneToMany(cascade= CascadeType.ALL)
    @JoinColumn(name = "municipality_id") //NON-NLS
    private ArrayList<MunicipalityZoneEntity> municipalityZones;


    public MunicipalityEntity() {
    }

    public int getId() {
        return id;
    }

    public RegionEntity getRegion() {
        return region;
    }

    public void setRegion(RegionEntity region) {
        this.region = Objects.requireNonNull(region, "Region can not be null"); //NON-NLS
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = StringUtil.requireNotNullAndNotEmpty(name, "Name can not be null", "Name can not be empty"); //NON-NLS
    }

    public List<MunicipalityZoneEntity> getMunicipalityZones() {
        return municipalityZones;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof MunicipalityEntity) {
            MunicipalityEntity that = (MunicipalityEntity) obj;
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
        return String.format("MunicipalityEntity[region %s, name %s, municipalityZones %s]", //NON-NLS
                EntityUtil.toStringPart(region), name, municipalityZones);
    }

    public MunicipalityData toData() {
        List<ZoneData> zones = municipalityZones.stream()
                .map(MunicipalityZoneEntity::getZone)
                .map(ZoneEntity::toData)
                .collect(Collectors.toList());
        return new MunicipalityData(region.toData(), name, zones);
    }

    public static MunicipalityEntity build(RegionEntity region, String name) {
        MunicipalityEntity municipality = new MunicipalityEntity();
        municipality.setRegion(region);
        municipality.setName(name);
        municipality.municipalityZones = new ArrayList<>();
        return municipality;
    }
}
