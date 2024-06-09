package org.joelson.turf.application.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.joelson.turf.application.model.RegionHistoryData;
import org.joelson.turf.util.StringUtil;

import javax.annotation.Nullable;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "region_history")
public class RegionHistoryEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @ManyToOne(optional = false)
    private RegionEntity region;

    @NotNull
    @Id
    @Column(name = "from_timestamp", nullable = false)
    private Instant from;

    @NotNull
    @Column(nullable = false)
    private String name;

    @Nullable
    private String country;

    public RegionHistoryEntity() {
    }

    static RegionHistoryEntity build(
            @NotNull RegionEntity region, @NotNull Instant from, @NotNull String name, @Nullable String country) {
        RegionHistoryEntity regionHistory = new RegionHistoryEntity();
        regionHistory.setRegion(region);
        regionHistory.setFrom(from);
        regionHistory.setName(name);
        regionHistory.setCountry(country);
        return regionHistory;
    }

    @NotNull
    public RegionEntity getRegion() {
        return region;
    }

    public void setRegion(@NotNull RegionEntity region) {
        this.region = Objects.requireNonNull(region, "Region can not be null");
    }

    @NotNull
    public Instant getFrom() {
        return from;
    }

    public void setFrom(@NotNull Instant from) {
        this.from = Objects.requireNonNull(from, "From can not be null");
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = StringUtil.requireNotNullAndNotEmpty(name, "Name can not be null", "Name can not be empty");
    }

    @Nullable
    public String getCountry() {
        return country;
    }

    public void setCountry(@Nullable String country) {
        this.country = StringUtil.requireNullOrNonEmpty(country, "Country can not be empty");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof RegionHistoryEntity that) {
            return Objects.equals(region, that.region) && Objects.equals(from, that.from) && Objects.equals(name,
                    that.name) && Objects.equals(country, that.country);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, from);
    }

    @Override
    public String toString() {
        return String.format("RegionHistoryEntity[region %s, from %s, name %s, country %s]",
                EntityUtil.toStringPart(region), from, name, country);
    }

    public RegionHistoryData toData() {
        return new RegionHistoryData(region.getId(), from, name, country);
    }
}
