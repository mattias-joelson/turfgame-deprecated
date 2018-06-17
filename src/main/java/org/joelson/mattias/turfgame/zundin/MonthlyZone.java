package org.joelson.mattias.turfgame.zundin;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MonthlyZone {

    private static final String ZONE_NAME_LINK_TAG = "<a href=\"http://frut.zundin.se/zone.php?zoneid=";
    private static final String TABLE_CELL_TAG = "<td>";
    private static final String RIGHT_TABLE_CELL_TAG = "<td align=\"right\">";
    private static final String LEFT_TABLE_CELL_TAG = "<td style=\"padding-left:10px\" align=\"left\">";

    private final String name;
    private final int tp;
    private final int pph;
    private final String municipality;
    private final int takes;
    private final int revisits;
    private final int assists;

    public MonthlyZone(String name, int tp, int pph, String municipality, int takes, int revisits, int assists) {
        this.name = validString(name);
        this.tp = validNumber(tp, 65);
        this.pph = validNumber(pph, 0);
        this.municipality = validString(municipality);
        this.takes = validNumber(takes, 0);
        this.revisits = validNumber(revisits, 0);
        this.assists = validNumber(assists, 0);
    }

    public String getName() {
        return name;
    }

    public int getTP() {
        return tp;
    }

    public int getPPH() {
        return pph;
    }

    public String getMunicipality() {
        return municipality;
    }

    public int getTakes() {
        return takes;
    }

    public int getRevisits() {
        return revisits;
    }

    public int getAssists() {
        return assists;
    }

    private static String validString(String s) {
        if (s == null || s.isEmpty()) {
            throw new IllegalArgumentException("Null or empty string!");
        }
        return s;
    }

    private static int validNumber(int i, int min) {
        if (i < min) {
            throw new IllegalArgumentException("Integer " + i + " is less than " + min + '!');
        }
        return i;
    }

    private static class StringPosition {
        private final String str;
        private final int position;

        StringPosition(String str, int position) {
            this.str = str;
            this.position = position;
        }

        int integerValue() {
            return Integer.valueOf(str.trim());
        }

        String stringValue() {
            return str;
        }

        int getPosition() {
            return position;
        }
    }

    public static MonthlyZone fromHTML(String html) {
        StringPosition namePosition = getString(html, ZONE_NAME_LINK_TAG, new StringPosition("", 0));
        StringPosition tpPosition = getString(html, RIGHT_TABLE_CELL_TAG, namePosition);
        StringPosition pphPosition = getString(html, LEFT_TABLE_CELL_TAG, tpPosition);
        StringPosition municipalityPosition = getString(html, TABLE_CELL_TAG, pphPosition);
        StringPosition visitsPosition = getString(html, RIGHT_TABLE_CELL_TAG, municipalityPosition);
        StringPosition takesPosition = getString(html, RIGHT_TABLE_CELL_TAG, visitsPosition);
        StringPosition revisitsPosition = getString(html, RIGHT_TABLE_CELL_TAG, takesPosition);
        StringPosition assistsPosition = getString(html, RIGHT_TABLE_CELL_TAG, revisitsPosition);


        return new MonthlyZone(namePosition.stringValue(), tpPosition.integerValue(), pphPosition.integerValue(),
                municipalityPosition.stringValue(), takesPosition.integerValue(), revisitsPosition.integerValue(),
                assistsPosition.integerValue());
    }

    private static StringPosition getString(String html, String afterPattern, StringPosition lastPosition) {
        int start = html.indexOf('>', html.indexOf(afterPattern, lastPosition.getPosition())) + 1;
        int end = html.indexOf('<', start);
        return new StringPosition(html.substring(start, end), end);
    }

    private static MonthlyZone fromJsoupHTML(String html) {

        Document doc = Jsoup.parse(html);
        Elements rows = doc.getElementsByTag("tr");
        for (Element row : rows) {
            Elements cells = row.getElementsByTag("td");
            return new MonthlyZone(parseZoneName(cells.get(1)), parseInteger(cells.get(2)), parseInteger(cells.get(3)),
                    parseMunicipality(cells.get(4)), parseInteger(cells.get(6)),
                    parseInteger(cells.get(7)), parseInteger(cells.get(8)));
        }
        return null;
    }

    private static String parseZoneName(Element element) {
        Elements links = element.getElementsByTag("a");
        for (Element link : links) {
            return element.text();
        }
        return null;
    }

    private static int parseInteger(Element element) {
        return Integer.valueOf(element.text());
    }

    private static String parseMunicipality(Element element) {
        return element.text();
    }
}
