package org.joelson.turf.dailyinc.controller;

import org.joelson.turf.dailyinc.db.UserProgressType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserProgressTypeTest {

    @Test
    public void testDailyIncrease() {
        assertEquals(1, UserProgressType.DAILY_INCREASE.getNeededVisits(1));
        assertEquals(2, UserProgressType.DAILY_INCREASE.getNeededVisits(2));
        assertEquals(3, UserProgressType.DAILY_INCREASE.getNeededVisits(3));
        assertEquals(4, UserProgressType.DAILY_INCREASE.getNeededVisits(4));
        assertEquals(5, UserProgressType.DAILY_INCREASE.getNeededVisits(5));
        assertEquals(6, UserProgressType.DAILY_INCREASE.getNeededVisits(6));
        assertEquals(7, UserProgressType.DAILY_INCREASE.getNeededVisits(7));
        assertEquals(500, UserProgressType.DAILY_INCREASE.getNeededVisits(500));
    }

    @Test
    public void testDailyAdd() {
        assertEquals(1, UserProgressType.DAILY_ADD.getNeededVisits(1));
        assertEquals(3, UserProgressType.DAILY_ADD.getNeededVisits(2));
        assertEquals(6, UserProgressType.DAILY_ADD.getNeededVisits(3));
        assertEquals(10, UserProgressType.DAILY_ADD.getNeededVisits(4));
        assertEquals(15, UserProgressType.DAILY_ADD.getNeededVisits(5));
        assertEquals(21, UserProgressType.DAILY_ADD.getNeededVisits(6));
        assertEquals(28, UserProgressType.DAILY_ADD.getNeededVisits(7));
        assertEquals(528, UserProgressType.DAILY_ADD.getNeededVisits(32));
    }

    @Test
    public void testDailyFibonacci() {
        assertEquals(1, UserProgressType.DAILY_FIBONACCI.getNeededVisits(1));
        assertEquals(1, UserProgressType.DAILY_FIBONACCI.getNeededVisits(2));
        assertEquals(2, UserProgressType.DAILY_FIBONACCI.getNeededVisits(3));
        assertEquals(3, UserProgressType.DAILY_FIBONACCI.getNeededVisits(4));
        assertEquals(5, UserProgressType.DAILY_FIBONACCI.getNeededVisits(5));
        assertEquals(8, UserProgressType.DAILY_FIBONACCI.getNeededVisits(6));
        assertEquals(13, UserProgressType.DAILY_FIBONACCI.getNeededVisits(7));
        assertEquals(610, UserProgressType.DAILY_FIBONACCI.getNeededVisits(15));
    }

    @Test
    public void testDailyPowerOfTwo() {
        assertEquals(1, UserProgressType.DAILY_POWER_OF_TWO.getNeededVisits(1));
        assertEquals(2, UserProgressType.DAILY_POWER_OF_TWO.getNeededVisits(2));
        assertEquals(4, UserProgressType.DAILY_POWER_OF_TWO.getNeededVisits(3));
        assertEquals(8, UserProgressType.DAILY_POWER_OF_TWO.getNeededVisits(4));
        assertEquals(16, UserProgressType.DAILY_POWER_OF_TWO.getNeededVisits(5));
        assertEquals(32, UserProgressType.DAILY_POWER_OF_TWO.getNeededVisits(6));
        assertEquals(64, UserProgressType.DAILY_POWER_OF_TWO.getNeededVisits(7));
        assertEquals(512, UserProgressType.DAILY_POWER_OF_TWO.getNeededVisits(10));
    }
}
