package org.joelson.mattias.turfgame.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple JSON parser based on https://www.json.org/ and https://tools.ietf.org/pdf/rfc7159.pdf
 */
class JSONParser {

    private static final String TRUE_VALUE = "true"; //NON-NLS
    private static final String FALSE_VALUE = "false"; //NON-NLS
    private static final String NULL_VALUE = "null"; //NON-NLS
    private static final char OBJECT_START_BRACE = '{';
    private static final char OBJECT_END_BRACE = '}';
    private static final char ARRAY_START_BRACE = '[';
    private static final char ARRAY_END_BRACE = ']';
    private static final char STRING_CITATION = '"';
    private static final char NEGATIVE_CHAR = '-';
    private static final char PAIR_COLON = ':';
    private static final char COMMA_CHAR = ',';
    private static final char ESCAPE_CHAR = '\\';
    
    private final String json;
    private int pos;

    /**
     * @param jsonString the string to parse
     */
    JSONParser(String jsonString) {
        json = jsonString;
        pos = 0;
    }

    /**
     * @return a JSON value
     * @throws ParseException if JSON is incorrect
     */
    JSONValue parse() throws ParseException {
        return nextValue();
    }

    private JSONValue nextValue() throws ParseException {
        consumeWhitespaces();
        if (matches(TRUE_VALUE)) {
            pos += TRUE_VALUE.length();
            return new JSONBoolean(true);
        }
        if (matches(FALSE_VALUE)) {
            pos += FALSE_VALUE.length();
            return new JSONBoolean(false);
        }
        if (matches(NULL_VALUE)) {
            pos += NULL_VALUE.length();
            return new JSONNull();
        }
        char next = nextChar();
        if (next == OBJECT_START_BRACE) {
            return nextObject();
        }
        if (next == ARRAY_START_BRACE) {
            return nextArray();
        }
        if (next == STRING_CITATION) {
            return nextString();
        }
        if (Character.isDigit(next) || next == NEGATIVE_CHAR) {
            return nextNumber();
        }
        throw new ParseException(createExceptionMessage(), pos);
    }
    
    private void consumeWhitespaces() {
        while (Character.isWhitespace(nextChar())) {
            pos += 1;
        }
    }
    
    private JSONObject nextObject() throws ParseException {
        expect(OBJECT_START_BRACE);
        List<JSONPair> pairs = new ArrayList<>();
        boolean hasPair = !matches(OBJECT_END_BRACE);
        while (hasPair) {
            pairs.add(nextPair());
            hasPair = hasAnother();
        }
        consumeWhitespaces();
        expect(OBJECT_END_BRACE);
        return new JSONObject(pairs);
    }

    private JSONPair nextPair() throws ParseException {
        JSONString name = nextString();
        expect(PAIR_COLON);
        JSONValue value = nextValue();
        return new JSONPair(name, value);
    }

    private JSONArray nextArray() throws ParseException {
        expect(ARRAY_START_BRACE);
        List<JSONValue> elements = new ArrayList<>();
        boolean hasElement = !matches(ARRAY_END_BRACE);
        while (hasElement) {
            elements.add(nextValue());
            hasElement = hasAnother();
        }
        consumeWhitespaces();
        expect(ARRAY_END_BRACE);
        return new JSONArray(elements);
    }

    private boolean hasAnother() {
        if (matches(COMMA_CHAR)) {
            pos += 1;
            return true;
        }
        return false;
    }

    private void expect(char c) throws ParseException {
        if (nextChar() != c) {
            throw new ParseException(String.format("Expected '%c'", c), pos);
        }
        pos += 1;
    }

    private JSONString nextString() throws ParseException {
        consumeWhitespaces();
        expect('"');
        int startPos = pos;
        boolean hasMore = true;
        do {
            switch (nextChar()) {
            case '"':
                hasMore = false;
                break;
            case '\\':
                nextStringEscape();
                break;
            default:
                char ch = nextChar();
                if (ch >= ' ' && ch != '"' && ch != '\\' && (ch <= '~' || ch >= '¡')) {
                    pos += 1;
                } else {
                    throw new ParseException(String.format("Invalid char '%c' (%d)", ch, (int) ch), pos);
                }
            }
        } while (hasMore);
        String value = json.substring(startPos, pos);
        expect('"');
        return new JSONString(value);
    }

    private void nextStringEscape() throws ParseException {
        pos += 1;
        switch(nextChar()) {
        case '"':
        case '\\':
        case '/':
        case 'b':
        case 'f':
        case 'n':
        case 'r':
        case 't':
            pos += 1;
            break;
        case 'u':
            pos += 1;
            for (int i = 0; i < 4; i += 1) {
                if (!matchesHexDigit()) {
                    throw new ParseException(String.format("Invalid hex digit '%c'", nextChar()), pos);
                }
                pos += 1;
            }
            break;
        default:
            throw new ParseException(String.format("Invalid char '%c' after escape", nextChar()), pos);
        }
    }

    private JSONNumber nextNumber() {
        int startPos = pos;
        if (matches(NEGATIVE_CHAR)) {
            pos += 1;
        }
        if (matches('0')) {
            pos += 1;
        } else {
            consumeDigits();
        }
        if (matches('.')) {
            pos += 1;
            consumeDigits();
        }
        if (matches('e') || matches('E')) {
            pos += 1;
            if (matches('+') || matches('-')) {
                pos += 1;
            }
            consumeDigits();
        }
        return new JSONNumber(json.substring(startPos, pos));
    }

    private void consumeDigits() {
        while (matchesDigit()) {
            pos += 1;
        }
    }

    private char nextChar() {
        return json.charAt(pos);
    }

    private boolean matches(String pattern) {
        int minEnd = Math.min(pos + pattern.length(), json.length());
        return json.substring(pos, minEnd).equals(pattern);
    }

    private boolean matches(char ch) {
        return nextChar() == ch;
    }

    private boolean matchesDigit() {
        char ch = nextChar();
        return ch >= '0' && ch <= '9';
    }

    private boolean matchesHexDigit() {
        char ch = nextChar();
        return (ch >= '0' && ch <= '9') || (ch >= 'A' && ch <='F') || (ch >= 'a' && ch <= 'f');
    }

    private String createExceptionMessage() {
        String jsonStr = json.substring(pos);
        if (jsonStr.length() > 8) {
            jsonStr = jsonStr.substring(0, 8) + "...";
        }
        return String.format("Can not parse JSON \"%s\"", jsonStr); //NON-NLS
    }
}
