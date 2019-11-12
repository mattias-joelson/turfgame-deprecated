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
    private int pos;

    /**
     *
     * @param jsonString the string to parse
     * @return a JSON value
     * @throws JSONParseException if JSON is incorrect
     */
    public JSONValue parse(String jsonString) {
        json = jsonString;
        pos = 0;
        return nextValue();
    }

    private JSONValue nextValue() {
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
    
    private void consumeWhitespaces() {
        for (;;) {
            switch (nextChar()) {
                case ' ':
                case '\t':
                case '\n':
                    pos += 1;
                    break;
                default:
                    return;
            }
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
        consumeWhitespaces();
        expect('}');
        return new JSONObject(pairs);
    }

    private JSONPair nextPair() {
        JSONString name = nextString();
        expect(':');
        JSONValue value = nextValue();
        return new JSONPair(name, value);
    }

    private JSONArray nextArray() {
        expect('[');
        List<JSONValue> elements = new ArrayList<>();
        boolean hasElement = !matches(']');
        while (hasElement) {
            elements.add(nextValue());
            hasElement = containsAnother();
        }
        consumeWhitespaces();
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
                    throw new JSONParseException("Invalid char '" + ch + "' (" + (int) ch + ") at position "
                            + pos + '!');
                }
            }
        } while (hasMore);
        String value = json.substring(startPos, pos);
        expect('"');
        return new JSONString(value);
    }

    private void nextStringEscape() {
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
                    throw new JSONParseException("Invalid hex digit '" + nextChar() + "' at postion " + pos + '!');
                }
                pos += 1;
            }
            break;
        default:
            throw new JSONParseException("Invalid char '" + nextChar() + "' after escape at position "
                    + (pos - 1) + '!');
        }
    }

    private JSONNumber nextNumber() {
        int startPos = pos;
        if (matches('-')) {
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
        return "Can not parse JSON \"" + jsonStr + "\" at position " + pos + '!';
    }
}
