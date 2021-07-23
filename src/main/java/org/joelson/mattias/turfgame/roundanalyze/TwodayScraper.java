package org.joelson.mattias.turfgame.roundanalyze;

import org.joelson.mattias.turfgame.util.URLReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class TwodayScraper {


    public static void main(String[] args) throws IOException {
        int round = 132;
        LocalDate startDate = LocalDate.of(2021, Month.JUNE, 6);
        LocalDate endDate = LocalDate.of(2021, Month.JUNE, 6);
        String[] users = new String[] {
                "Welshman", "Aicar", "fearglas", "jösses...då", "0beroff",
                "deej", "GW007", "PastaPutin", "Lindhardt", "Jonte67"
        };
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            for (String user : users) {
                saveToday(round, user, date);
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void saveToday(int round, String userName, LocalDate date) throws IOException {
        String fileName = String.format("round-%d.%s.%s.html", round, userName, toString(date));
        String content = getToday(userName, date);
        Files.writeString(Path.of (".", fileName), content);
    }

    private static String getToday(String userName, LocalDate date) throws IOException {
        String requestURL = getRequestURL(userName, date);
        System.out.println(String.format("getToday(userName=%s, date=%s) -> requestURL=%s",
                userName, toString(date), requestURL));
        return URLReader.getRequest(requestURL);
    }

    private static String getRequestURL(String userName, LocalDate date) {
        return "https://frut.zundin.se/2day.php?userid=" + userName + "&date=" + toString(date);
    }

    private static String toString(LocalDate date) {
        return DateTimeFormatter.ISO_LOCAL_DATE.format(date);
    }
}
