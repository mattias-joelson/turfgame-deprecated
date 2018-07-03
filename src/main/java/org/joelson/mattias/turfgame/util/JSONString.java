package org.joelson.mattias.turfgame.util;

public class JSONString implements JSONValue {

    private final String str;

    JSONString(String str) {
        this.str = str;
    }

    public String stringValue() {
        StringBuilder sb = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i += 1) {
            if (str.charAt(i) == '\\') {
                switch (str.charAt(i + 1)) {
                case '"':
                case '/':
                case '\\':
                    sb.append(str.charAt(i + 1));
                    i += 1;
                    break;
                case 'b':
                    sb.append('\b');
                    i += 1;
                    break;
                case 'f':
                    sb.append('\b');
                    i += 1;
                    break;
                case 'n':
                    sb.append('\n');
                    i += 1;
                    break;
                case 'r':
                    sb.append('\r');
                    i += 1;
                    break;
                case 't':
                    sb.append('\t');
                    i += 1;
                    break;
                case 'u':
                    sb.append((char) Integer.parseInt(str.substring(i + 2, i + 6), 16));
                    i += 5;
                    break;
                default:
                    throw new IllegalStateException("Invalid JSON string " + this + '!');
                }
            } else {
                sb.append(str.charAt(i));
            }
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JSONString) {
            return str.equals(((JSONString) obj).stringValue());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return str.hashCode();
    }

    @Override
    public String toString() {
        return '"' + str + '"';
    }
}
