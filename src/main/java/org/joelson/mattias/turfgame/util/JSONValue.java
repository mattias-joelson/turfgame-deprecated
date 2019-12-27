package org.joelson.mattias.turfgame.util;

import java.text.ParseException;

public interface JSONValue {
    
    static JSONValue parse(String s) throws ParseException {
        return new JSONParser(s).parse();
    }
}
