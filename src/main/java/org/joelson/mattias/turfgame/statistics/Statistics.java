package org.joelson.mattias.turfgame.statistics;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Statistics implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Set<Country> countries;
    private final Set<Region> regions;
    private final Set<Municipality> municipalities;
    private final Set<Zone> zones;
    private final Set<Round> rounds;
    private final Set<User> users;
    private final Set<Visits> visits;

    public Statistics() {
        countries = new HashSet<>();
        regions = new HashSet<>();
        municipalities = new HashSet<>();
        zones = new HashSet<>();
        rounds = new HashSet<>();
        users = new HashSet<>();
        visits = new HashSet<>();
    }

    public boolean addCountry(Country country) {
        return countries.add(Objects.requireNonNull(country));
    }

    public boolean addRegion(Region region) {
        return regions.add(Objects.requireNonNull(region));
    }

    public boolean addMunicipality(Municipality municipality) {
        return municipalities.add(Objects.requireNonNull(municipality));
    }

    public boolean addRound(Round round) {
        return rounds.add(Objects.requireNonNull(round));
    }

    public boolean addUser(User user) {
        return users.add(Objects.requireNonNull(user));
    }

    @Override
    public String toString() {
        return "Statistics{countries:" + countries + ",regions:" + regions + ",municipalities:" + municipalities
                + ",zones:" + zones + ",rounds:" + rounds + ",users:" + users + ",visits:" + visits + '}';
    }
}
