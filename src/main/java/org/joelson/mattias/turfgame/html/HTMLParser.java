package org.joelson.mattias.turfgame.html;

import java.util.function.Predicate;

public class HTMLParser {

    private final String htmlString;
    private int pos;

    public HTMLParser(String htmlString) {
        this.htmlString = htmlString;
        pos = 0;
    }

    private int nextChar(char ch, int p) {
        int i = htmlString.indexOf(ch, p);
        if (i == -1) {
            i = htmlString.length();
        }
        return i;
    }

    private int skipCharacter(int p, Predicate<Character> pred) {
        while (p < htmlString.length() && pred.test(htmlString.charAt(p))) {
            p += 1;
        }
        return p;
    }

    private int skipWS(int p) {
        return skipCharacter(p, Character::isWhitespace);
    }

    private int skipNonWS(int p) {
        return skipCharacter(p, ch -> !Character.isWhitespace(ch));
    }

    private int skipTag(int p) {
        return skipCharacter(p, Character::isAlphabetic);
    }

    private String substring(int start, int end) {
        return htmlString.substring(Integer.min(start, htmlString.length() - 1),
                Integer.min(end, htmlString.length()));
    }

    public boolean atEnd() {
        return pos >= htmlString.length();
    }

    public HTMLParser nextTag(String s) {
        while (!atEnd()) {
            int p = nextChar('<', pos) + 1;
            int tag = skipWS(p);
            if (s.equalsIgnoreCase(substring(tag, tag + s.length()))) {
                pos = p - 1;
                break;
            }
            pos = p;
        }
        return this;
    }

    private int findEndTag(int p) {
        /*int lessThan = nextChar('<', p);
        while (isBeginTagAt(lessThan)) {
            int endTag = findEndTag(getTagAt(lessThan));
            int endOfEndTag = nextChar('>', endTag);
            lessThan = nextChar('<', p);
        }*/
        int lessThan = nextChar('<', p);
        /*while (!isEndTag(lessThan)) {
            int end = skipOverTag(lessThan);
            lessThan = nextChar('<', end);
        }*/
        if (lessThan >= htmlString.length()) {
            return -1;
        }
//        int n = nextChar('/', lessThan);
//        n = skipWS(n + 1);
        return lessThan;
    }

    public String getTag() {
        int p = nextChar('<', pos) + 1;
        int tag = skipWS(p);
        int end = skipTag(tag);
        return substring(tag, end);
    }

    public String getText() {
        String tag = getTag();
        int endBegin = nextChar('>', pos) + 1;
        int startEnd = findEndTag(endBegin);
        if (startEnd == -1) {
            return "";
        }
        return substring(endBegin, startEnd);
    }
}
