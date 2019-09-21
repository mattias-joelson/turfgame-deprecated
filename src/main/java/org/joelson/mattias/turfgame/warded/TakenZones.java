package org.joelson.mattias.turfgame.warded;

import org.joelson.mattias.turfgame.util.JSONArray;
import org.joelson.mattias.turfgame.util.JSONNumber;
import org.joelson.mattias.turfgame.util.JSONObject;
import org.joelson.mattias.turfgame.util.JSONParseException;
import org.joelson.mattias.turfgame.util.JSONParser;
import org.joelson.mattias.turfgame.util.JSONString;
import org.joelson.mattias.turfgame.util.JSONValue;

import java.util.HashMap;
import java.util.Map;

public class TakenZones {
    
    public static Map<String,Integer> fromHTML(String s) {
        int startIndex = s.indexOf("\"features\": ");
        startIndex = s.indexOf('[', startIndex);
        int endIndex = s.indexOf("});", startIndex);
        endIndex = s.lastIndexOf("]", endIndex) + 1;
        String json = s.substring(startIndex, endIndex);
        JSONArray array = (JSONArray) new JSONParser().parse(json);
        Map<String, Integer> zoneCounts = new HashMap<>();
        for (JSONValue element : array.getElements()) {
            JSONObject object = (JSONObject) element;
            JSONObject properties = (JSONObject) object.getValue("properties");
            JSONString title = (JSONString) properties.getValue("title");
            JSONNumber count = (JSONNumber) properties.getValue("count");
            zoneCounts.put(title.stringValue(), count.intValue());
        }
        return zoneCounts;
    }
    
    private static String unescapeString(String title) {
        StringBuilder sb = new StringBuilder(title.length());
        for (int i = 0; i < title.length(); i += 1) {
            char ch = title.charAt(i);
            if (ch == '\\') {
                if (i + 5 >= title.length()) {
                    throw new JSONParseException("Bad character escape - " + title.substring(i));
                }
                if (title.charAt(i +1 ) != 'u') {
                    throw new JSONParseException("Bad character escape - " + title.substring(i, i + 6));
                }
                String hex = title.substring(i + 2, i + 6);
                char c = (char) Integer.parseInt(hex, 16);
                sb.append(c);
                i += 5;
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }
}
