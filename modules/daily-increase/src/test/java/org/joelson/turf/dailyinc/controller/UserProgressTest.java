package org.joelson.turf.dailyinc.controller;

import org.joelson.turf.dailyinc.db.DatabaseEntityManagerTest;
import org.joelson.turf.dailyinc.db.UserProgressType;
import org.joelson.turf.dailyinc.model.RevisitData;
import org.joelson.turf.dailyinc.model.TakeData;
import org.joelson.turf.dailyinc.model.UserData;
import org.joelson.turf.dailyinc.model.UserProgressData;
import org.joelson.turf.dailyinc.model.ZoneData;
import org.joelson.turf.dailyinc.util.InstantUtil;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserProgressTest extends DatabaseEntityManagerTest {

    @Test
    public void testTakeUserProgress() {
        ZoneData zone = new ZoneData(1, "Zone");
        UserData user = new UserData(1, "User");
        UserData assister = new UserData(2, "Assister");
        Instant takeTime = InstantUtil.getInstantNowTruncatedToSecond();
        Instant takeDate = takeTime.truncatedTo(ChronoUnit.DAYS);
        Instant revisitTime = InstantUtil.addDays(takeTime, 1);
        Instant revisitDate = revisitTime.truncatedTo(ChronoUnit.DAYS);

        ProgressUpdater progressUpdater = new ProgressUpdater(getEntityManager());
        progressUpdater.updateWithVisits(getEntityManager().addTake(new TakeData(zone, user, takeTime), List.of(assister)));
        assertEquals(List.of(new UserProgressData(user, UserProgressType.DAILY_INCREASE, takeDate, 0, 1, takeTime),
                new UserProgressData(user, UserProgressType.DAILY_ADD, takeDate, 0, 1, takeTime),
                new UserProgressData(user, UserProgressType.DAILY_FIBONACCI, takeDate, 0, 1, takeTime),
                new UserProgressData(user, UserProgressType.DAILY_POWER_OF_TWO, takeDate, 0, 1, takeTime),
                new UserProgressData(assister, UserProgressType.DAILY_INCREASE, takeDate, 0, 1, takeTime),
                new UserProgressData(assister, UserProgressType.DAILY_ADD, takeDate, 0, 1, takeTime),
                new UserProgressData(assister, UserProgressType.DAILY_FIBONACCI, takeDate, 0, 1, takeTime),
                new UserProgressData(assister, UserProgressType.DAILY_POWER_OF_TWO, takeDate, 0, 1, takeTime)),
                getEntityManager().getUserProgress());

        progressUpdater.updateWithVisits(getEntityManager().addRevisit(new RevisitData(zone, user, revisitTime), List.of()));
        assertEquals(List.of(new UserProgressData(user, UserProgressType.DAILY_INCREASE, takeDate, 0, 1, takeTime),
                        new UserProgressData(user, UserProgressType.DAILY_INCREASE, revisitDate, 1, 1, revisitTime),
                        new UserProgressData(user, UserProgressType.DAILY_ADD, takeDate, 0, 1, takeTime),
                        new UserProgressData(user, UserProgressType.DAILY_ADD, revisitDate, 1, 1, revisitTime),
                        new UserProgressData(user, UserProgressType.DAILY_FIBONACCI, takeDate, 0, 1, takeTime),
                        new UserProgressData(user, UserProgressType.DAILY_FIBONACCI, revisitDate, 1, 2, revisitTime),
                        new UserProgressData(user, UserProgressType.DAILY_POWER_OF_TWO, takeDate, 0, 1, takeTime),
                        new UserProgressData(user, UserProgressType.DAILY_POWER_OF_TWO, revisitDate, 1, 1, revisitTime),
                        new UserProgressData(assister, UserProgressType.DAILY_INCREASE, takeDate, 0, 1, takeTime),
                        new UserProgressData(assister, UserProgressType.DAILY_ADD, takeDate, 0, 1, takeTime),
                        new UserProgressData(assister, UserProgressType.DAILY_FIBONACCI, takeDate, 0, 1, takeTime),
                        new UserProgressData(assister, UserProgressType.DAILY_POWER_OF_TWO, takeDate, 0, 1, takeTime)),
                getEntityManager().getUserProgress());

        assertEquals(8, getEntityManager().getUserProgress(user).size());
        assertEquals(4, getEntityManager().getUserProgress(assister).size());
        assertEquals(3, getEntityManager().getUserProgress(UserProgressType.DAILY_ADD).size());
        assertEquals(3, getEntityManager().getUserProgress(UserProgressType.DAILY_INCREASE).size());
        assertEquals(3, getEntityManager().getUserProgress(UserProgressType.DAILY_FIBONACCI).size());
        assertEquals(3, getEntityManager().getUserProgress(UserProgressType.DAILY_POWER_OF_TWO).size());
        assertEquals(8, getEntityManager().getUserProgress(takeDate).size());
        assertEquals(4, getEntityManager().getUserProgress(revisitDate).size());
    }

    @Test
    public void testDailyFive() {
        ZoneData[] zones = new ZoneData[] { new ZoneData(1, "Zone1"), new ZoneData(2, "Zone2"),
                new ZoneData(3, "Zone3"),new ZoneData(4, "Zone4"),new ZoneData(5, "Zone5")};
        UserData user = new UserData(1, "User");
        Instant time = InstantUtil.toInstant("2024-06-23T13:40:23");

        ProgressUpdater progressUpdater = new ProgressUpdater(getEntityManager());

        Instant takeTime = time;
        Instant takeDate = takeTime.truncatedTo(ChronoUnit.DAYS);
        for (ZoneData zone : zones) {
            progressUpdater.updateWithVisits(getEntityManager().addTake(new TakeData(zone, user, takeTime), List.of()));
            takeTime = InstantUtil.addMinutes(takeTime, 3);
        }
        assertEquals(List.of(new UserProgressData(user, UserProgressType.DAILY_INCREASE, takeDate, 0, 1, time),
                        new UserProgressData(user, UserProgressType.DAILY_ADD, takeDate, 0, 1, time),
                        new UserProgressData(user, UserProgressType.DAILY_FIBONACCI, takeDate, 0, 1, time),
                        new UserProgressData(user, UserProgressType.DAILY_POWER_OF_TWO, takeDate, 0, 1, time)),
                getEntityManager().getUserProgress());


        time = InstantUtil.addDays(time, 1);
        takeTime = time;
        takeDate = takeTime.truncatedTo(ChronoUnit.DAYS);
        for (ZoneData zone : zones) {
            progressUpdater.updateWithVisits(getEntityManager().addTake(new TakeData(zone, user, takeTime), List.of()));
            takeTime = InstantUtil.addMinutes(takeTime, 3);
        }
        assertEquals(List.of(new UserProgressData(user, UserProgressType.DAILY_INCREASE, takeDate, 1, 2,
                                InstantUtil.addMinutes(time, 3)),
                        new UserProgressData(user, UserProgressType.DAILY_ADD, takeDate, 1, 2,
                                InstantUtil.addMinutes(time, 6)),
                        new UserProgressData(user, UserProgressType.DAILY_FIBONACCI, takeDate, 1, 2,
                                InstantUtil.addMinutes(time, 0)),
                        new UserProgressData(user, UserProgressType.DAILY_POWER_OF_TWO, takeDate, 1, 2,
                                InstantUtil.addMinutes(time, 3))),
                getEntityManager().getUserProgress(takeDate));

        time = InstantUtil.addDays(time, 1);
        takeTime = time;
        takeDate = takeTime.truncatedTo(ChronoUnit.DAYS);
        for (ZoneData zone : zones) {
            progressUpdater.updateWithVisits(getEntityManager().addTake(new TakeData(zone, user, takeTime), List.of()));
            takeTime = InstantUtil.addMinutes(takeTime, 3);
        }
        assertEquals(List.of(new UserProgressData(user, UserProgressType.DAILY_INCREASE, takeDate, 2, 3,
                                InstantUtil.addMinutes(time, 6)),
                        new UserProgressData(user, UserProgressType.DAILY_ADD, takeDate, 2, 2,
                                InstantUtil.addMinutes(time, 6)),
                        new UserProgressData(user, UserProgressType.DAILY_FIBONACCI, takeDate, 2, 3,
                                InstantUtil.addMinutes(time, 3)),
                        new UserProgressData(user, UserProgressType.DAILY_POWER_OF_TWO, takeDate, 2, 3,
                                InstantUtil.addMinutes(time, 9))),
                getEntityManager().getUserProgress(takeDate));

        time = InstantUtil.addDays(time, 1);
        takeTime = time;
        takeDate = takeTime.truncatedTo(ChronoUnit.DAYS);
        for (ZoneData zone : zones) {
            progressUpdater.updateWithVisits(getEntityManager().addTake(new TakeData(zone, user, takeTime), List.of()));
            takeTime = InstantUtil.addMinutes(takeTime, 3);
        }
        assertEquals(List.of(new UserProgressData(user, UserProgressType.DAILY_INCREASE, takeDate, 3, 4,
                                InstantUtil.addMinutes(time, 9)),
                        new UserProgressData(user, UserProgressType.DAILY_ADD, takeDate, 2, 2,
                                InstantUtil.addMinutes(time, 6)),
                        new UserProgressData(user, UserProgressType.DAILY_FIBONACCI, takeDate, 3, 4,
                                InstantUtil.addMinutes(time, 6)),
                        new UserProgressData(user, UserProgressType.DAILY_POWER_OF_TWO, takeDate, 3, 3,
                                InstantUtil.addMinutes(time, 9))),
                getEntityManager().getUserProgress(takeDate));

        time = InstantUtil.addDays(time, 1);
        takeTime = time;
        takeDate = takeTime.truncatedTo(ChronoUnit.DAYS);
        for (ZoneData zone : zones) {
            progressUpdater.updateWithVisits(getEntityManager().addTake(new TakeData(zone, user, takeTime), List.of()));
            takeTime = InstantUtil.addMinutes(takeTime, 3);
        }
        assertEquals(List.of(new UserProgressData(user, UserProgressType.DAILY_INCREASE, takeDate, 4, 5,
                                InstantUtil.addMinutes(time, 12)),
                        new UserProgressData(user, UserProgressType.DAILY_ADD, takeDate, 2, 2,
                                InstantUtil.addMinutes(time, 6)),
                        new UserProgressData(user, UserProgressType.DAILY_FIBONACCI, takeDate, 4, 5,
                                InstantUtil.addMinutes(time, 12)),
                        new UserProgressData(user, UserProgressType.DAILY_POWER_OF_TWO, takeDate, 3, 3,
                                InstantUtil.addMinutes(time, 9))),
                getEntityManager().getUserProgress(takeDate));
    }
}
