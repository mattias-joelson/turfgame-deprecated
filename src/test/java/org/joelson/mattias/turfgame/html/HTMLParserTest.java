package org.joelson.mattias.turfgame.html;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HTMLParserTest {

    private static final String HTML_ROW = "   <tr bgcolor=\"#b0e0e6\">\n" +
            "    <td align=\"right\">4</td>\n" +
            "    <td><a href=\"https://www.turfgame.com/zone/Southball\"><img height=\"12\" src=\"./monthly_oberoff_round96_files/turf.ico\"></a> <a href=\"http://frut.zundin.se/zone.php?zoneid=257&amp;roundid=96\">Southball</a></td>\n" +
            "\t<td align=\"right\">65</td>\n" +
            "\t<td style=\"padding-left:10px\" align=\"left\"> +9</td>\n" +
            "\t<td>Stockholms kommun</td>\n" +
            "\t<td align=\"right\">6</td>\n" +
            "\t<td align=\"right\">6</td>\n" +
            "\t<td align=\"right\">0</td>\n" +
            "\t<td align=\"right\">1</td>\n" +
            "   </tr>\n" +
            "\t\t\n";

    private static final String SIMPLE_HTML = "<b>hello</B>";

    private HTMLParser htmlParser;

    @Before
    public void setupParser() {
        htmlParser = new HTMLParser(HTML_ROW);
    }

    @Test
    public void initialNotAtEnd() {
        assertTrue(!htmlParser.atEnd());
    }

    @Test
    public void unknownTagAtEnd() {
        assertTrue(htmlParser.nextTag("table").atEnd());
    }

    @Test
    public void firstTDTag() {
        String tag = htmlParser.nextTag("td").getTag();
        assertEquals("td", tag);
        assertTrue(!htmlParser.atEnd());
    }

    @Test
    public void firstTDText() {
        String text = htmlParser.nextTag("td").getText();
        assertEquals("4", text);
        assertTrue(!htmlParser.atEnd());
    }

    @Test
    public void getSimpleTag() {
        HTMLParser parser = new HTMLParser(SIMPLE_HTML);
        String tag = parser.nextTag("b").getTag();
        assertEquals("b", tag);
    }

    @Test
    public void getSimpleText() {
        HTMLParser parser = new HTMLParser(SIMPLE_HTML);
        String text = parser.nextTag("b").getText();
        assertEquals("hello", text);
    }
}