package org.joelson.mattias.turfgame.animate;

import org.joelson.mattias.turfgame.zundin.Today;
import org.joelson.mattias.turfgame.zundin.TodayZone;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.time.temporal.TemporalAccessor;
import java.util.SortedMap;
import java.util.TreeMap;

public class Animate {

    /**
     * 1. Läs in laget och nick från turfportalen.
     * 1. Samla in data - importera dag för dag för alla användare
     * samla in username<->id från turfgame
     * konvertera username till id för att hantera nickändringar
     * hitta vilka användare som ingår i de tre grupperna
     * sortera events som ingår
     * slå samman events
     * summera totalt
     * summera vecka
     * rendrera minut för minut
     * generera film - ffmpeg?
     */

    /**
     * neutrals +50
     */

    public static int WIDTH = 1920;
    public static int HEIGHT = 1080;
    public static int FPS = 60;

    private static class ScoreUpdater {

    }

    public static void main(String[] args) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        String html = Files.readString(Path.of("/Users/mattias.joelson/src/turfgame-statistics/src/test/resources/0beroff_2020-09-25.html"));
        Today today = Today.fromHTML("0beroff", "2020-09-25", html);
        for (TodayZone zone : today.getZones()) {
            Instant instant = getInstant(zone.getDate());
            LocalDateTime stdt = LocalDateTime.ofInstant(instant, ZoneOffset.ofHours(2));
            System.out.println(stdt.format(formatter) + '\t' + zone.getActivity() + '\t' + zone.getZoneName());
        }
        SortedMap<Instant, ScoreUpdater> timeQueue = new TreeMap<>();
    }

    private static Instant getInstant(String date) {
        int space = date.indexOf(' ');
        LocalDate localDate = LocalDate.parse(date.substring(0, space), DateTimeFormatter.ISO_LOCAL_DATE);
        LocalTime localTime = LocalTime.parse(date.substring(space + 1), DateTimeFormatter.ISO_LOCAL_TIME);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneOffset.UTC);
        return zonedDateTime.toInstant();
    }
}
