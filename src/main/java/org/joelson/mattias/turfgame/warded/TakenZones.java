package org.joelson.mattias.turfgame.warded;

import org.joelson.mattias.turfgame.util.JSONArray;
import org.joelson.mattias.turfgame.util.JSONNumber;
import org.joelson.mattias.turfgame.util.JSONObject;
import org.joelson.mattias.turfgame.util.JSONString;

import java.text.ParseException;
import java.util.Map;
import java.util.stream.Collectors;

public final class TakenZones {
    
    private static final String PROPERTIES_PROPERTY = "properties"; // NON-NLS
    private static final String TITLE_PROPERTY = "title"; // NON-NLS
    private static final String COUNT_PROPERTY = "count"; // NON-NLS
    private static final char ARRAY_START = '[';
    private static final char ARRAY_END = ']';
    
    private static final class ZoneCount {
        
        private final String zonename;
        private final int count;
        
        private ZoneCount(String zonename, int count) {
            this.zonename = zonename;
            this.count = count;
        }
        
        public String getZonename() {
            return zonename;
        }
        
        public int getCount() {
            return count;
        }
        
        private static ZoneCount fromJSON(JSONObject object) {
            JSONObject properties = (JSONObject) object.getValue(PROPERTIES_PROPERTY);
            JSONString title = (JSONString) properties.getValue(TITLE_PROPERTY);
            JSONNumber count = (JSONNumber) properties.getValue(COUNT_PROPERTY);
            return new ZoneCount(title.stringValue(), count.intValue());
        }
    }
    
    private TakenZones() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }
    
    public static Map<String,Integer> fromHTML(String s) throws ParseException {
        return JSONArray.parseArray(getZonesJSONSting(s)).stream()
                .map(JSONObject.class::cast)
                .map(ZoneCount::fromJSON)
                .collect(Collectors.toMap(ZoneCount::getZonename, ZoneCount::getCount));
    }
    
    private static String getZonesJSONSting(String s) {
        int startIndex = s.indexOf("\"features\": "); //NON-NLS
        startIndex = s.indexOf(ARRAY_START, startIndex);
        int endIndex = s.indexOf("});", startIndex);
        endIndex = s.lastIndexOf(ARRAY_END, endIndex) + 1;
        return s.substring(startIndex, endIndex);
    }
}
