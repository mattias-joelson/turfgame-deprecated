package org.joelson.turf.dailyinc.db;

import java.util.function.Function;

public enum UserProgressType {

    DAILY_INCREASE(integer -> integer),
    DAILY_ADD(UserProgressType::getDailyAddVisits),
    DAILY_FIBONACCI(UserProgressType::getDailyFibonacciVisits),
    DAILY_POWER_OF_TWO(UserProgressType::getDailyPowerOfTwoVisits);

    private final Function<Integer, Integer> visitsNeeded;

    UserProgressType(Function<Integer, Integer> visitsNeeded) {
        this.visitsNeeded = visitsNeeded;
    }

    private static int getDailyAddVisits(int forDay) {
        int visits = 0;
        for (int day = 1; day <= forDay; day += 1) {
            visits += day;
        }
        return visits;
    }

    private static int getDailyFibonacciVisits(int forDay) {
        if (forDay <= 2) {
            return 1;
        }
        int[] visits = new int[forDay];
        visits[0] = visits[1] = 1;
        for (int day = 2; day < forDay; day += 1) {
            visits[day] = visits[day - 2] + visits[day - 1];
        }
        return visits[forDay - 1];
    }

    private static int getDailyPowerOfTwoVisits(int forDay) {
        int visits = 1;
        for (int day = 1; day < forDay; day += 1) {
            visits *= 2;
        }
        return visits;
    }

    public int getNeededVisits(int day) {
        return visitsNeeded.apply(day);
    }
}
