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

    public Municipality getMunicipality(String name) {
        for (Municipality municipality : municipalities) {
            if (municipality.getName().equals(name)) {
                return municipality;
            }
        }
        return null;
    }

    public boolean addZone(Zone zone) {
        return zones.add(Objects.requireNonNull(zone));
    }

    public Zone getZone(String name) {
        for (Zone zone : zones) {
            if (zone.getName().equals(name)) {
                return zone;
            }
        }
        return null;
    }

    public boolean addRound(Round round) {
        return rounds.add(Objects.requireNonNull(round));
    }

    public Round getRound(int id) {
        for (Round round : rounds) {
            if (round.getId() == id) {
                return round;
            }
        }
        return null;
    }

    public boolean addUser(User user) {
        return users.add(Objects.requireNonNull(user));
    }

    public User getUser(String name) {
        for (User user : users) {
            if (user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    public boolean addVisits(Visits visits) {
        if (this.visits.stream()
                .anyMatch(v -> v.getZone().equals(visits.getZone())
                        && v.getUser().equals(visits.getUser())
                        && v.getRound().equals(visits.getRound()))) {
            return false;
        }
        return this.visits.add(visits);
    }

    @Override
    public String toString() {
        return "Statistics{countries:" + countries + ",regions:" + regions + ",municipalities:" + municipalities
                + ",zones:" + zones + ",rounds:" + rounds + ",users:" + users + ",visits:" + visits + '}';
    }
}
