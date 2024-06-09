package org.joelson.turf.idioten.model;

import org.joelson.turf.idioten.db.IdiotVisitType;

import java.time.Instant;
import java.util.Objects;

public class VisitData {

    private final ZoneData zone;
    private final PlayerData player;
    private final Instant when;
    private final IdiotVisitType type;
    private final PlayerData assistedPlayer;

    public VisitData(ZoneData zone, PlayerData player, Instant when, IdiotVisitType type, PlayerData assistedPlayer) {
        this.zone = zone;
        this.player = player;
        this.when = when;
        this.type = type;
        this.assistedPlayer = assistedPlayer;
    }

    public ZoneData getZone() {
        return zone;
    }

    public PlayerData getPlayer() {
        return player;
    }

    public Instant getWhen() {
        return when;
    }

    public IdiotVisitType getType() {
        return type;
    }

    public PlayerData getAssistedPlayer() {
        return assistedPlayer;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof VisitData that) {
            return Objects.equals(zone, that.zone) && Objects.equals(player, that.player)
                    && Objects.equals(when, that.when) && Objects.equals(type, that.type)
                    && Objects.equals(assistedPlayer, that.assistedPlayer);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(zone, player, when);
    }

    @Override
    public String toString() {
        return String.format("TakeData[zone %s, player %s, when %s, type %s, assistedPlayer %s]",
                ModelUtil.toStringPart(zone), ModelUtil.toStringPart(player), when, type,
                ModelUtil.toStringPart(assistedPlayer));
    }
}
