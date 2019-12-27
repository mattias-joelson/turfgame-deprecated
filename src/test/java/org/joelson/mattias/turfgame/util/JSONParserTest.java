package org.joelson.mattias.turfgame.util;

import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JSONParserTest {

    private static final String COUNTRY = "country";
    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String REGION_LORD = "regionLord";

    @Test
    public void parseRegionTest() throws ParseException {
        JSONValue value = new JSONParser("{\"country\":\"nl\",\"name\":\"Utrecht\",\"id\":95,"
                + "\"regionLord\":{\"name\":\"Mellanfetisen\\u00c4\",\"id\":210311}}").parse();
        assertTrue(value instanceof JSONObject);

        JSONObject object = (JSONObject) value;
        assertTrue(object.containsName(COUNTRY));
        assertTrue(object.getValue(COUNTRY) instanceof JSONString);
        assertEquals("nl", ((JSONString) object.getValue(COUNTRY)).stringValue());

        assertTrue(object.containsName(NAME));
        assertTrue(object.getValue(NAME) instanceof JSONString);
        assertEquals("Utrecht", ((JSONString) object.getValue(NAME)).stringValue());

        assertTrue(object.containsName(ID));
        assertTrue(object.getValue(ID) instanceof JSONNumber);
        assertEquals(95L, ((JSONNumber) object.getValue(ID)).intValue());

        assertTrue(object.containsName(REGION_LORD));
        assertTrue(object.getValue(REGION_LORD) instanceof JSONObject);
        JSONObject regionLord = (JSONObject) object.getValue(REGION_LORD);

        assertTrue(regionLord.containsName(NAME));
        assertTrue(regionLord.getValue(NAME) instanceof JSONString);
        assertEquals("MellanfetisenÄ", ((JSONString) regionLord.getValue(NAME)).stringValue());

        assertTrue(regionLord.containsName(ID));
        assertTrue(regionLord.getValue(ID) instanceof JSONNumber);
        assertEquals(210311L, ((JSONNumber) regionLord.getValue(ID)).intValue());
    }
}
