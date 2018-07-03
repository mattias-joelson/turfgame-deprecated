package org.joelson.mattias.turfgame.util;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple JSON parser based on https://www.json.org/ and https://tools.ietf.org/pdf/rfc7159.pdf
 */
public class JSONParser {

    private static final String TRUE_VALUE = "true";
    private static final String FALSE_VALUE = "false";
    private static final String NULL_VALUE = "null";

    private String json = "";
    private int pos = 0;

    /**
     *
     * @param jsonString the string to parse
     * @return a JSON value
     * @throws JSONParseException if JSON is incorrect
     */
    public JSONValue parse(String jsonString) {
        this.json = jsonString;
        pos = 0;
        return nextValue();
    }

    private JSONValue nextValue() {
        if (matches(TRUE_VALUE)) {
            pos += TRUE_VALUE.length();
            return new JSONTrue();
        }
        if (matches(FALSE_VALUE)) {
            pos += FALSE_VALUE.length();
            return new JSONFalse();
        }
        if (matches(NULL_VALUE)) {
            pos += NULL_VALUE.length();
            return new JSONNull();
        }
        switch (nextChar()) {
            case '{':
                return nextObject();
            case '[':
                return nextArray();
            case '"':
                return nextString();
            case '-':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return nextNumber();
            default:
                throw new JSONParseException(createExceptionMessage());
        }
    }

    private JSONObject nextObject() {
        expect('{');
        List<JSONPair> pairs = new ArrayList<>();
        boolean hasPair = !matches('}');
        while (hasPair) {
            pairs.add(nextPair());
            hasPair = containsAnother();
        }
        expect('}');
        return new JSONObject(pairs);
    }

    private JSONPair nextPair() {
        JSONString string = nextString();
        expect(':');
        JSONValue value = nextValue();
        return new JSONPair(string, value);
    }

    private JSONArray nextArray() {
        expect('[');
        List<JSONValue> elements = new ArrayList<>();
        boolean hasElement = !matches(']');
        while (hasElement) {
            elements.add(nextValue());
            hasElement = containsAnother();
        }
        expect(']');
        return new JSONArray(elements);
    }

    private boolean containsAnother() {
        if (matches(',')) {
            pos += 1;
            return true;
        }
        return false;
    }

    private void expect(char c) {
        if (nextChar() != c) {
            throw new JSONParseException("Expected '" + c + "' at position " + pos + '!');
        }
        pos += 1;
    }

    private JSONString nextString() {
        expect('"');
        StringBuilder string = new StringBuilder();
        boolean atEnd = false;
        do {
            switch (nextChar()) {
            case '"':
                atEnd = true;
                break;
            case '\\':
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
                    string.append('\\');
                    string.append(nextChar());
                    pos += 1;
                    break;
                case 'u':
                    pos -= 1;
                    new RuntimeException("Not implemented yet!");
                    break;
                default:
                    throw new JSONParseException("Invalid char '" + nextChar() + "' after escape at position "
                            + (pos - 1) + '!');
                }
                break;
            default:
                char ch = nextChar();
                if (ch >= 32 && ch != '"' && ch != '\\' && (ch < 127 || ch > 160) && ch <= 0x10ffff) {
                    string.append(ch);
                    pos += 1;
                } else {
                    throw new JSONParseException("Invalid char '" + ch + "' (" + (int) ch + ") at position "
                            + pos + '!');
                }
            }
        } while (!atEnd);
        expect('"');
        return new JSONString(string.toString());
    }

    private JSONNumber nextNumber() {
        int startPos = pos;
        boolean negative = false;
        if (matches('-')) {
            negative = true;
            pos += 1;
        }
        if (!matches('0')) {
            while (matchesDigit()) {
                pos += 1;
            }
        } else {
            pos += 1;
        }
        boolean integer = true;
        if (matches('.')) {
            integer = false;
            pos += 1;
            while (matchesDigit()) {
                pos += 1;
            }
        }
        if (matches('e') || matches('E')) {
            throw new RuntimeException("Exponents not yet implemented!");
        }
        String numberString = json.substring(startPos, pos);
        Number number;
        if (integer) {
            number = Long.valueOf(numberString);
        } else {
            number = Double.valueOf(numberString);
        }
        return new JSONNumber(number);
    }

    private char nextChar() {
        return json.charAt(pos);
    }

    private boolean matches(String pattern) {
        return json.substring(pos, pos + pattern.length()).equals(pattern);
    }

    private boolean matches(char ch) {
        return nextChar() == ch;
    }

    private boolean matchesDigit() {
        char ch = nextChar();
        return ch >= '0' && ch <= '9';
    }

    private String createExceptionMessage() {
        String jsonStr = json.substring(pos);
        if (jsonStr.length() > 8) {
            jsonStr = jsonStr.substring(0, 8) + "...";
        }
        return "Can not parse JSON \"" + jsonStr + "\" at position " + pos + '!';
    }
}
