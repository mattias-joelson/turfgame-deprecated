package org.joelson.mattias.turfgame.zundin;

import static org.joelson.mattias.turfgame.zundin.Parser.StringPosition;
import static org.joelson.mattias.turfgame.zundin.Parser.validString;
import static org.joelson.mattias.turfgame.zundin.Parser.validNumber;

public class MonthlyZone {

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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MonthlyZone) {
            MonthlyZone that = (MonthlyZone) obj;
            return name.equals(that.getName()) && municipality.equals(that.getMunicipality())
                    && tp == that.getTP() && pph == that.getPPH()
                    && takes == that.getTakes() && revisits == that.getRevisits() && assists == that.getAssists();
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public static MonthlyZone fromHTML(String html) {
        StringPosition namePosition = Parser.getString(html, Parser.ZONE_NAME_LINK_TAG, new StringPosition("", 0));
        StringPosition tpPosition = Parser.getString(html, Parser.RIGHT_TABLE_CELL_TAG, namePosition);
        StringPosition pphPosition = Parser.getString(html, Parser.LEFT_TABLE_CELL_TAG, tpPosition);
        StringPosition municipalityPosition = Parser.getString(html, Parser.TABLE_CELL_TAG, pphPosition);
        StringPosition visitsPosition = Parser.getString(html, Parser.RIGHT_TABLE_CELL_TAG, municipalityPosition);
        StringPosition takesPosition = Parser.getString(html, Parser.RIGHT_TABLE_CELL_TAG, visitsPosition);
        StringPosition revisitsPosition = Parser.getString(html, Parser.RIGHT_TABLE_CELL_TAG, takesPosition);
        StringPosition assistsPosition = Parser.getString(html, Parser.RIGHT_TABLE_CELL_TAG, revisitsPosition);


        return new MonthlyZone(namePosition.stringValue(), tpPosition.integerValue(), pphPosition.integerValue(),
                municipalityPosition.stringValue(), takesPosition.integerValue(), revisitsPosition.integerValue(),
                assistsPosition.integerValue());
    }
}
